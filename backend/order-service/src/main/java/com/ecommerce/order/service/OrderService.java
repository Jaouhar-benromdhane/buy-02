package com.ecommerce.order.service;

import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.UpdateOrderStatusRequest;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.model.PaymentStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVICE : Logique métier des commandes
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    /**
     * Créer une nouvelle commande
     */
    public Order createOrder(CreateOrderRequest request) {
        log.info("Creating new order for user: {}", request.getUserId());
        
        // Calculer les sous-totaux des items
        request.getItems().forEach(OrderItem::calculateSubtotal);
        
        // Créer la commande
        Order order = new Order();
        order.setOrderNumber(Order.generateOrderNumber());
        order.setUserId(request.getUserId());
        order.setUserName(request.getUserName());
        order.setUserEmail(request.getUserEmail());
        order.setItems(request.getItems());
        order.setShippingCost(request.getShippingCost());
        order.setTax(request.getTax());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setShippingAddress(request.getShippingAddress());
        order.setNotes(request.getNotes());
        
        // Calculer le montant total
        order.calculateTotalAmount();
        
        // Définir les statuts initiaux
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        
        // Définir les dates
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        // Sauvegarder
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully: {}", savedOrder.getOrderNumber());
        
        return savedOrder;
    }
    
    /**
     * Récupérer toutes les commandes d'un utilisateur
     */
    public List<Order> getUserOrders(String userId) {
        log.info("Fetching orders for user: {}", userId);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    /**
     * Récupérer toutes les commandes d'un vendeur
     */
    public List<Order> getSellerOrders(String sellerId) {
        log.info("Fetching orders for seller: {}", sellerId);
        return orderRepository.findBySellerIdOrderByCreatedAtDesc(sellerId);
    }
    
    /**
     * Récupérer une commande par son ID
     */
    public Order getOrderById(String orderId) {
        log.info("Fetching order by ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }
    
    /**
     * Récupérer une commande par son numéro
     */
    public Order getOrderByNumber(String orderNumber) {
        log.info("Fetching order by number: {}", orderNumber);
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
    }
    
    /**
     * Mettre à jour le statut d'une commande
     */
    public Order updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        log.info("Updating order status: {} -> {}", orderId, request.getStatus());
        
        Order order = getOrderById(orderId);
        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();
        
        // Mettre à jour le statut
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        
        // Mettre à jour les dates selon le statut
        switch (newStatus) {
            case CONFIRMED:
                order.setConfirmedAt(LocalDateTime.now());
                break;
            case SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                order.setPaymentStatus(PaymentStatus.PAID);  // Marquer comme payé à la livraison
                break;
            case CANCELLED:
                order.setCancelledAt(LocalDateTime.now());
                order.setCancellationReason(request.getCancellationReason());
                break;
        }
        
        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated: {} -> {}", oldStatus, newStatus);
        
        return updatedOrder;
    }
    
    /**
     * Annuler une commande
     */
    public Order cancelOrder(String orderId, String reason) {
        log.info("Cancelling order: {}", orderId);
        
        Order order = getOrderById(orderId);
        
        // Vérifier si la commande peut être annulée
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel a delivered order");
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }
        
        // Annuler
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(reason);
        order.setUpdatedAt(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * Supprimer une commande (soft delete - changement de statut)
     */
    public void deleteOrder(String orderId) {
        log.info("Deleting order: {}", orderId);
        
        Order order = getOrderById(orderId);
        
        // Vérifier si la commande peut être supprimée
        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new RuntimeException("Only cancelled orders can be deleted");
        }
        
        orderRepository.deleteById(orderId);
        log.info("Order deleted: {}", orderId);
    }
    
    /**
     * Rechercher des commandes
     */
    public List<Order> searchOrders(String query) {
        log.info("Searching orders with query: {}", query);
        
        // Recherche par numéro de commande ou nom d'utilisateur
        List<Order> ordersByNumber = orderRepository.findByOrderNumberContainingIgnoreCaseOrderByCreatedAtDesc(query);
        List<Order> ordersByName = orderRepository.findByUserNameContainingIgnoreCaseOrderByCreatedAtDesc(query);
        
        // Combiner les résultats (sans doublons)
        ordersByNumber.addAll(ordersByName);
        return ordersByNumber.stream().distinct().toList();
    }
    
    /**
     * Filtrer les commandes par statut pour un utilisateur
     */
    public List<Order> filterUserOrdersByStatus(String userId, OrderStatus status) {
        log.info("Filtering orders for user {} with status: {}", userId, status);
        return orderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
    }
    
    /**
     * Filtrer les commandes par statut pour un vendeur
     */
    public List<Order> filterSellerOrdersByStatus(String sellerId, OrderStatus status) {
        log.info("Filtering orders for seller {} with status: {}", sellerId, status);
        return orderRepository.findBySellerIdAndStatus(sellerId, status);
    }
}

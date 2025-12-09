package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.UpdateOrderStatusRequest;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER : API REST pour la gestion des commandes
 * 
 * Endpoints:
 * - POST   /api/orders                    : Créer une commande
 * - GET    /api/orders/user/{userId}      : Récupérer les commandes d'un utilisateur
 * - GET    /api/orders/seller/{sellerId}  : Récupérer les commandes d'un vendeur
 * - GET    /api/orders/{orderId}          : Récupérer une commande par ID
 * - GET    /api/orders/number/{orderNumber} : Récupérer une commande par numéro
 * - PUT    /api/orders/{orderId}/status   : Mettre à jour le statut
 * - DELETE /api/orders/{orderId}          : Supprimer une commande
 * - POST   /api/orders/{orderId}/cancel   : Annuler une commande
 * - GET    /api/orders/search             : Rechercher des commandes
 * - GET    /api/orders/user/{userId}/filter : Filtrer les commandes utilisateur
 * - GET    /api/orders/seller/{sellerId}/filter : Filtrer les commandes vendeur
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * Créer une nouvelle commande
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("POST /api/orders - Creating order for user: {}", request.getUserId());
        try {
            Order order = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            log.error("Error creating order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer toutes les commandes d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable String userId) {
        log.info("GET /api/orders/user/{} - Fetching user orders", userId);
        try {
            List<Order> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Error fetching user orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer toutes les commandes d'un vendeur
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Order>> getSellerOrders(@PathVariable String sellerId) {
        log.info("GET /api/orders/seller/{} - Fetching seller orders", sellerId);
        try {
            List<Order> orders = orderService.getSellerOrders(sellerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Error fetching seller orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer une commande par son ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        log.info("GET /api/orders/{} - Fetching order", orderId);
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            log.error("Order not found: {}", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error fetching order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer une commande par son numéro
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getOrderByNumber(@PathVariable String orderNumber) {
        log.info("GET /api/orders/number/{} - Fetching order", orderNumber);
        try {
            Order order = orderService.getOrderByNumber(orderNumber);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            log.error("Order not found: {}", orderNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error fetching order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Mettre à jour le statut d'une commande
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        log.info("PUT /api/orders/{}/status - Updating status to {}", orderId, request.getStatus());
        try {
            Order order = orderService.updateOrderStatus(orderId, request);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            log.error("Error updating order status", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error updating order status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Annuler une commande
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String orderId,
            @RequestParam(required = false) String reason) {
        log.info("POST /api/orders/{}/cancel - Cancelling order", orderId);
        try {
            Order order = orderService.cancelOrder(orderId, reason);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            log.error("Error cancelling order", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error cancelling order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Supprimer une commande
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        log.info("DELETE /api/orders/{} - Deleting order", orderId);
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting order", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error deleting order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Rechercher des commandes
     */
    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(@RequestParam String query) {
        log.info("GET /api/orders/search?query={}", query);
        try {
            List<Order> orders = orderService.searchOrders(query);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Error searching orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Filtrer les commandes d'un utilisateur par statut
     */
    @GetMapping("/user/{userId}/filter")
    public ResponseEntity<List<Order>> filterUserOrders(
            @PathVariable String userId,
            @RequestParam OrderStatus status) {
        log.info("GET /api/orders/user/{}/filter?status={}", userId, status);
        try {
            List<Order> orders = orderService.filterUserOrdersByStatus(userId, status);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Error filtering user orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Filtrer les commandes d'un vendeur par statut
     */
    @GetMapping("/seller/{sellerId}/filter")
    public ResponseEntity<List<Order>> filterSellerOrders(
            @PathVariable String sellerId,
            @RequestParam OrderStatus status) {
        log.info("GET /api/orders/seller/{}/filter?status={}", sellerId, status);
        try {
            List<Order> orders = orderService.filterSellerOrdersByStatus(sellerId, status);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Error filtering seller orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

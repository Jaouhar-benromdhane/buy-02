package com.ecommerce.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ENTITÉ ORDER
 * 
 * Représente une commande complète dans le système
 * Stockée dans MongoDB dans la collection "orders"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String orderNumber;  // Ex: ORD-2025-001234
    
    // Informations client
    private String userId;
    private String userName;
    private String userEmail;
    
    // Articles commandés
    private List<OrderItem> items;
    
    // Montants
    private Double subtotal;
    private Double shippingCost;
    private Double tax;
    private Double totalAmount;
    
    // Statut
    @Indexed
    private OrderStatus status;
    
    // Paiement
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    
    // Adresse de livraison
    private ShippingAddress shippingAddress;
    
    // Dates
    @Indexed
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    
    // Notes
    private String notes;
    private String cancellationReason;
    
    /**
     * Calculer le montant total de la commande
     */
    public void calculateTotalAmount() {
        this.subtotal = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        this.totalAmount = this.subtotal + this.shippingCost + this.tax;
    }
    
    /**
     * Générer un numéro de commande unique
     */
    public static String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().getYear() + 
               "-" + System.currentTimeMillis();
    }
}

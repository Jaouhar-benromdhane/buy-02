package com.ecommerce.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SOUS-DOCUMENT : Article dans une commande
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    private String productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;  // quantity * unitPrice
    private String sellerId;
    private String sellerName;
    
    /**
     * Calculer le sous-total
     */
    public void calculateSubtotal() {
        this.subtotal = this.quantity * this.unitPrice;
    }
}

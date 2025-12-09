package com.ecommerce.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * ENTITÉ CART ITEM
 * 
 * Représente un article dans le panier d'un utilisateur
 * Stocké dans MongoDB dans la collection "cart_items"
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart_items")
@CompoundIndex(name = "user_product_idx", def = "{'userId': 1, 'productId': 1}", unique = true)
public class CartItem {
    
    @Id
    private String id;
    
    // Utilisateur
    private String userId;
    
    // Produit
    private String productId;
    private String productName;
    private String productImage;
    private Double productPrice;
    
    // Quantité
    private Integer quantity;
    
    // Vendeur
    private String sellerId;
    private String sellerName;
    
    // Dates
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    
    /**
     * Calculer le sous-total pour cet item
     */
    public Double getSubtotal() {
        return productPrice * quantity;
    }
}

package com.ecommerce.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO : Requête pour ajouter un produit au panier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    
    @NotNull(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Product ID is required")
    private String productId;
    
    @NotNull(message = "Product name is required")
    private String productName;
    
    private String productImage;
    
    @NotNull(message = "Product price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double productPrice;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Seller ID is required")
    private String sellerId;
    
    @NotNull(message = "Seller name is required")
    private String sellerName;
}

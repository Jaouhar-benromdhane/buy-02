package com.ecommerce.user.dto;

import com.ecommerce.user.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO : Réponse avec le résumé du panier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartSummary {
    
    private List<CartItem> items;
    private Integer totalItems;
    private Double totalAmount;
    
    /**
     * Calculer le résumé à partir d'une liste de CartItems
     */
    public static CartSummary from(List<CartItem> items) {
        CartSummary summary = new CartSummary();
        summary.setItems(items);
        summary.setTotalItems(items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum());
        summary.setTotalAmount(items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum());
        return summary;
    }
}

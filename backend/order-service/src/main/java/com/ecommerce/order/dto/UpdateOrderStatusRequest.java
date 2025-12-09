package com.ecommerce.order.dto;

import com.ecommerce.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO : Requête pour mettre à jour le statut d'une commande
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    
    private OrderStatus status;
    private String cancellationReason;  // Optionnel, requis si status = CANCELLED
}

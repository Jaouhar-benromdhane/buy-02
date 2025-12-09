package com.ecommerce.order.dto;

import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.PaymentMethod;
import com.ecommerce.order.model.ShippingAddress;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO : Requête pour créer une commande
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotNull(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "User name is required")
    private String userName;
    
    @NotNull(message = "User email is required")
    private String userEmail;
    
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItem> items;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    @NotNull(message = "Shipping address is required")
    private ShippingAddress shippingAddress;
    
    private Double shippingCost = 0.0;
    private Double tax = 0.0;
    private String notes;
}

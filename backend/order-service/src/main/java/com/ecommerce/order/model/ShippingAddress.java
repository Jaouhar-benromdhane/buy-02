package com.ecommerce.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SOUS-DOCUMENT : Adresse de livraison
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {
    
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
}

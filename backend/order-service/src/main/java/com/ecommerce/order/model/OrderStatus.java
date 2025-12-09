package com.ecommerce.order.model;

/**
 * ENUM : Statuts possibles d'une commande
 */
public enum OrderStatus {
    PENDING,      // En attente de confirmation
    CONFIRMED,    // Confirmée par le vendeur
    PROCESSING,   // En cours de préparation
    SHIPPED,      // Expédiée
    DELIVERED,    // Livrée
    CANCELLED     // Annulée
}

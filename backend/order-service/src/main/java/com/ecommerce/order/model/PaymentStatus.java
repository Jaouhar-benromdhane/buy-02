package com.ecommerce.order.model;

/**
 * ENUM : Statuts de paiement
 */
public enum PaymentStatus {
    PENDING,   // En attente
    PAID,      // Payé
    FAILED,    // Échec
    REFUNDED   // Remboursé
}

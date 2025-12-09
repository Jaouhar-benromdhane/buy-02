package com.ecommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * ===================================
 * ORDER SERVICE - APPLICATION PRINCIPALE
 * ===================================
 * 
 * Microservice de gestion des commandes
 * Port: 8084
 * Database: ecommerce_orders (MongoDB)
 * 
 * Fonctionnalités:
 * - Création de commandes
 * - Suivi du cycle de vie des commandes
 * - Gestion des statuts (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
 * - Recherche et filtrage des commandes
 * - Support paiement à la livraison
 */
@SpringBootApplication
@EnableMongoRepositories
public class OrderServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("\n" +
                "╔══════════════════════════════════════════════════╗\n" +
                "║      ORDER SERVICE STARTED SUCCESSFULLY  🚀      ║\n" +
                "║                                                  ║\n" +
                "║  Port:     8084 (HTTPS)                          ║\n" +
                "║  Database: ecommerce_orders (MongoDB)            ║\n" +
                "║  API:      https://localhost:8084/api/orders     ║\n" +
                "╚══════════════════════════════════════════════════╝\n");
    }
}

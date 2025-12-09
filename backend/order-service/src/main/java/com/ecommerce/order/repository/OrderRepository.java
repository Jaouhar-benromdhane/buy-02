package com.ecommerce.order.repository;

import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY : Accès aux données des commandes
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    
    // Trouver une commande par son numéro
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // Trouver toutes les commandes d'un utilisateur
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
    
    // Trouver les commandes d'un utilisateur par statut
    List<Order> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, OrderStatus status);
    
    // Trouver toutes les commandes d'un vendeur (via les items)
    @Query("{ 'items.sellerId': ?0 }")
    List<Order> findBySellerIdOrderByCreatedAtDesc(String sellerId);
    
    // Trouver les commandes d'un vendeur par statut
    @Query("{ 'items.sellerId': ?0, 'status': ?1 }")
    List<Order> findBySellerIdAndStatus(String sellerId, OrderStatus status);
    
    // Recherche par nom d'utilisateur (pour admin ou vendeur)
    List<Order> findByUserNameContainingIgnoreCaseOrderByCreatedAtDesc(String userName);
    
    // Recherche par numéro de commande (contient)
    List<Order> findByOrderNumberContainingIgnoreCaseOrderByCreatedAtDesc(String orderNumber);
    
    // Compter les commandes par statut pour un utilisateur
    long countByUserIdAndStatus(String userId, OrderStatus status);
    
    // Compter les commandes d'un vendeur
    @Query(value = "{ 'items.sellerId': ?0 }", count = true)
    long countBySellerId(String sellerId);
}

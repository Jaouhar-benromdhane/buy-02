package com.ecommerce.user.repository;

import com.ecommerce.user.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY : Accès aux données du panier
 */
@Repository
public interface CartRepository extends MongoRepository<CartItem, String> {
    
    // Trouver tous les items du panier d'un utilisateur
    List<CartItem> findByUserIdOrderByAddedAtDesc(String userId);
    
    // Trouver un item spécifique (user + produit)
    Optional<CartItem> findByUserIdAndProductId(String userId, String productId);
    
    // Supprimer tous les items du panier d'un utilisateur
    void deleteByUserId(String userId);
    
    // Compter les items dans le panier
    long countByUserId(String userId);
    
    // Vérifier si un produit est dans le panier
    boolean existsByUserIdAndProductId(String userId, String productId);
}

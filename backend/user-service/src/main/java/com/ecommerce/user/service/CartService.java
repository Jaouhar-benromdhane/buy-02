package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddToCartRequest;
import com.ecommerce.user.dto.CartSummary;
import com.ecommerce.user.model.CartItem;
import com.ecommerce.user.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVICE : Logique métier du panier
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    
    private final CartRepository cartRepository;
    
    /**
     * Ajouter un produit au panier
     * Si le produit existe déjà, met à jour la quantité
     */
    @Transactional
    public CartItem addToCart(AddToCartRequest request) {
        log.info("Adding product {} to cart for user {}", request.getProductId(), request.getUserId());
        
        // Vérifier si le produit est déjà dans le panier
        var existingItem = cartRepository.findByUserIdAndProductId(
                request.getUserId(), 
                request.getProductId()
        );
        
        if (existingItem.isPresent()) {
            // Mettre à jour la quantité
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setUpdatedAt(LocalDateTime.now());
            log.info("Updated quantity for product {} in cart", request.getProductId());
            return cartRepository.save(item);
        } else {
            // Créer un nouvel item
            CartItem newItem = new CartItem();
            newItem.setUserId(request.getUserId());
            newItem.setProductId(request.getProductId());
            newItem.setProductName(request.getProductName());
            newItem.setProductImage(request.getProductImage());
            newItem.setProductPrice(request.getProductPrice());
            newItem.setQuantity(request.getQuantity());
            newItem.setSellerId(request.getSellerId());
            newItem.setSellerName(request.getSellerName());
            newItem.setAddedAt(LocalDateTime.now());
            newItem.setUpdatedAt(LocalDateTime.now());
            
            log.info("Added new product {} to cart", request.getProductId());
            return cartRepository.save(newItem);
        }
    }
    
    /**
     * Récupérer le panier complet d'un utilisateur avec résumé
     */
    public CartSummary getCart(String userId) {
        log.info("Fetching cart for user {}", userId);
        List<CartItem> items = cartRepository.findByUserIdOrderByAddedAtDesc(userId);
        return CartSummary.from(items);
    }
    
    /**
     * Récupérer tous les items du panier
     */
    public List<CartItem> getCartItems(String userId) {
        log.info("Fetching cart items for user {}", userId);
        return cartRepository.findByUserIdOrderByAddedAtDesc(userId);
    }
    
    /**
     * Mettre à jour la quantité d'un item
     */
    @Transactional
    public CartItem updateQuantity(String cartItemId, Integer quantity) {
        log.info("Updating quantity for cart item {}", cartItemId);
        
        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + cartItemId));
        
        item.setQuantity(quantity);
        item.setUpdatedAt(LocalDateTime.now());
        
        return cartRepository.save(item);
    }
    
    /**
     * Supprimer un item du panier
     */
    @Transactional
    public void removeItem(String cartItemId) {
        log.info("Removing cart item {}", cartItemId);
        
        if (!cartRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found: " + cartItemId);
        }
        
        cartRepository.deleteById(cartItemId);
        log.info("Cart item {} removed successfully", cartItemId);
    }
    
    /**
     * Supprimer un item par userId et productId
     */
    @Transactional
    public void removeItemByProduct(String userId, String productId) {
        log.info("Removing product {} from cart for user {}", productId, userId);
        
        CartItem item = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));
        
        cartRepository.delete(item);
        log.info("Product {} removed from cart", productId);
    }
    
    /**
     * Vider le panier complet d'un utilisateur
     */
    @Transactional
    public void clearCart(String userId) {
        log.info("Clearing cart for user {}", userId);
        cartRepository.deleteByUserId(userId);
        log.info("Cart cleared for user {}", userId);
    }
    
    /**
     * Compter le nombre d'items dans le panier
     */
    public long getCartItemCount(String userId) {
        return cartRepository.countByUserId(userId);
    }
    
    /**
     * Vérifier si un produit est dans le panier
     */
    public boolean isProductInCart(String userId, String productId) {
        return cartRepository.existsByUserIdAndProductId(userId, productId);
    }
}

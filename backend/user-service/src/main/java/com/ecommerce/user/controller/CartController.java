package com.ecommerce.user.controller;

import com.ecommerce.user.dto.AddToCartRequest;
import com.ecommerce.user.dto.CartSummary;
import com.ecommerce.user.dto.UpdateCartQuantityRequest;
import com.ecommerce.user.model.CartItem;
import com.ecommerce.user.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER : API REST pour la gestion du panier
 * 
 * Endpoints:
 * - POST   /api/cart                    : Ajouter un produit au panier
 * - GET    /api/cart/{userId}           : Récupérer le panier complet
 * - GET    /api/cart/{userId}/items     : Récupérer les items du panier
 * - PUT    /api/cart/item/{itemId}      : Mettre à jour la quantité
 * - DELETE /api/cart/item/{itemId}      : Supprimer un item
 * - DELETE /api/cart/{userId}/product/{productId} : Supprimer par produit
 * - DELETE /api/cart/{userId}           : Vider le panier
 * - GET    /api/cart/{userId}/count     : Nombre d'items
 * - GET    /api/cart/{userId}/has/{productId} : Vérifier si produit dans panier
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CartController {
    
    private final CartService cartService;
    
    /**
     * Ajouter un produit au panier
     */
    @PostMapping
    public ResponseEntity<CartItem> addToCart(@Valid @RequestBody AddToCartRequest request) {
        log.info("POST /api/cart - Adding product to cart");
        try {
            CartItem item = cartService.addToCart(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (Exception e) {
            log.error("Error adding to cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer le panier complet avec résumé
     */
    @GetMapping("/{userId}")
    public ResponseEntity<CartSummary> getCart(@PathVariable String userId) {
        log.info("GET /api/cart/{} - Fetching cart", userId);
        try {
            CartSummary cart = cartService.getCart(userId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            log.error("Error fetching cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupérer uniquement les items du panier
     */
    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String userId) {
        log.info("GET /api/cart/{}/items - Fetching cart items", userId);
        try {
            List<CartItem> items = cartService.getCartItems(userId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error fetching cart items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Mettre à jour la quantité d'un item
     */
    @PutMapping("/item/{itemId}")
    public ResponseEntity<CartItem> updateQuantity(
            @PathVariable String itemId,
            @Valid @RequestBody UpdateCartQuantityRequest request) {
        log.info("PUT /api/cart/item/{} - Updating quantity", itemId);
        try {
            CartItem item = cartService.updateQuantity(itemId, request.getQuantity());
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            log.error("Error updating quantity", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error updating quantity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Supprimer un item du panier
     */
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable String itemId) {
        log.info("DELETE /api/cart/item/{} - Removing item", itemId);
        try {
            cartService.removeItem(itemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error removing item", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error removing item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Supprimer un produit du panier (par userId et productId)
     */
    @DeleteMapping("/{userId}/product/{productId}")
    public ResponseEntity<Void> removeItemByProduct(
            @PathVariable String userId,
            @PathVariable String productId) {
        log.info("DELETE /api/cart/{}/product/{} - Removing product", userId, productId);
        try {
            cartService.removeItemByProduct(userId, productId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error removing product", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error removing product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Vider le panier complet
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        log.info("DELETE /api/cart/{} - Clearing cart", userId);
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error clearing cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Compter le nombre d'items dans le panier
     */
    @GetMapping("/{userId}/count")
    public ResponseEntity<Long> getCartItemCount(@PathVariable String userId) {
        log.info("GET /api/cart/{}/count - Getting item count", userId);
        try {
            long count = cartService.getCartItemCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error getting cart count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Vérifier si un produit est dans le panier
     */
    @GetMapping("/{userId}/has/{productId}")
    public ResponseEntity<Boolean> isProductInCart(
            @PathVariable String userId,
            @PathVariable String productId) {
        log.info("GET /api/cart/{}/has/{} - Checking if product in cart", userId, productId);
        try {
            boolean exists = cartService.isProductInCart(userId, productId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking product in cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddToCartRequest;
import com.ecommerce.user.dto.CartSummary;
import com.ecommerce.user.model.CartItem;
import com.ecommerce.user.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * TESTS UNITAIRES - CART SERVICE
 * 
 * Tests critiques :
 * 1. Ajouter produit au panier
 * 2. Mise à jour quantité existante
 * 3. Récupérer panier complet
 * 4. Mettre à jour quantité
 * 5. Supprimer item du panier
 * 6. Vider le panier
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private AddToCartRequest validRequest;
    private CartItem mockCartItem;

    @BeforeEach
    void setUp() {
        // Préparer données de test
        validRequest = new AddToCartRequest();
        validRequest.setUserId("user123");
        validRequest.setProductId("product123");
        validRequest.setProductName("Test Product");
        validRequest.setProductImage("image.jpg");
        validRequest.setProductPrice(99.99);
        validRequest.setQuantity(2);
        validRequest.setSellerId("seller123");
        validRequest.setSellerName("Test Seller");

        mockCartItem = new CartItem();
        mockCartItem.setId("cart123");
        mockCartItem.setUserId("user123");
        mockCartItem.setProductId("product123");
        mockCartItem.setProductName("Test Product");
        mockCartItem.setProductImage("image.jpg");
        mockCartItem.setProductPrice(99.99);
        mockCartItem.setQuantity(2);
        mockCartItem.setSellerId("seller123");
        mockCartItem.setSellerName("Test Seller");
        mockCartItem.setAddedAt(LocalDateTime.now());
        mockCartItem.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * TEST 1 : AJOUTER PRODUIT AU PANIER - Nouveau produit
     */
    @Test
    void testAddToCart_NewProduct() {
        // Arrange
        when(cartRepository.findByUserIdAndProductId(anyString(), anyString())).thenReturn(Optional.empty());
        when(cartRepository.save(any(CartItem.class))).thenReturn(mockCartItem);

        // Act
        CartItem result = cartService.addToCart(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals("cart123", result.getId());
        assertEquals("product123", result.getProductId());
        assertEquals(2, result.getQuantity());
        
        verify(cartRepository, times(1)).findByUserIdAndProductId("user123", "product123");
        verify(cartRepository, times(1)).save(any(CartItem.class));
    }

    /**
     * TEST 2 : AJOUTER PRODUIT AU PANIER - Produit existant (mise à jour quantité)
     */
    @Test
    void testAddToCart_ExistingProduct() {
        // Arrange
        when(cartRepository.findByUserIdAndProductId(anyString(), anyString())).thenReturn(Optional.of(mockCartItem));
        when(cartRepository.save(any(CartItem.class))).thenReturn(mockCartItem);

        // Act
        CartItem result = cartService.addToCart(validRequest);

        // Assert
        assertNotNull(result);
        verify(cartRepository, times(1)).findByUserIdAndProductId("user123", "product123");
        verify(cartRepository, times(1)).save(any(CartItem.class));
    }

    /**
     * TEST 3 : RÉCUPÉRER PANIER - Succès
     */
    @Test
    void testGetCart_Success() {
        // Arrange
        CartItem item1 = new CartItem();
        item1.setProductId("product1");
        item1.setProductPrice(50.0);
        item1.setQuantity(2);
        
        CartItem item2 = new CartItem();
        item2.setProductId("product2");
        item2.setProductPrice(30.0);
        item2.setQuantity(1);
        
        when(cartRepository.findByUserIdOrderByAddedAtDesc(anyString()))
            .thenReturn(Arrays.asList(item1, item2));

        // Act
        CartSummary result = cartService.getCart("user123");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals(3, result.getTotalItems());
        assertEquals(130.0, result.getTotalAmount(), 0.01); // (50*2) + (30*1) = 130
        
        verify(cartRepository, times(1)).findByUserIdOrderByAddedAtDesc("user123");
    }

    /**
     * TEST 4 : RÉCUPÉRER PANIER - Panier vide
     */
    @Test
    void testGetCart_Empty() {
        // Arrange
        when(cartRepository.findByUserIdOrderByAddedAtDesc(anyString()))
            .thenReturn(Arrays.asList());

        // Act
        CartSummary result = cartService.getCart("user123");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getTotalItems());
        assertEquals(0.0, result.getTotalAmount(), 0.01);
        
        verify(cartRepository, times(1)).findByUserIdOrderByAddedAtDesc("user123");
    }

    /**
     * TEST 5 : METTRE À JOUR QUANTITÉ - Succès
     */
    @Test
    void testUpdateQuantity_Success() {
        // Arrange
        when(cartRepository.findById(anyString())).thenReturn(Optional.of(mockCartItem));
        when(cartRepository.save(any(CartItem.class))).thenReturn(mockCartItem);

        // Act
        CartItem result = cartService.updateQuantity("cart123", 5);

        // Assert
        assertNotNull(result);
        verify(cartRepository, times(1)).findById("cart123");
        verify(cartRepository, times(1)).save(any(CartItem.class));
    }

    /**
     * TEST 6 : METTRE À JOUR QUANTITÉ - Item not found
     */
    @Test
    void testUpdateQuantity_NotFound() {
        // Arrange
        when(cartRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cartService.updateQuantity("invalid-id", 5);
        });

        verify(cartRepository, times(1)).findById("invalid-id");
        verify(cartRepository, never()).save(any(CartItem.class));
    }

    /**
     * TEST 7 : SUPPRIMER ITEM - Succès
     */
    @Test
    void testRemoveItem_Success() {
        // Arrange
        when(cartRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(cartRepository).deleteById(anyString());

        // Act
        cartService.removeItem("cart123");

        // Assert
        verify(cartRepository, times(1)).existsById("cart123");
        verify(cartRepository, times(1)).deleteById("cart123");
    }

    /**
     * TEST 8 : VIDER PANIER - Succès
     */
    @Test
    void testClearCart_Success() {
        // Arrange
        doNothing().when(cartRepository).deleteByUserId(anyString());

        // Act
        cartService.clearCart("user123");

        // Assert
        verify(cartRepository, times(1)).deleteByUserId("user123");
    }

    /**
     * TEST 9 : GET CART ITEMS - Succès
     */
    @Test
    void testGetCartItems_Success() {
        // Arrange
        List<CartItem> items = Arrays.asList(mockCartItem);
        when(cartRepository.findByUserIdOrderByAddedAtDesc(anyString())).thenReturn(items);

        // Act
        List<CartItem> result = cartService.getCartItems("user123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("product123", result.get(0).getProductId());
        
        verify(cartRepository, times(1)).findByUserIdOrderByAddedAtDesc("user123");
    }
}

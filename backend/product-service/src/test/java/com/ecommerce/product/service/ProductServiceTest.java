package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * TESTS UNITAIRES - PRODUCT SERVICE
 * 
 * Tests critiques :
 * 1. Récupération tous produits
 * 2. Lecture produit par ID
 * 3. Recherche produit
 * 4. Décrément stock
 * 
 * Note : Tests de création/mise à jour/suppression désactivés car nécessitent Kafka
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private ProductService productService;

    private ProductRequest validProductRequest;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        // Injecter la valeur du topic Kafka via reflection
        ReflectionTestUtils.setField(productService, "productEventsTopic", "product-events");
        
        // Préparer données de test
        validProductRequest = new ProductRequest();
        validProductRequest.setName("Test Product");
        validProductRequest.setDescription("Test Description");
        validProductRequest.setPrice(99.99);
        validProductRequest.setCategory("Electronics");
        validProductRequest.setStock(10);

        mockProduct = new Product();
        mockProduct.setId("product123");
        mockProduct.setName("Test Product");
        mockProduct.setDescription("Test Description");
        mockProduct.setPrice(99.99);
        mockProduct.setCategory("Electronics");
        mockProduct.setStock(10);
        mockProduct.setSellerId("seller123");
        mockProduct.setSellerName("Test Seller");
        mockProduct.setCreatedAt(LocalDateTime.now());
    }

    /**
     * TEST 1 : GET ALL PRODUCTS - Succès
     */
    @Test
    void testGetAllProducts_Success() {
        // Arrange
        Product product2 = new Product();
        product2.setId("product456");
        product2.setName("Product 2");
        
        when(productRepository.findAll()).thenReturn(Arrays.asList(mockProduct, product2));

        // Act
        List<ProductResponse> products = productService.getAllProducts();

        // Assert
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    /**
     * TEST 2 : GET PRODUCT BY ID - Succès
     */
    @Test
    void testGetProductById_Success() {
        // Arrange
        when(productRepository.findById(anyString())).thenReturn(Optional.of(mockProduct));

        // Act
        Optional<ProductResponse> response = productService.getProductById("product123");

        // Assert
        assertTrue(response.isPresent());
        assertEquals("product123", response.get().getId());
        assertEquals("Test Product", response.get().getName());
        
        verify(productRepository, times(1)).findById("product123");
    }

    /**
     * TEST 3 : GET PRODUCT BY ID - Not Found
     */
    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<ProductResponse> response = productService.getProductById("invalid-id");

        // Assert
        assertFalse(response.isPresent());
        
        verify(productRepository, times(1)).findById("invalid-id");
    }

    /**
     * TEST 4 : SEARCH PRODUCTS - Succès
     */
    @Test
    void testSearchProducts_Success() {
        // Arrange
        when(productRepository.findByNameContainingIgnoreCase(anyString()))
            .thenReturn(Arrays.asList(mockProduct));

        // Act
        List<ProductResponse> products = productService.searchProducts("Test");

        // Assert
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }

    /**
     * TEST 6 : DECREASE STOCK - Succès
     */
    @Test
    void testDecreaseStock_Success() {
        // Arrange
        when(productRepository.findById(anyString())).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        // Act
        productService.decreaseStock("product123", 3);

        // Assert
        verify(productRepository, times(1)).findById("product123");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * TEST 7 : DECREASE STOCK - Stock insuffisant
     */
    @Test
    void testDecreaseStock_InsufficientStock() {
        // Arrange
        when(productRepository.findById(anyString())).thenReturn(Optional.of(mockProduct));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productService.decreaseStock("product123", 20);  // Plus que le stock disponible (10)
        });

        verify(productRepository, times(1)).findById("product123");
        verify(productRepository, never()).save(any(Product.class));
    }
}

package com.ecommerce.order.service;

import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.model.*;
import com.ecommerce.order.repository.OrderRepository;
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
 * TESTS UNITAIRES - ORDER SERVICE
 * 
 * Tests critiques :
 * 1. Créer commande
 * 2. Récupérer commande par ID
 * 3. Calcul montant total
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest validOrderRequest;
    private Order mockOrder;
    private OrderItem mockOrderItem;

    @BeforeEach
    void setUp() {
        // Préparer OrderItem
        mockOrderItem = new OrderItem();
        mockOrderItem.setProductId("product123");
        mockOrderItem.setProductName("Test Product");
        mockOrderItem.setProductImage("image.jpg");
        mockOrderItem.setUnitPrice(99.99);
        mockOrderItem.setQuantity(2);
        mockOrderItem.setSellerId("seller123");
        mockOrderItem.setSellerName("Test Seller");
        mockOrderItem.calculateSubtotal();

        // Préparer CreateOrderRequest
        validOrderRequest = new CreateOrderRequest();
        validOrderRequest.setUserId("user123");
        validOrderRequest.setUserName("Test User");
        validOrderRequest.setUserEmail("test@example.com");
        validOrderRequest.setItems(Arrays.asList(mockOrderItem));
        validOrderRequest.setShippingCost(10.0);
        validOrderRequest.setTax(5.0);
        validOrderRequest.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);
        
        ShippingAddress address = new ShippingAddress();
        address.setFullName("Test User");
        address.setPhone("0612345678");
        address.setAddress("123 Test Street");
        address.setCity("Paris");
        address.setPostalCode("75001");
        address.setCountry("France");
        validOrderRequest.setShippingAddress(address);
        validOrderRequest.setNotes("Test notes");

        // Préparer Order
        mockOrder = new Order();
        mockOrder.setId("order123");
        mockOrder.setOrderNumber("ORD-123456");
        mockOrder.setUserId("user123");
        mockOrder.setUserName("Test User");
        mockOrder.setUserEmail("test@example.com");
        mockOrder.setItems(Arrays.asList(mockOrderItem));
        mockOrder.setShippingCost(10.0);
        mockOrder.setTax(5.0);
        mockOrder.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);
        
        ShippingAddress mockAddress = new ShippingAddress();
        mockAddress.setFullName("Test User");
        mockAddress.setAddress("123 Test Street");
        mockOrder.setShippingAddress(mockAddress);
        mockOrder.setStatus(OrderStatus.PENDING);
        mockOrder.setPaymentStatus(PaymentStatus.PENDING);
        mockOrder.setCreatedAt(LocalDateTime.now());
        mockOrder.calculateTotalAmount();
    }

    /**
     * TEST 1 : CRÉER COMMANDE - Succès
     */
    @Test
    void testCreateOrder_Success() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Act
        Order result = orderService.createOrder(validOrderRequest);

        // Assert
        assertNotNull(result);
        assertEquals("order123", result.getId());
        assertEquals("user123", result.getUserId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(PaymentStatus.PENDING, result.getPaymentStatus());
        assertEquals(1, result.getItems().size());
        assertTrue(result.getTotalAmount() > 0);
        
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    /**
     * TEST 2 : CRÉER COMMANDE - Calcul montant total
     */
    @Test
    void testCreateOrder_CalculateTotalAmount() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Act
        Order result = orderService.createOrder(validOrderRequest);

        // Assert
        assertNotNull(result);
        // Total = (99.99 * 2) + 10.0 (shipping) + 5.0 (tax) = 214.98
        assertEquals(214.98, result.getTotalAmount(), 0.01);
        
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    /**
     * TEST 3 : GET ORDER BY ID - Succès
     */
    @Test
    void testGetOrderById_Success() {
        // Arrange
        when(orderRepository.findById(anyString())).thenReturn(Optional.of(mockOrder));

        // Act
        Order result = orderService.getOrderById("order123");

        // Assert
        assertNotNull(result);
        assertEquals("order123", result.getId());
        assertEquals("user123", result.getUserId());
        
        verify(orderRepository, times(1)).findById("order123");
    }

    /**
     * TEST 4 : GET ORDER BY ID - Not Found
     */
    @Test
    void testGetOrderById_NotFound() {
        // Arrange
        when(orderRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.getOrderById("invalid-id");
        });

        verify(orderRepository, times(1)).findById("invalid-id");
    }

    /**
     * TEST 5 : ORDER ITEM - Calculate Subtotal
     */
    @TestUni
    void testOrderItem_CalculateSubtotal() {
        // Arrange
        OrderItem item = new OrderItem();
        item.setProductPrice(50.0);
        item.setQuantity(3);

        // Act
        item.calculateSubtotal();

        // Assert
        assertEquals(150.0, item.getSubtotal(), 0.01);
    }
}

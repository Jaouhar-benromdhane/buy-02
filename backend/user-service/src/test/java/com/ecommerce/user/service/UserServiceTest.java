package com.ecommerce.user.service;

import com.ecommerce.user.dto.AuthResponse;
import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.dto.RegisterRequest;
import com.ecommerce.user.model.User;
import com.ecommerce.user.model.Role;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * TESTS UNITAIRES - USER SERVICE
 * 
 * Tests critiques :
 * 1. Inscription utilisateur (register)
 * 2. Connexion utilisateur (login)
 * 3. Validation email unique
 * 4. Hash du mot de passe
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Préparer données de test
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("Password123!");
        validRegisterRequest.setName("Test User");
        validRegisterRequest.setRole(Role.CLIENT);

        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@example.com");
        validLoginRequest.setPassword("Password123!");

        mockUser = new User();
        mockUser.setId("user123");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("$2a$10$hashedPassword");
        mockUser.setName("Test User");
        mockUser.setRole(Role.CLIENT);
    }

    /**
     * TEST 1 : INSCRIPTION - Succès
     */
    @Test
    void testRegister_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        String response = userService.register(validRegisterRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Utilisateur créé avec succès", response);
        
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("Password123!");
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * TEST 2 : INSCRIPTION - Email déjà existant
     */
    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.register(validRegisterRequest);
        });

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * TEST 3 : CONNEXION - Succès
     */
    @Test
    void testLogin_Success() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyString(), anyString()))
            .thenReturn("jwt-token-456");

        // Act
        AuthResponse response = userService.login(validLoginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-456", response.getToken());
        assertEquals("user123", response.getUserId());
        assertEquals("Test User", response.getName());
        assertEquals("CLIENT", response.getRole());
        
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("Password123!", "$2a$10$hashedPassword");
    }

    /**
     * TEST 4 : CONNEXION - Email invalide
     */
    @Test
    void testLogin_InvalidEmail() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.login(validLoginRequest);
        });

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    /**
     * TEST 5 : CONNEXION - Mot de passe invalide
     */
    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.login(validLoginRequest);
        });

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("Password123!", "$2a$10$hashedPassword");
    }

    /**
     * TEST 6 : Vérifier hash du password
     */
    @Test
    void testPasswordIsHashed() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashed");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        userService.register(validRegisterRequest);

        // Assert - Vérifier que le password est hashé
        verify(passwordEncoder, times(1)).encode("Password123!");
    }
}

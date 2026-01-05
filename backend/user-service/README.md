# 👤 User Service

Service de gestion des utilisateurs et du panier pour la plateforme e-commerce.

## 📋 Description

Microservice responsable de :
- Authentification (inscription / connexion)
- Gestion des utilisateurs (CLIENT / SELLER)
- Gestion du panier d'achat
- Sécurité JWT

## 🚀 Démarrage Rapide

### Windows

```cmd
# Compiler
build.bat

# Tester
test.bat

# Démarrer
run.bat
```

### Linux

```bash
# Compiler
chmod +x build.sh && ./build.sh

# Tester
chmod +x test.sh && ./test.sh

# Démarrer
chmod +x run.sh && ./run.sh
```

## 🔧 Configuration

**Port :** 8081  
**Base de données :** MongoDB (port 27017)  
**Collections :** `users`, `cart_items`

### Variables d'environnement (application.yml)

```yaml
server:
  port: 8081

spring:
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/ecommerce?authSource=admin

jwt:
  secret: your-secret-key
  expiration: 86400000  # 24 heures
```

## 📡 API Endpoints

### Authentification

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Inscription |
| POST | `/api/auth/login` | Connexion |

### Utilisateurs

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/users/me` | Profil utilisateur |
| GET | `/api/users/{id}` | Récupère un utilisateur |

### Panier

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/cart/add` | Ajouter au panier |
| GET | `/api/cart/{userId}` | Récupérer le panier |
| PUT | `/api/cart/{itemId}` | Modifier quantité |
| DELETE | `/api/cart/{itemId}` | Supprimer article |
| DELETE | `/api/cart/clear/{userId}` | Vider le panier |

## 🧪 Tests

**Tests unitaires :** 15 tests (6 UserService + 9 CartService)

```bash
# Windows
test.bat

# Linux
./test.sh
```

### Tests implémentés

#### UserService (6 tests)
- ✅ `testRegisterUser_Success()` - Inscription réussie
- ✅ `testRegisterUser_EmailExists()` - Email déjà utilisé
- ✅ `testLoginUser_Success()` - Connexion réussie
- ✅ `testLoginUser_InvalidEmail()` - Email invalide
- ✅ `testLoginUser_InvalidPassword()` - Mot de passe invalide
- ✅ `testPasswordHashing()` - Vérification hash BCrypt

#### CartService (9 tests)
- ✅ `testAddToCart_NewProduct()` - Ajouter nouveau produit
- ✅ `testAddToCart_ExistingProduct()` - Produit déjà dans le panier
- ✅ `testGetCart()` - Récupérer le panier
- ✅ `testGetCart_Empty()` - Panier vide
- ✅ `testUpdateQuantity_Success()` - Modifier quantité
- ✅ `testUpdateQuantity_NotFound()` - Item non trouvé
- ✅ `testRemoveFromCart()` - Supprimer un article
- ✅ `testClearCart()` - Vider le panier
- ✅ `testGetCartItems()` - Liste des items

## 📦 Modèles de données

### User

```java
public class User {
    private String id;
    private String fullName;
    private String email;
    private String password;  // Hashé avec BCrypt
    private String role;  // CLIENT ou SELLER
    private String avatar;
    private LocalDateTime createdAt;
}
```

### CartItem

```java
public class CartItem {
    private String id;
    private String userId;
    private String productId;
    private String productName;
    private Double productPrice;
    private String productImage;
    private Integer quantity;
    private String sellerId;
    private String sellerName;
    private LocalDateTime addedAt;
}
```

## 🔐 Sécurité

- **JWT Token** : Authentification stateless
- **BCrypt** : Hash des mots de passe
- **CORS** : Configuré pour localhost:4200

## 🔗 Dépendances

- Spring Boot 3.2.0
- Spring Security
- Spring Data MongoDB
- JWT (io.jsonwebtoken)
- BCrypt
- Lombok

## 📝 Logs

Les logs sont affichés dans la console avec le format :
```
INFO com.ecommerce.user.service.UserService - [message]
```

## 🐛 Troubleshooting

### JWT Token invalide

**Solution :** Vérifier que la clé secrète dans `application.yml` correspond

### Erreur "Email already exists"

**Solution :** Utiliser un email différent pour l'inscription

## 👥 Auteur

E-Commerce Platform Team

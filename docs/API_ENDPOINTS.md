# 🔌 API ENDPOINTS - buy-02

## 📋 Table des matières
1. [Cart API (User Service - Port 8081)](#cart-api)
2. [Order API (Order Service - Port 8084)](#order-api)

---

## 🛒 CART API

**Base URL:** `https://localhost:8081/api/cart`

### 1. Ajouter un produit au panier

```bash
POST /api/cart
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
  "userId": "67567abc123def456789",
  "productId": "prod123",
  "productName": "iPhone 15 Pro",
  "productImage": "https://localhost:8083/api/media/image123.jpg",
  "productPrice": 1299.99,
  "quantity": 1,
  "sellerId": "seller123",
  "sellerName": "Tech Store"
}
```

**Response:** `201 Created`
```json
{
  "id": "cart001",
  "userId": "67567abc123def456789",
  "productId": "prod123",
  "productName": "iPhone 15 Pro",
  "productImage": "https://localhost:8083/api/media/image123.jpg",
  "productPrice": 1299.99,
  "quantity": 1,
  "sellerId": "seller123",
  "sellerName": "Tech Store",
  "addedAt": "2025-12-09T10:00:00",
  "updatedAt": "2025-12-09T10:00:00"
}
```

---

### 2. Récupérer le panier complet (avec résumé)

```bash
GET /api/cart/{userId}
Authorization: Bearer <JWT_TOKEN>
```

**Response:** `200 OK`
```json
{
  "items": [
    {
      "id": "cart001",
      "userId": "67567abc123def456789",
      "productId": "prod123",
      "productName": "iPhone 15 Pro",
      "productImage": "https://localhost:8083/api/media/image123.jpg",
      "productPrice": 1299.99,
      "quantity": 2,
      "sellerId": "seller123",
      "sellerName": "Tech Store",
      "addedAt": "2025-12-09T10:00:00",
      "updatedAt": "2025-12-09T10:30:00"
    }
  ],
  "totalItems": 2,
  "totalAmount": 2599.98
}
```

---

### 3. Récupérer uniquement les items du panier

```bash
GET /api/cart/{userId}/items
Authorization: Bearer <JWT_TOKEN>
```

---

### 4. Mettre à jour la quantité

```bash
PUT /api/cart/item/{itemId}
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
  "quantity": 3
}
```

---

### 5. Supprimer un item du panier

```bash
DELETE /api/cart/item/{itemId}
Authorization: Bearer <JWT_TOKEN>
```

---

### 6. Supprimer par produit

```bash
DELETE /api/cart/{userId}/product/{productId}
Authorization: Bearer <JWT_TOKEN>
```

---

### 7. Vider le panier

```bash
DELETE /api/cart/{userId}
Authorization: Bearer <JWT_TOKEN>
```

---

### 8. Compter les items

```bash
GET /api/cart/{userId}/count
Authorization: Bearer <JWT_TOKEN>
```

**Response:** `200 OK`
```json
5
```

---

### 9. Vérifier si un produit est dans le panier

```bash
GET /api/cart/{userId}/has/{productId}
Authorization: Bearer <JWT_TOKEN>
```

**Response:** `200 OK`
```json
true
```

---

## 📦 ORDER API

**Base URL:** `https://localhost:8084/api/orders`

### 1. Créer une nouvelle commande

```bash
POST /api/orders
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
  "userId": "67567abc123def456789",
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "items": [
    {
      "productId": "prod123",
      "productName": "iPhone 15 Pro",
      "productImage": "https://localhost:8083/api/media/image123.jpg",
      "quantity": 1,
      "unitPrice": 1299.99,
      "sellerId": "seller123",
      "sellerName": "Tech Store"
    }
  ],
  "paymentMethod": "CASH_ON_DELIVERY",
  "shippingAddress": {
    "fullName": "John Doe",
    "phone": "+33612345678",
    "address": "123 Rue de la Paix",
    "city": "Paris",
    "postalCode": "75001",
    "country": "France"
  },
  "shippingCost": 0.0,
  "tax": 0.0,
  "notes": "Livrer après 18h"
}
```

**Response:** `201 Created`
```json
{
  "id": "order001",
  "orderNumber": "ORD-2025-001234",
  "userId": "67567abc123def456789",
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "items": [...],
  "subtotal": 1299.99,
  "shippingCost": 0.0,
  "tax": 0.0,
  "totalAmount": 1299.99,
  "status": "PENDING",
  "paymentMethod": "CASH_ON_DELIVERY",
  "paymentStatus": "PENDING",
  "shippingAddress": {...},
  "createdAt": "2025-12-09T10:00:00",
  "updatedAt": "2025-12-09T10:00:00"
}
```

---

### 2. Récupérer les commandes d'un utilisateur

```bash
GET /api/orders/user/{userId}
Authorization: Bearer <JWT_TOKEN>
```

---

### 3. Récupérer les commandes d'un vendeur

```bash
GET /api/orders/seller/{sellerId}
Authorization: Bearer <JWT_TOKEN>
```

---

### 4. Récupérer une commande par ID

```bash
GET /api/orders/{orderId}
Authorization: Bearer <JWT_TOKEN>
```

---

### 5. Récupérer une commande par numéro

```bash
GET /api/orders/number/{orderNumber}
Authorization: Bearer <JWT_TOKEN>
```

---

### 6. Mettre à jour le statut d'une commande

```bash
PUT /api/orders/{orderId}/status
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
  "status": "CONFIRMED"
}
```

**Status possibles:**
- `PENDING` - En attente
- `CONFIRMED` - Confirmée
- `SHIPPED` - Expédiée
- `DELIVERED` - Livrée
- `CANCELLED` - Annulée

---

### 7. Annuler une commande

```bash
POST /api/orders/{orderId}/cancel
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>

{
  "reason": "Client a changé d'avis"
}
```

---

### 8. Rechercher des commandes par statut

```bash
GET /api/orders/search?status=PENDING
Authorization: Bearer <JWT_TOKEN>
```

---

## 🔐 Authentication

Tous les endpoints nécessitent un JWT token dans le header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Pour obtenir un token, utilisez l'endpoint de login:

```bash
POST https://localhost:8081/api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

---

## 🧪 Tests avec cURL

### Exemple complet : Ajouter au panier

```bash
curl -k -X POST https://localhost:8081/api/cart \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "userId": "67567abc123def456789",
    "productId": "prod123",
    "productName": "iPhone 15 Pro",
    "productImage": "https://localhost:8083/api/media/image123.jpg",
    "productPrice": 1299.99,
    "quantity": 1,
    "sellerId": "seller123",
    "sellerName": "Tech Store"
  }'
```

---

**Date:** 09/12/2025  
**Version:** 1.0  
**Status:** ✅ BACKEND COMPLET

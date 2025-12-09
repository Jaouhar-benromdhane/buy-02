# 📊 DATABASE DESIGN - buy-02

## Architecture MongoDB Collections

### Collections Existantes (buy-01)
✅ **users** - Gestion des utilisateurs (clients et vendeurs)
✅ **products** - Catalogue de produits
✅ **media** - Gestion des fichiers uploadés

### Nouvelles Collections (buy-02)

---

## 🛒 1. Collection: **cart_items**

**Database:** `ecommerce_users`

Stocke les articles ajoutés au panier par chaque utilisateur.

```javascript
{
  "_id": ObjectId("..."),
  "userId": "user123",                    // Référence vers User
  "productId": "prod456",                 // Référence vers Product
  "productName": "iPhone 15 Pro",
  "productPrice": 1299.99,
  "productImage": "https://...",
  "quantity": 2,
  "sellerId": "seller789",
  "sellerName": "Tech Store",
  "addedAt": ISODate("2025-12-09T10:00:00Z"),
  "updatedAt": ISODate("2025-12-09T10:30:00Z")
}
```

**Indexes:**
- `userId` (compound index avec productId pour éviter les doublons)
- `productId`

---

## 📦 2. Collection: **orders**

**Database:** `ecommerce_orders` (nouvelle database)

Gère toutes les commandes de la plateforme.

```javascript
{
  "_id": ObjectId("..."),
  "orderNumber": "ORD-2025-001234",       // Numéro unique de commande
  
  // Client
  "userId": "user123",
  "userName": "John Doe",
  "userEmail": "john@example.com",
  
  // Items commandés
  "items": [
    {
      "productId": "prod456",
      "productName": "iPhone 15 Pro",
      "productImage": "https://...",
      "quantity": 1,
      "unitPrice": 1299.99,
      "subtotal": 1299.99,
      "sellerId": "seller789",
      "sellerName": "Tech Store"
    },
    {
      "productId": "prod789",
      "productName": "AirPods Pro",
      "productImage": "https://...",
      "quantity": 2,
      "unitPrice": 249.99,
      "subtotal": 499.98,
      "sellerId": "seller789",
      "sellerName": "Tech Store"
    }
  ],
  
  // Montants
  "subtotal": 1799.97,
  "shippingCost": 0.00,
  "tax": 0.00,
  "totalAmount": 1799.97,
  
  // Statut de la commande
  "status": "PENDING",  // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
  
  // Paiement
  "paymentMethod": "CASH_ON_DELIVERY",    // PAY_ON_DELIVERY, CARD, PAYPAL
  "paymentStatus": "PENDING",             // PENDING, PAID, FAILED
  
  // Adresse de livraison
  "shippingAddress": {
    "fullName": "John Doe",
    "phone": "+33612345678",
    "address": "123 Rue de la Paix",
    "city": "Paris",
    "postalCode": "75001",
    "country": "France"
  },
  
  // Dates
  "createdAt": ISODate("2025-12-09T10:00:00Z"),
  "updatedAt": ISODate("2025-12-09T10:00:00Z"),
  "confirmedAt": null,
  "shippedAt": null,
  "deliveredAt": null,
  "cancelledAt": null,
  
  // Notes
  "notes": "Livrer après 18h",
  "cancellationReason": null
}
```

**Indexes:**
- `orderNumber` (unique)
- `userId`
- `status`
- `createdAt` (descending)
- Compound: `userId` + `status`
- Compound: items.sellerId (pour les vendeurs)

---

## 👤 3. Enrichissement Collection: **users**

Ajout de champs pour les statistiques utilisateur/vendeur.

**Nouveaux champs:**

```javascript
{
  // ... champs existants ...
  
  // Statistiques Client
  "stats": {
    "totalOrders": 15,
    "totalSpent": 5420.50,
    "favoriteCategories": ["Electronics", "Fashion"],
    "lastOrderDate": ISODate("2025-12-08T15:30:00Z")
  },
  
  // Statistiques Vendeur (si role = SELLER)
  "sellerStats": {
    "totalSales": 25000.00,
    "totalOrders": 87,
    "productsSold": 142,
    "topProducts": [
      {
        "productId": "prod123",
        "productName": "iPhone 15",
        "unitsSold": 25
      }
    ],
    "rating": 4.7,
    "totalReviews": 45
  }
}
```

---

## 🎁 4. Collection: **wishlists** (BONUS)

**Database:** `ecommerce_users`

Liste de souhaits pour chaque utilisateur.

```javascript
{
  "_id": ObjectId("..."),
  "userId": "user123",
  "productId": "prod456",
  "productName": "iPhone 15 Pro",
  "productPrice": 1299.99,
  "productImage": "https://...",
  "addedAt": ISODate("2025-12-09T10:00:00Z")
}
```

**Indexes:**
- Compound: `userId` + `productId` (unique)

---

## 📈 5. Collection: **order_stats** (Pré-agrégation)

**Database:** `ecommerce_orders`

Statistiques pré-calculées pour performance.

```javascript
{
  "_id": "user123",                       // userId
  "type": "USER",                         // USER ou SELLER
  "totalOrders": 15,
  "totalSpent": 5420.50,                  // Pour USER
  "totalRevenue": 25000.00,               // Pour SELLER
  "mostBoughtProducts": [
    {
      "productId": "prod123",
      "count": 5,
      "totalSpent": 500.00
    }
  ],
  "lastUpdated": ISODate("2025-12-09T12:00:00Z")
}
```

---

## 🔍 Indexes pour Search & Filtering

**Collection products:**
```javascript
// Text index pour recherche full-text
db.products.createIndex({ 
  name: "text", 
  description: "text", 
  category: "text" 
})

// Indexes composés pour filtres
db.products.createIndex({ category: 1, price: 1 })
db.products.createIndex({ sellerId: 1, createdAt: -1 })
db.products.createIndex({ stock: 1 })
```

---

## 🗄️ Databases MongoDB

```
ecommerce_users     → users, cart_items, wishlists
ecommerce_products  → products
ecommerce_media     → media (existing)
ecommerce_orders    → orders, order_stats
```

---

## 🔗 Relations

```
User (1) ----< (N) CartItem
User (1) ----< (N) Order
User (1) ----< (N) Wishlist
Product (1) ----< (N) CartItem
Product (1) ----< (N) Order.items
Seller (1) ----< (N) Order.items
```

---

## ⚡ Stratégie de Performance

1. **Dénormalisation** : Dupliquer productName, price dans cart/orders pour éviter les JOINs
2. **Indexes** : Créer tous les indexes listés ci-dessus
3. **Pré-agrégation** : order_stats pour statistiques temps réel
4. **TTL Index** : Supprimer automatiquement les paniers abandonnés après 30 jours

---

## 🔐 Validation Schema (optionnel)

MongoDB peut valider les schémas avec JSON Schema pour garantir l'intégrité des données.

---

**Date:** 09/12/2025  
**Version:** 1.0  
**Status:** ✅ VALIDÉ

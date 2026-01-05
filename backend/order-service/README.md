# 📦 Order Service

Microservice de gestion des commandes pour la plateforme e-commerce.

## 🎯 Fonctionnalités

- ✅ Création de commandes à partir du panier
- ✅ Suivi du cycle de vie des commandes
- ✅ Gestion des statuts (PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- ✅ Recherche et filtrage des commandes
- ✅ Vue utilisateur (mes commandes)
- ✅ Vue vendeur (commandes de mes produits)
- ✅ Paiement à la livraison (CASH_ON_DELIVERY)
- ✅ Annulation de commandes
- ✅ Historique complet

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

## 🏗️ Architecture

- **Port**: 8084
- **Base de données**: MongoDB (`ecommerce`)
- **Collection**: `orders`
- **Communication**: Kafka (événements inter-services)
- **Framework**: Spring Boot 3.2.0
- **Java**: 17

## 🧪 Tests

**Tests unitaires :** 5 tests

```bash
# Windows
test.bat

# Linux
./test.sh
```

### Tests implémentés

- ✅ `testCreateOrder_Success()` - Création commande réussie
- ✅ `testCreateOrder_WithItems()` - Commande avec articles
- ✅ `testCalculateTotalAmount()` - Calcul montant total
- ✅ `testGetOrderById()` - Récupération par ID
- ✅ `testOrderItem_CalculateSubtotal()` - Calcul sous-total item

## 📊 Modèle de données

### Order
```json
{
  "orderNumber": "ORD-2025-001234",
  "userId": "user123",
  "userName": "John Doe",
  "items": [
    {
      "productId": "prod456",
      "productName": "iPhone 15",
      "quantity": 1,
      "unitPrice": 1299.99,
      "subtotal": 1299.99,
      "sellerId": "seller789"
    }
  ],
  "totalAmount": 1299.99,
  "status": "PENDING",
  "paymentMethod": "CASH_ON_DELIVERY",
  "shippingAddress": {...},
  "createdAt": "2025-12-09T10:00:00"
}
```

## 🔌 API Endpoints

### Commandes de base
- `POST /api/orders` - Créer une commande
- `GET /api/orders/{orderId}` - Détails d'une commande
- `GET /api/orders/number/{orderNumber}` - Recherche par numéro

### Gestion utilisateur
- `GET /api/orders/user/{userId}` - Toutes les commandes d'un user
- `GET /api/orders/user/{userId}/filter?status=PENDING` - Filtrer par statut

### Gestion vendeur
- `GET /api/orders/seller/{sellerId}` - Commandes des produits du vendeur
- `GET /api/orders/seller/{sellerId}/filter?status=CONFIRMED` - Filtrer

### Actions
- `PUT /api/orders/{orderId}/status` - Mettre à jour le statut
- `POST /api/orders/{orderId}/cancel` - Annuler une commande
- `DELETE /api/orders/{orderId}` - Supprimer (uniquement si annulée)

### Recherche
- `GET /api/orders/search?query=ORD-2025` - Rechercher

## 🚀 Démarrage

### Compilation
```bash
mvn clean package -DskipTests
```

### Lancement
```bash
java -jar target/order-service-1.0.0.jar
```

Le service sera disponible sur `https://localhost:8084`

## 📝 Statuts des commandes

| Statut | Description |
|--------|-------------|
| `PENDING` | En attente de confirmation |
| `CONFIRMED` | Confirmée par le vendeur |
| `PROCESSING` | En cours de préparation |
| `SHIPPED` | Expédiée |
| `DELIVERED` | Livrée (paiement effectué) |
| `CANCELLED` | Annulée |

## 🔧 Configuration

Voir `application.yml` pour la configuration MongoDB et Kafka.

## 📦 Dépendances

- Spring Boot Web
- Spring Data MongoDB
- Spring Kafka
- Lombok
- Validation

## 👥 Intégration

Ce service communique avec :
- **User Service** (informations utilisateur)
- **Product Service** (détails produits)
- **Cart Service** (transformation panier → commande)

## 🧪 Tests

```bash
mvn test
```

---

**Version**: 1.0.0  
**Date**: 09/12/2025

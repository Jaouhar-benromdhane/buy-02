# 🛒 Product Service

Service de gestion des produits pour la plateforme e-commerce.

## 📋 Description

Microservice responsable de la gestion complète des produits :
- CRUD produits
- Gestion du stock
- Recherche de produits
- Filtrage par vendeur

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

**Port :** 8082  
**Base de données :** MongoDB (port 27017)  
**Collection :** `products`

### Variables d'environnement (application.yml)

```yaml
server:
  port: 8082

spring:
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/ecommerce?authSource=admin
```

## 📡 API Endpoints

### Produits

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/products` | Liste tous les produits |
| GET | `/api/products/{id}` | Récupère un produit |
| GET | `/api/products/search?name={name}` | Recherche par nom |
| GET | `/api/products/seller/{sellerId}` | Produits d'un vendeur |
| POST | `/api/products` | Créer un produit |
| PUT | `/api/products/{id}` | Modifier un produit |
| DELETE | `/api/products/{id}` | Supprimer un produit |
| PUT | `/api/products/{id}/stock` | Décrémenter le stock |

## 🧪 Tests

**Tests unitaires :** 6 tests

```bash
# Windows
test.bat

# Linux
./test.sh
```

### Tests implémentés

- ✅ `testGetAllProducts()` - Récupération de tous les produits
- ✅ `testGetProductById_Success()` - Récupération par ID (succès)
- ✅ `testGetProductById_NotFound()` - Récupération par ID (non trouvé)
- ✅ `testSearchProducts()` - Recherche de produits
- ✅ `testDecreaseStock_Success()` - Décrémentation du stock (succès)
- ✅ `testDecreaseStock_InsufficientStock()` - Stock insuffisant

## 📦 Modèle de données

```java
public class Product {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String sellerId;
    private String sellerName;
    private List<String> imageUrls;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

## 🔗 Dépendances

- Spring Boot 3.2.0
- Spring Data MongoDB
- Kafka (pour events)
- Lombok

## 📝 Logs

Les logs sont affichés dans la console avec le format :
```
INFO com.ecommerce.product.service.ProductService - [message]
```

## 🐛 Troubleshooting

### Erreur de connexion MongoDB

```
Error: MongoTimeoutException
```

**Solution :** Vérifier que MongoDB tourne :
```bash
docker ps | grep mongodb
```

### Port 8082 déjà utilisé

**Solution :** Modifier le port dans `application.yml`

## 👥 Auteur

E-Commerce Platform Team

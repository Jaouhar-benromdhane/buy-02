# 🛍️ E-Commerce Platform - Microservices Architecture

## 📌 Description du Projet

Plateforme e-commerce **complète et fonctionnelle** développée avec une architecture microservices utilisant **Spring Boot** pour le backend et **Angular** pour le frontend.

### 🎯 Objectif Principal
Créer une plateforme où :
- Les **clients** peuvent consulter, rechercher et acheter des produits
- Les **vendeurs** peuvent gérer leurs produits avec images multiples
- Communication entre services via **Kafka** (suppression en cascade)
- Sécurité renforcée avec authentification **JWT**
- Système de **panier d'achat** personnalisé par utilisateur

---

## ✨ Fonctionnalités Implémentées

### 🔐 **Authentification & Sécurité**
- ✅ Inscription avec choix de rôle (CLIENT / SELLER)
- ✅ Connexion sécurisée avec JWT
- ✅ Guards Angular (auth, seller, login)
- ✅ Redirection automatique selon le rôle
- ✅ Protection des routes frontend et backend
- ✅ Hash des mots de passe avec BCrypt
- ✅ **HTTPS/SSL activé** (certificats auto-signés pour développement)
- ✅ **Upload d'avatar** pour les vendeurs
- ✅ **Validation de stock** dans le panier et pages produits

### 👥 **Pour les Clients (CLIENT)**
- ✅ Liste des produits avec images
- ✅ Recherche de produits
- ✅ Page détail produit avec galerie d'images
- ✅ Sélecteur de quantité
- ✅ **Validation de stock** avant ajout au panier
- ✅ Ajout au panier avec notifications
- ✅ **Panier d'achat complet (MongoDB)** :
  - Badge avec compteur en temps réel
  - **Validation de stock** lors de l'augmentation de quantité
  - Gestion des quantités (augmenter/diminuer)
  - Suppression d'articles
  - Calcul du total
  - Panier persistant par utilisateur en base de données
- ✅ Navigation fluide entre les pages
- 🚧 **Système de commandes** (en cours) :
  - Checkout avec formulaire d'adresse
  - Paiement à la livraison (Pay on Delivery)
  - Historique des commandes
  - Suivi du statut des commandes

### 🏪 **Pour les Vendeurs (SELLER)**
- ✅ Dashboard de gestion des produits
- ✅ **Avatar de profil** (upload lors de l'inscription)
- ✅ Création de produits avec formulaire validé
- ✅ Upload d'images multiples (max 2MB par image)
- ✅ Modification de produits existants :
  - Affichage des images actuelles
  - Suppression d'images
  - Ajout de nouvelles images
- ✅ Suppression de produits (cascade avec Kafka)
- ✅ Tableau de bord avec :
  - Liste des produits en tableau
  - **Avatar du vendeur** dans la toolbar
  - **Nom réel du vendeur** sur les produits
  - Indicateurs de stock (normal/faible)
  - Actions rapides (éditer/supprimer)
  - Notifications de succès/erreur

### 🎨 **Interface Utilisateur**
- ✅ Design moderne avec **Angular Material**
- ✅ Responsive (mobile, tablette, desktop)
- ✅ Snackbar notifications pour feedback
- ✅ Loading spinners
- ✅ États vides avec call-to-action
- ✅ Galerie d'images avec navigation
- ✅ Cartes produits attrayantes

---

## 🏗️ Architecture du Projet

### Microservices Backend (Spring Boot)
```
├── user-service          # Gestion des utilisateurs + Panier d'achat
├── product-service       # Gestion des produits (CRUD)
├── media-service         # Gestion des images produits
├── order-service         # Gestion des commandes
├── api-gateway           # Point d'entrée unique (optionnel)
└── eureka-server         # Service discovery (optionnel)
```

### Frontend (Angular 20)
```
└── frontend
    ├── core/
    │   ├── guards/          # Auth, Seller, Login guards
    │   ├── models/          # TypeScript interfaces
    │   └── services/        # Auth, Product, Media, Cart services
    ├── features/
    │   ├── auth/            # Login, Register pages
    │   ├── products/        # Product list, Product detail
    │   ├── cart/            # Shopping cart page
    │   └── seller/          # Dashboard, Product form dialog
    └── styles/              # Global SCSS styles
```

---

## 📊 Modèle de Données

### User (Utilisateur)
```json
{
  "id": "String",
  "name": "String",
  "email": "String (unique)",
  "password": "String (hashé avec BCrypt)",
  "role": "Enum (CLIENT / SELLER)",
  "createdAt": "Date",
  "updatedAt": "Date"
}
```

### Product (Produit)
```json
{
  "id": "String",
  "name": "String",
  "description": "String",
  "price": "Double",
  "stock": "Int",
  "category": "String",
  "sellerId": "String (référence au vendeur)",
  "sellerName": "String",
  "createdAt": "Date",
  "updatedAt": "Date"
}
```

### Media (Image)
```json
{
  "id": "String",
  "url": "String (chemin local)",
  "productId": "String (référence au produit)",
  "uploadedAt": "Date"
}
```

### CartItem (Panier - MongoDB)
```json
{
  "id": "String",
  "userId": "String",
  "productId": "String",
  "productName": "String",
  "productImage": "String",
  "productPrice": "Double",
  "quantity": "Int",
  "sellerId": "String",
  "sellerName": "String",
  "addedAt": "Date",
  "updatedAt": "Date"
}
```

### Order (Commande)
```json
{
  "id": "String",
  "orderNumber": "String (ex: ORD-2025-001234)",
  "userId": "String",
  "userName": "String",
  "userEmail": "String",
  "items": [
    {
      "productId": "String",
      "productName": "String",
      "productImage": "String",
      "quantity": "Int",
      "unitPrice": "Double",
      "subtotal": "Double",
      "sellerId": "String",
      "sellerName": "String"
    }
  ],
  "subtotal": "Double",
  "shippingCost": "Double",
  "tax": "Double",
  "totalAmount": "Double",
  "status": "Enum (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)",
  "paymentMethod": "Enum (CASH_ON_DELIVERY, CARD, PAYPAL)",
  "paymentStatus": "Enum (PENDING, PAID, FAILED)",
  "shippingAddress": {
    "fullName": "String",
    "phone": "String",
    "address": "String",
    "city": "String",
    "postalCode": "String",
    "country": "String"
  },
  "createdAt": "Date",
  "updatedAt": "Date"
}
```

**Relations** :
- Un User (SELLER) peut avoir plusieurs Products (1 → n)
- Un Product peut avoir plusieurs Media (1 → n)
- Un User peut avoir plusieurs CartItems (1 → n)
- Un User peut avoir plusieurs Orders (1 → n)
- Suppression en cascade via Kafka : Product supprimé → Media supprimés automatiquement
- Panier persistant en base MongoDB (pas en localStorage)

---

## 🔧 Technologies Utilisées

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT)
- **Spring Data MongoDB**
- **Spring Kafka**
- **MongoDB 7.0** (base de données)
- **Apache Kafka 7.5.0** (message broker)
- **Maven** (gestion dépendances)
- **Lombok** (réduction boilerplate)
- **Jackson** (JSON parsing)

### Frontend
- **Angular 20.3.6**
- **Angular Material 20.2.11**
- **TypeScript 5.x**
- **RxJS** (programmation réactive)
- **SCSS** (styling)
- **Angular CLI**

### Infrastructure
- **Docker** (containerisation)
- **Docker Compose** (orchestration)
- **Git** (version control)

### Sécurité
- **JWT tokens** (authentification)
- **BCrypt** (hash passwords)
- **CORS configuration**
- **Guards Angular** (protection routes)
- **Validation des entrées**

---

## 🚀 Installation et Démarrage

### Prérequis
- **Java 17** ou supérieur
- **Node.js 18+** et npm
- **Docker** et **Docker Compose**
- **Maven 3.8+**
- **Git**

### 🎯 Démarrage Rapide (Automatique)

#### Windows (PowerShell)
```powershell
.\start-all.ps1
```

#### Linux / Mac
```bash
chmod +x start-all.sh
./start-all.sh
```

Ces scripts démarrent automatiquement :
- ✅ Docker Compose (MongoDB + Kafka + Zookeeper)
- ✅ User Service (port 8081)
- ✅ Product Service (port 8082)
- ✅ Media Service (port 8083)
- ✅ Frontend Angular (port 4200)

#### Arrêter tous les services

**Windows:**
```powershell
.\stop-all.ps1
```

**Linux / Mac:**
```bash
./stop-all.sh
```

---

### 📋 Installation Manuelle (Étape par étape)

#### 1️⃣ **Cloner le projet**
```bash
git clone https://zone01normandie.org/git/jbenromd/buy-01.git
cd buy-01
```

#### 2️⃣ **Démarrer l'infrastructure (MongoDB + Kafka)**
```bash
docker-compose up -d
```

Vérifier que les containers tournent :
```bash
docker ps
```

Vous devriez voir :
- `mongodb` sur le port 27017
- `zookeeper` sur le port 2181
- `kafka` sur le port 9092

#### 3️⃣ **Backend - Compiler et lancer les microservices**

**User Service** (Port 8081)
```bash
cd backend/user-service
mvn clean install
mvn spring-boot:run
```

**Product Service** (Port 8082)
```bash
cd backend/product-service
mvn clean install
mvn spring-boot:run
```

**Media Service** (Port 8083)
```bash
cd backend/media-service
mvn clean install
mvn spring-boot:run
```

#### 4️⃣ **Frontend - Angular**
```bash
cd frontend
npm install
npm start
```

Le serveur de développement démarre sur **http://localhost:4200**

---

## 🌐 Accès à l'Application

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | https://localhost:4200 | Application Angular |
| **User Service** | https://localhost:8081 | API Utilisateurs |
| **Product Service** | https://localhost:8082 | API Produits |
| **Media Service** | https://localhost:8083 | API Images |
| **MongoDB** | localhost:27017 | Base de données |
| **Kafka** | localhost:9092 | Message broker |

### ⚠️ Note importante sur HTTPS

Les services utilisent des **certificats SSL auto-signés** pour le développement. Lors du premier accès, votre navigateur affichera un avertissement de sécurité.

**Pour accepter les certificats :**
1. Ouvrez chaque URL backend dans votre navigateur :
   - https://localhost:8081/api/auth/health
   - https://localhost:8082/api/products
   - https://localhost:8083/api/media/health
2. Cliquez sur **"Avancé"** puis **"Continuer vers localhost"**
3. Rechargez le frontend : https://localhost:4200

**Alternative (pour développement seulement) :** Pour désactiver HTTPS, commentez les sections `ssl:` dans les fichiers `application.yml` des 3 services backend et changez les URLs de `https://` vers `http://` dans les services Angular.

---

## 👤 Comptes de Test

### Client
- **Email** : `bob@client.com`
- **Mot de passe** : `password123`
- **Rôle** : CLIENT

### Vendeur
- **Email** : `alice@seller.com`
- **Mot de passe** : `password123`
- **Rôle** : SELLER

---

## 🔐 Sécurité

### Mesures de Sécurité Implémentées
✅ **JWT Authentication** - Tokens sécurisés avec expiration 24h  
✅ **Hash des mots de passe** - BCrypt avec salt automatique  
✅ **Guards Angular** - Protection des routes (auth, seller, login)  
✅ **Role-based Access Control** - Séparation CLIENT/SELLER  
✅ **Validation des entrées** - Backend et frontend  
✅ **CORS configuré** - Sécurisation des requêtes cross-origin  
✅ **Limitation upload** - Max 2MB par image  
✅ **Validation fichiers** - Vérification type et taille  
✅ **HTTP Interceptor** - Injection automatique du JWT  
✅ **Protection données sensibles** - Mots de passe jamais exposés  

### Architecture de Sécurité
- **Frontend** : Guards empêchent l'accès non autorisé aux routes
- **Backend** : `@PreAuthorize` sur les endpoints sensibles
- **Communication** : JWT dans le header `Authorization: Bearer <token>`
- **Panier** : Isolé par utilisateur avec clé unique dans localStorage

---

## 📱 Guide d'Utilisation

### 🔹 **En tant que CLIENT**

1. **S'inscrire**
   - Aller sur http://localhost:4200/register
   - Remplir le formulaire avec rôle = CLIENT
   - Cliquer sur "S'inscrire"

2. **Se connecter**
   - Email : votre email
   - Mot de passe : votre mot de passe
   - Redirection automatique vers `/products`

3. **Consulter les produits**
   - Liste des produits avec images
   - Barre de recherche pour filtrer
   - Cliquer sur "Détails" pour voir le produit complet

4. **Page détail produit**
   - Galerie d'images avec navigation
   - Sélectionner la quantité
   - Cliquer sur "Ajouter au panier"

5. **Panier d'achat**
   - Cliquer sur l'icône panier (badge avec compteur)
   - Modifier les quantités (+/-)
   - Supprimer des articles
   - Voir le total
   - "Procéder au paiement" (à implémenter)

### 🔹 **En tant que SELLER**

1. **S'inscrire en tant que vendeur**
   - Rôle = SELLER lors de l'inscription

2. **Accéder au dashboard**
   - Connexion → Redirection automatique vers `/seller/dashboard`
   - Vue tableau de tous vos produits

3. **Créer un produit**
   - Cliquer sur "Ajouter un produit"
   - Remplir le formulaire :
     - Nom (min 3 caractères)
     - Description (min 10 caractères)
     - Prix (> 0.01 €)
     - Stock (entier ≥ 0)
     - Catégorie (dropdown)
   - Ajouter des images (optionnel, max 2MB)
   - Cliquer sur "Créer"

4. **Modifier un produit**
   - Cliquer sur l'icône ✏️ (edit)
   - Les images existantes s'affichent
   - Supprimer des images avec ❌
   - Ajouter de nouvelles images
   - Modifier les informations
   - Cliquer sur "Enregistrer"

5. **Supprimer un produit**
   - Cliquer sur l'icône 🗑️ (delete)
   - Confirmer la suppression
   - Toutes les images sont supprimées automatiquement (Kafka)

6. **Voir la boutique**
   - Cliquer sur "Voir la boutique" pour voir vos produits comme un client

---

## 🧪 Tests

### Backend
```bash
mvn test
```

### Frontend
```bash
ng test
```

### Tests à effectuer
- ✅ CRUD Users et Products
- ✅ Authentification par rôle
- ✅ Upload média (contraintes)
- ✅ Sécurité et validation
- ✅ Gestion d'erreurs

---

## 📚 Documentation API

### 🔵 **User Service** (Port 8081)

#### Authentification
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "CLIENT"
}
```

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGc...",
  "userId": "123",
  "email": "john@example.com",
  "name": "John Doe",
  "role": "CLIENT"
}
```

---

### 🟢 **Product Service** (Port 8082)

#### Endpoints publics
```http
GET /api/products
# Liste tous les produits

GET /api/products/{id}
# Détail d'un produit

GET /api/products/search?keyword=iPhone
# Recherche de produits

GET /api/products/category/{category}
# Produits par catégorie
```

#### Endpoints protégés (SELLER uniquement)
```http
POST /api/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "iPhone 15 Pro",
  "description": "Dernier iPhone avec puce A17",
  "price": 1299.99,
  "stock": 50,
  "category": "Smartphones"
}
```

```http
PUT /api/products/{id}
Authorization: Bearer <token>
# Modifier un produit (seulement le propriétaire)

DELETE /api/products/{id}
Authorization: Bearer <token>
# Supprimer un produit (déclenche suppression Kafka des images)

GET /api/products/seller/my-products
Authorization: Bearer <token>
# Récupérer les produits du vendeur connecté
```

---

### 🟡 **Media Service** (Port 8083)

```http
POST /api/media/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: [fichier image]
productId: "product123"

Contraintes:
- Max 2MB par image
- Formats: JPG, PNG, GIF
```

```http
GET /api/media/product/{productId}
# Récupérer toutes les images d'un produit

DELETE /api/media/{id}
Authorization: Bearer <token>
# Supprimer une image (seulement le propriétaire du produit)

GET /uploads/{filename}
# Accéder à l'image (URL retournée par upload)
```

---

## 🐳 Docker

### Services Docker Compose
```yaml
services:
  mongodb:
    image: mongo:7.0
    ports: 27017:27017
    volumes: mongodb_data
    
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      
  kafka:
    image: confluentinc/cp-kafka:7.5.0
    ports: 9092:9092
    depends_on: zookeeper
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
```

### Commandes utiles
```bash
# Démarrer tous les services
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Arrêter les services
docker-compose down

# Arrêter et supprimer les volumes
docker-compose down -v

# Redémarrer un service spécifique
docker-compose restart mongodb
```

---

## 🔄 Architecture Kafka

### Topic : `product-events`

**Producteur** : Product Service  
**Consommateur** : Media Service

**Cas d'usage** : Suppression en cascade
1. Un SELLER supprime un produit
2. Product Service publie un événement sur Kafka :
   ```json
   {
     "type": "PRODUCT_DELETED",
     "productId": "123"
   }
   ```
3. Media Service consomme l'événement
4. Media Service supprime toutes les images liées au produit
5. Les fichiers physiques sont supprimés du dossier `uploads/`

---

## 📸 Screenshots

### Page Login
![Login](docs/screenshots/login.png)

### Liste des Produits (CLIENT)
![Products](docs/screenshots/products.png)

### Page Détail Produit
![Detail](docs/screenshots/product-detail.png)

### Panier d'Achat
![Cart](docs/screenshots/cart.png)

### Dashboard Vendeur (SELLER)
![Dashboard](docs/screenshots/seller-dashboard.png)

### Formulaire Produit
![Form](docs/screenshots/product-form.png)

---

## 🧪 Tests & Validation

### Tests Manuels Essentiels
- ✅ Inscription CLIENT et SELLER
- ✅ Connexion avec rôles différents
- ✅ Protection des routes (guards)
- ✅ CRUD produits complet
- ✅ Upload images multiples
- ✅ Suppression en cascade (Kafka)
- ✅ Système de panier par utilisateur
- ✅ Recherche de produits

---

## 🚧 Prochaines Étapes

### À Implémenter (Ordre de priorité)
1. **Order Service** - Microservice de gestion des commandes
2. **Checkout Page** - Finalisation des achats
3. **Order History** - Historique pour CLIENT et SELLER
4. **Email Notifications** - Confirmation de commande
5. **Payment Integration** - Stripe/PayPal
6. **Product Reviews** - Avis et notes
7. **Admin Panel** - Interface d'administration

---

## 👥 Auteur

Développé par **jbenromd** - Zone01 Normandie

---

## 📄 Licence

Ce projet est à usage éducatif.

---

## 🔗 Ressources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Angular Documentation](https://angular.io/docs)
- [Apache Kafka](https://kafka.apache.org/)
- [MongoDB Manual](https://www.mongodb.com/docs/)

**Bonne découverte ! 🎉**


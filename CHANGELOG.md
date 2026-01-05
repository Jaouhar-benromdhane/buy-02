# 📝 Changelog

Tous les changements notables de ce projet seront documentés dans ce fichier.

Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.0.0/),
et ce projet adhère au [Semantic Versioning](https://semver.org/lang/fr/).

---

## [1.0.0] - 2026-01-05

### 🎉 Version Initiale Complète

#### ✨ Ajouté
- **Architecture microservices complète** (4 services backend + 1 frontend)
- **Sécurité SSL/HTTPS** activée sur tous les services backend
- **Certificats auto-signés** pour environnement de développement (keystore.p12)
- **Authentification JWT** avec guards Angular et intercepteurs
- **Upload de fichiers** (avatars vendeurs, images produits)
- **Système de panier** persistant en base MongoDB
- **Gestion des commandes** avec workflow complet (PENDING → CONFIRMED → SHIPPED → DELIVERED)
- **Communication Kafka** entre services (événements produits)
- **Scripts cross-platform** :
  - Windows: build.bat, run.bat, test.bat, start-all.ps1, stop-all.ps1
  - Linux: build.sh, run.sh, test.sh, start-all.sh, stop-all.sh
- **Documentation complète** :
  - README par service (User, Product, Media, Order)
  - GUIDE_DEMARRAGE.md (Windows + Linux)
  - TESTS_RAPPORT_AUDIT.md
  - AUDIT_DASHBOARD.md
  - AUDIT_SYNTHESE.md
  - API_ENDPOINTS.md
  - DATABASE_DESIGN.md

#### 🧪 Tests
- **45 tests unitaires** (26 backend + 19 frontend) - 100% de réussite
- Tests backend: JUnit 5 + Mockito
- Tests frontend: Jasmine + Karma
- **Tests E2E validés** :
  - Scénario CLIENT complet (inscription, login, panier, commande)
  - Scénario VENDEUR complet (création produits, gestion commandes)
  - Validation sécurité (JWT, guards, authentification)

#### 🛠️ Corrections
- Fix OrderServiceTest: @TestUni → @Test, setProductPrice → setUnitPrice
- Fix tests frontend: ajout HttpClientTestingModule et provideRouter
- Fix path build.bat Media Service: buy-01 → buy-02
- Fix configuration SSL User Service et Media Service

#### 🏗️ Infrastructure
- Docker Compose (MongoDB, Kafka, Zookeeper)
- MongoDB avec authentification
- Kafka pour événements inter-services
- Jenkins CI/CD pipeline (9 stages)

#### 🎯 Fonctionnalités Complètes

**Côté CLIENT:**
- Inscription et connexion
- Navigation produits avec recherche
- Détails produit avec galerie images
- Panier d'achat persistant
- Validation de stock temps réel
- Processus de commande (checkout)
- Historique des commandes
- Suivi du statut

**Côté VENDEUR:**
- Inscription avec avatar
- Dashboard vendeur
- Création/modification de produits
- Upload d'images multiples
- Gestion du stock
- Consultation des commandes
- Mise à jour des statuts de livraison

#### 📦 Services

**Backend (Spring Boot 3.2.0 + Java 17):**
- User Service (port 8081) - Authentification, profils
- Product Service (port 8082) - Gestion produits, stock
- Media Service (port 8083) - Upload fichiers, avatars
- Order Service (port 8084) - Commandes, statuts

**Frontend (Angular 20.3.6):**
- Interface moderne et responsive
- Guards et intercepteurs
- Services RESTful
- État partagé (cart, auth)

#### 🔐 Sécurité
- HTTPS/SSL activé (ports 8081-8084)
- JWT pour authentification
- BCrypt pour hash des mots de passe
- Guards Angular (AuthGuard, SellerGuard, LoginGuard)
- Intercepteur HTTP pour JWT
- CORS configuré
- Validation des entrées

---

## [0.9.0] - 2026-01-04

### 🚧 Version Pré-Release

#### ✨ Ajouté
- Développement initial de l'architecture
- Création des 4 microservices backend
- Développement du frontend Angular
- Mise en place de Docker Compose
- Création des tests unitaires

---

## Notes de Version

### Configuration Requise
- **Java**: 17 ou supérieur
- **Node.js**: 18 ou supérieur
- **Maven**: 3.8+ (ou mvnd pour Windows)
- **Docker**: 20.10+ avec Docker Compose
- **Angular CLI**: 20+

### Ports Utilisés
- Frontend: 4200 (HTTP)
- User Service: 8081 (HTTPS)
- Product Service: 8082 (HTTPS)
- Media Service: 8083 (HTTPS)
- Order Service: 8084 (HTTPS)
- MongoDB: 27017
- Kafka: 9092
- Zookeeper: 2181
- Jenkins: 8080

### Prochaines Évolutions
- [ ] Tests d'intégration entre services
- [ ] Métriques et monitoring (Prometheus + Grafana)
- [ ] Documentation API (Swagger/OpenAPI)
- [ ] Certificats SSL signés pour production
- [ ] CI/CD complet avec déploiement automatique
- [ ] Gestion des retours et remboursements
- [ ] Système de notes et avis produits
- [ ] Notifications en temps réel (WebSocket)

---

**Contributeurs**: Équipe E-Commerce  
**Licence**: MIT  
**Documentation**: [README.md](README.md)

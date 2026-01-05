# 🚀 GUIDE DE DÉMARRAGE RAPIDE

Guide complet pour démarrer le projet buy-02 sur **Windows** et **Linux**.

---

## 📋 PRÉREQUIS

### Tous les systèmes

- ✅ **Java 17+** 
- ✅ **Maven 3.9+**
- ✅ **Node.js 18+** et npm
- ✅ **Docker Desktop** (ou Docker Engine sur Linux)
- ✅ **Git**

### Vérification

```bash
java -version    # Java 17+
mvn -version     # Maven 3.9+
node --version   # Node 18+
docker --version # Docker 20+
```

---

## 🐳 ÉTAPE 1 : Démarrer l'Infrastructure

### Docker Compose (Windows & Linux)

```bash
# Démarrer MongoDB, Kafka, Zookeeper
docker-compose up -d

# Vérifier que tout tourne
docker ps

# Attendu :
# - ecommerce-mongodb (port 27017)
# - ecommerce-kafka (port 9092)
# - ecommerce-zookeeper (port 2181)
# - jenkins (port 8080) [optionnel]
```

---

## 🔧 ÉTAPE 2 : Compiler les Services Backend

### Windows (CMD)

```cmd
cd backend\user-service
build.bat

cd ..\product-service
build.bat

cd ..\media-service
build.bat

cd ..\order-service
build.bat
```

### Linux (Bash)

```bash
cd backend/user-service
chmod +x build.sh && ./build.sh

cd ../product-service
chmod +x build.sh && ./build.sh

cd ../media-service
chmod +x build.sh && ./build.sh

cd ../order-service
chmod +x build.sh && ./build.sh
```

### Résultat attendu

```
✅ BUILD SUCCESS
JAR file: target/[service-name]-1.0.0.jar
```

---

## 🧪 ÉTAPE 3 : Exécuter les Tests Unitaires

### Windows

```cmd
cd backend\user-service
test.bat

cd ..\product-service
test.bat

cd ..\order-service
test.bat
```

### Linux

```bash
cd backend/user-service
chmod +x test.sh && ./test.sh

cd ../product-service
chmod +x test.sh && ./test.sh

cd ../order-service
chmod +x test.sh && ./test.sh
```

### Frontend (Windows & Linux)

```bash
cd frontend
npm install
npm test -- --watch=false --browsers=ChromeHeadless
```

### Résultats attendus

- ✅ **Backend** : 26 tests - 100% réussite
- ✅ **Frontend** : 19 tests - 100% réussite
- 🎯 **TOTAL** : 45 tests - 0 échec

---

## 🚀 ÉTAPE 4 : Démarrer les Services Backend

### Option A : Manuellement (Windows)

**Ouvrir 4 terminaux CMD/PowerShell** :

```cmd
# Terminal 1
cd backend\user-service
run.bat

# Terminal 2
cd backend\product-service
run.bat

# Terminal 3
cd backend\media-service
run.bat

# Terminal 4
cd backend\order-service
run.bat
```

### Option B : Manuellement (Linux)

**Ouvrir 4 terminaux** :

```bash
# Terminal 1
cd backend/user-service
chmod +x run.sh && ./run.sh

# Terminal 2
cd backend/product-service
chmod +x run.sh && ./run.sh

# Terminal 3
cd backend/media-service
chmod +x run.sh && ./run.sh

# Terminal 4
cd backend/order-service
chmod +x run.sh && ./run.sh
```

### Option C : Script automatique

**Windows :**
```powershell
.\start-all.ps1
```

**Linux :**
```bash
chmod +x start-all.sh && ./start-all.sh
```

### Vérifier que tout tourne

```bash
# User Service
curl http://localhost:8081/api/auth/health

# Product Service
curl http://localhost:8082/api/products

# Media Service
curl http://localhost:8083/api/media/health

# Order Service
curl http://localhost:8084/api/orders/health
```

---

## 🎨 ÉTAPE 5 : Démarrer le Frontend

### Windows & Linux

```bash
cd frontend
npm install    # Si pas déjà fait
npm start
```

**Ouvrir le navigateur :** `http://localhost:4200`

---

## 📝 ÉTAPE 6 : Tests E2E

### Scénario CLIENT

1. ✅ Aller sur `http://localhost:4200`
2. ✅ Cliquer sur **Register**
3. ✅ Créer un compte CLIENT
4. ✅ Se connecter
5. ✅ Aller sur **Products**
6. ✅ Ajouter 2-3 produits au panier
7. ✅ Vérifier le badge compteur
8. ✅ Aller dans **Cart**
9. ✅ Modifier quantités
10. ✅ **RAFRAÎCHIR la page (F5)** → le panier doit rester
11. ✅ Cliquer **Checkout**
12. ✅ Remplir l'adresse
13. ✅ Confirmer la commande
14. ✅ Aller dans **My Orders**
15. ✅ Vérifier la commande apparaît

### Scénario SELLER

1. ✅ Se déconnecter
2. ✅ Créer un compte SELLER avec avatar
3. ✅ Se connecter
4. ✅ Aller sur **Seller Dashboard**
5. ✅ Créer un produit avec 2 images
6. ✅ Modifier le produit
7. ✅ Supprimer une image
8. ✅ Aller dans **Seller Orders**
9. ✅ Changer le statut d'une commande
10. ✅ Aller dans **Seller Profile**
11. ✅ Vérifier les statistiques

### Scénario SÉCURITÉ

1. ✅ Se déconnecter
2. ✅ Essayer d'accéder `/profile` → redirection login
3. ✅ Essayer d'accéder `/seller` → redirection login
4. ✅ Se connecter en CLIENT
5. ✅ Essayer d'accéder `/seller-dashboard` → bloqué

---

## 🛑 ÉTAPE 7 : Arrêter tout

### Windows

```cmd
# Arrêter services (Ctrl+C dans chaque terminal)

# Arrêter Docker
docker-compose down

# Ou script automatique
stop-all.ps1
```

### Linux

```bash
# Arrêter services (Ctrl+C dans chaque terminal)

# Arrêter Docker
docker-compose down

# Ou script automatique
chmod +x stop-all.sh && ./stop-all.sh
```

---

## 📊 ARCHITECTURE DES PORTS

| Service | Port | URL |
|---------|------|-----|
| MongoDB | 27017 | mongodb://localhost:27017 |
| Kafka | 9092 | localhost:9092 |
| Zookeeper | 2181 | localhost:2181 |
| User Service | 8081 | http://localhost:8081 |
| Product Service | 8082 | http://localhost:8082 |
| Media Service | 8083 | http://localhost:8083 |
| Order Service | 8084 | http://localhost:8084 |
| Frontend | 4200 | http://localhost:4200 |
| Jenkins | 8080 | http://localhost:8080 |

---

## 🐛 TROUBLESHOOTING

### Port déjà utilisé

**Windows :**
```cmd
netstat -ano | findstr :8081
taskkill /PID [PID] /F
```

**Linux :**
```bash
lsof -i :8081
kill -9 [PID]
```

### MongoDB connection refused

```bash
# Vérifier Docker
docker ps | grep mongodb

# Redémarrer si nécessaire
docker-compose restart mongodb
```

### Tests échouent

```bash
# Vérifier que MongoDB tourne
docker ps

# Nettoyer et recompiler
mvn clean install
```

### Frontend ne démarre pas

```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm start
```

---

## 📚 DOCUMENTATION SUPPLÉMENTAIRE

- [README.md](README.md) - Vue d'ensemble du projet
- [API_ENDPOINTS.md](docs/API_ENDPOINTS.md) - Documentation API
- [DATABASE_DESIGN.md](docs/DATABASE_DESIGN.md) - Schéma de la base
- [TESTS_README.md](docs/TESTS_README.md) - Guide des tests
- [AUDIT_CHECKLIST.md](AUDIT_CHECKLIST.md) - Checklist audit

---

## 🎯 RÉSUMÉ RAPIDE

```bash
# 1. Infrastructure
docker-compose up -d

# 2. Compiler (choisir OS)
# Windows: .\build-all.bat
# Linux: ./build-all.sh

# 3. Démarrer services backend (4 terminaux)
# Windows: run.bat dans chaque service
# Linux: ./run.sh dans chaque service

# 4. Frontend
cd frontend && npm start

# 5. Ouvrir http://localhost:4200 🎉
```

---

## ✅ CHECKLIST AVANT AUDIT

- [ ] Docker Desktop lancé
- [ ] 4 services backend tournent (8081-8084)
- [ ] Frontend tourne (4200)
- [ ] 45 tests passent (26 backend + 19 frontend)
- [ ] Scénario CLIENT fonctionne
- [ ] Scénario SELLER fonctionne
- [ ] Panier persiste après F5
- [ ] Guards fonctionnent (sécurité)

---

**Bon développement ! 🚀**

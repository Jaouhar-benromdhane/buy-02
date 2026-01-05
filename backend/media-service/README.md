# 📸 Media Service

Service de gestion des médias (images) pour la plateforme e-commerce.

## 📋 Description

Microservice responsable de la gestion des images :
- Upload d'images produits
- Upload d'avatars vendeurs
- Stockage dans MongoDB GridFS
- Récupération d'images

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

**Port :** 8083  
**Base de données :** MongoDB (port 27017)  
**Stockage :** GridFS

### Variables d'environnement (application.yml)

```yaml
server:
  port: 8083

spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  data:
    mongodb:
      uri: mongodb://admin:admin123@localhost:27017/ecommerce?authSource=admin
```

## 📡 API Endpoints

### Images

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/media/upload` | Upload une image |
| GET | `/api/media/{filename}` | Récupère une image |
| DELETE | `/api/media/{filename}` | Supprime une image |

## 📦 Format de requête

### Upload d'image

```bash
curl -X POST http://localhost:8083/api/media/upload \
  -F "file=@image.jpg"
```

**Réponse :**
```json
{
  "url": "/api/media/abc123.jpg"
}
```

## 🧪 Tests

**Tests unitaires :** À implémenter

```bash
# Windows
test.bat

# Linux
./test.sh
```

## 🖼️ Formats supportés

- JPG / JPEG
- PNG
- GIF
- WebP

**Taille maximale :** 10 MB

## 🔗 Dépendances

- Spring Boot 3.2.0
- Spring Data MongoDB
- MongoDB GridFS
- Lombok

## 📝 Logs

Les logs sont affichés dans la console avec le format :
```
INFO com.ecommerce.media.service.MediaService - [message]
```

## 🐛 Troubleshooting

### Erreur "File too large"

**Solution :** Modifier `max-file-size` dans `application.yml`

### Image corrompue

**Solution :** Vérifier que l'image est valide avant upload

## 👥 Auteur

E-Commerce Platform Team

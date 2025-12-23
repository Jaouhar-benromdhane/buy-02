#!/bin/bash

echo "🚀 Démarrage des services backend uniquement..."

# Démarrer Docker si pas déjà lancé
echo "  - Vérification Docker..."
sudo docker-compose up -d 2>/dev/null
sleep 3

# Démarrer les 4 services backend
echo "  - User Service (port 8081)..."
cd backend/user-service
java -jar target/user-service-1.0.0.jar > user.log 2>&1 &
cd ../..
sleep 5

echo "  - Product Service (port 8082)..."
cd backend/product-service
java -jar target/product-service-1.0.0.jar > product.log 2>&1 &
cd ../..
sleep 3

echo "  - Media Service (port 8083)..."
cd backend/media-service
java -jar target/media-service-1.0.0.jar > media.log 2>&1 &
cd ../..
sleep 3

echo "  - Order Service (port 8084)..."
cd backend/order-service
java -jar target/order-service-1.0.0.jar > order.log 2>&1 &
cd ../..
sleep 3

echo "✅ Backends démarrés !"
echo ""
echo "Services backend :"
echo "  - User Service:    https://localhost:8081"
echo "  - Product Service: https://localhost:8082"
echo "  - Media Service:   https://localhost:8083"
echo "  - Order Service:   https://localhost:8084"

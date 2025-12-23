#!/bin/bash

echo "🛑 Arrêt de tous les services..."

# Kill Angular
echo "  - Arrêt Angular (port 4200)..."
pkill -f "ng serve" 2>/dev/null
lsof -ti:4200 | xargs kill -9 2>/dev/null

# Kill tous les services Java Spring Boot
echo "  - Arrêt des services backend..."
pkill -f "user-service" 2>/dev/null
pkill -f "product-service" 2>/dev/null
pkill -f "media-service" 2>/dev/null
pkill -f "order-service" 2>/dev/null

# Kill ports backend
lsof -ti:8081 | xargs kill -9 2>/dev/null
lsof -ti:8082 | xargs kill -9 2>/dev/null
lsof -ti:8083 | xargs kill -9 2>/dev/null
lsof -ti:8084 | xargs kill -9 2>/dev/null

# Arrêt Docker Compose
echo "  - Arrêt Docker Compose..."
sudo docker-compose down 2>/dev/null

echo "✅ Tous les services sont arrêtés !"
echo ""
echo "Pour redémarrer : ./start-all.sh"

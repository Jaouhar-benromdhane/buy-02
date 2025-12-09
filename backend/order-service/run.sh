#!/bin/bash
echo "========================================"
echo "Starting Order Service on port 8084"
echo "========================================"

# Démarrer le service
echo "Starting Order Service..."
java -jar target/order-service-1.0.0.jar

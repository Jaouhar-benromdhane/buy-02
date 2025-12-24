#!/bin/bash

###############################################################################
# SCRIPT EXÉCUTION TOUS LES TESTS UNITAIRES
# Projet: Buy-02 E-Commerce Platform
# Date: 24 Décembre 2024
###############################################################################

echo "════════════════════════════════════════════════════════════════"
echo "   EXÉCUTION TOUS LES TESTS UNITAIRES - BUY-02 PLATFORM"
echo "════════════════════════════════════════════════════════════════"
echo ""

# Compteurs
TOTAL_TESTS=0
TOTAL_FAILURES=0
TOTAL_ERRORS=0

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

###############################################################################
# FONCTION: Exécuter tests Maven
###############################################################################
run_maven_tests() {
    local service_name=$1
    local service_path=$2
    
    echo -e "${BLUE}────────────────────────────────────────────────────────────────${NC}"
    echo -e "${YELLOW}🧪 Testing: $service_name${NC}"
    echo -e "${BLUE}────────────────────────────────────────────────────────────────${NC}"
    
    cd "$service_path" || exit 1
    
    # Exécuter tests Maven
    mvn test -q 2>&1 | tee /tmp/maven_test_output.txt
    
    # Extraire résultats
    local tests=$(grep "Tests run:" /tmp/maven_test_output.txt | tail -1 | sed 's/.*Tests run: \([0-9]*\).*/\1/')
    local failures=$(grep "Tests run:" /tmp/maven_test_output.txt | tail -1 | sed 's/.*Failures: \([0-9]*\).*/\1/')
    local errors=$(grep "Tests run:" /tmp/maven_test_output.txt | tail -1 | sed 's/.*Errors: \([0-9]*\).*/\1/')
    
    # Vérifier si BUILD SUCCESS
    if grep -q "BUILD SUCCESS" /tmp/maven_test_output.txt; then
        echo -e "${GREEN}✅ $service_name: Tests run: $tests, Failures: $failures, Errors: $errors${NC}"
        TOTAL_TESTS=$((TOTAL_TESTS + tests))
        TOTAL_FAILURES=$((TOTAL_FAILURES + failures))
        TOTAL_ERRORS=$((TOTAL_ERRORS + errors))
        return 0
    else
        echo -e "${RED}❌ $service_name: BUILD FAILED${NC}"
        return 1
    fi
}

###############################################################################
# BACKEND TESTS
###############################################################################
echo ""
echo -e "${BLUE}╔═══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║              BACKEND TESTS (Spring Boot + JUnit 5)           ║${NC}"
echo -e "${BLUE}╚═══════════════════════════════════════════════════════════════╝${NC}"
echo ""

BACKEND_SUCCESS=0

# User Service
if run_maven_tests "User Service" "/home/jaouhar/Bureau/buy-02/backend/user-service"; then
    ((BACKEND_SUCCESS++))
fi

echo ""

# Order Service
if run_maven_tests "Order Service" "/home/jaouhar/Bureau/buy-02/backend/order-service"; then
    ((BACKEND_SUCCESS++))
fi

echo ""

# Product Service
if run_maven_tests "Product Service" "/home/jaouhar/Bureau/buy-02/backend/product-service"; then
    ((BACKEND_SUCCESS++))
fi

echo ""

###############################################################################
# FRONTEND TESTS
###############################################################################
echo ""
echo -e "${BLUE}╔═══════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║            FRONTEND TESTS (Angular + Jasmine)                 ║${NC}"
echo -e "${BLUE}╚═══════════════════════════════════════════════════════════════╝${NC}"
echo ""

echo -e "${BLUE}────────────────────────────────────────────────────────────────${NC}"
echo -e "${YELLOW}🧪 Testing: Auth Service (Frontend)${NC}"
echo -e "${BLUE}────────────────────────────────────────────────────────────────${NC}"

cd /home/jaouhar/Bureau/buy-02/frontend || exit 1

# Installer dépendances si nécessaire
if [ ! -d "node_modules" ]; then
    echo "📦 Installation dépendances npm..."
    npm install --silent
fi

# Exécuter tests Karma (headless)
npm test -- --watch=false --browsers=ChromeHeadless 2>&1 | tee /tmp/karma_test_output.txt

# Extraire résultats Karma
if grep -q "Executed.*SUCCESS" /tmp/karma_test_output.txt; then
    frontend_tests=$(grep "Executed" /tmp/karma_test_output.txt | sed 's/.*Executed \([0-9]*\).*/\1/')
    echo -e "${GREEN}✅ Auth Service (Frontend): $frontend_tests tests SUCCESS${NC}"
    TOTAL_TESTS=$((TOTAL_TESTS + frontend_tests))
else
    echo -e "${RED}❌ Frontend Tests: FAILED${NC}"
fi

echo ""

###############################################################################
# RÉSUMÉ FINAL
###############################################################################
echo ""
echo -e "${BLUE}════════════════════════════════════════════════════════════════${NC}"
echo -e "${YELLOW}               RÉSUMÉ TESTS UNITAIRES${NC}"
echo -e "${BLUE}════════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "Backend Services   : ${GREEN}$BACKEND_SUCCESS/3${NC} services réussis"
echo -e "Total Tests        : ${GREEN}$TOTAL_TESTS${NC}"
echo -e "Failures           : ${RED}$TOTAL_FAILURES${NC}"
echo -e "Errors             : ${RED}$TOTAL_ERRORS${NC}"
echo ""

if [ $TOTAL_FAILURES -eq 0 ] && [ $TOTAL_ERRORS -eq 0 ] && [ $BACKEND_SUCCESS -eq 3 ]; then
    echo -e "${GREEN}╔═══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║                                                               ║${NC}"
    echo -e "${GREEN}║     ✅  TOUS LES TESTS RÉUSSIS - CONFORMITÉ 100% ✅           ║${NC}"
    echo -e "${GREEN}║                                                               ║${NC}"
    echo -e "${GREEN}╚═══════════════════════════════════════════════════════════════╝${NC}"
    exit 0
else
    echo -e "${RED}╔═══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${RED}║                                                               ║${NC}"
    echo -e "${RED}║     ❌  CERTAINS TESTS ONT ÉCHOUÉ - VÉRIFIER LOGS ❌          ║${NC}"
    echo -e "${RED}║                                                               ║${NC}"
    echo -e "${RED}╚═══════════════════════════════════════════════════════════════╝${NC}"
    exit 1
fi

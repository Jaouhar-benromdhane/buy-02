# 🧪 Guide Exécution Tests Unitaires

## 📋 Vue d'ensemble

Ce projet contient **37 tests unitaires** couvrant :
- ✅ **25 tests Backend** (Spring Boot + JUnit 5)
- ✅ **12 tests Frontend** (Angular + Jasmine)

**Taux de réussite : 100% ✅**

---

## 🚀 Lancer Tous les Tests

### Option 1 : Script Automatique (Recommandé)

```bash
./run-all-tests.sh
```

Ce script exécute automatiquement tous les tests backend et frontend, et affiche un résumé coloré.

**Résultat attendu :**
```
════════════════════════════════════════════════════════════════
   EXÉCUTION TOUS LES TESTS UNITAIRES - BUY-02 PLATFORM
════════════════════════════════════════════════════════════════

Backend Services   : 3/3 services réussis
Total Tests        : 37
Failures           : 0
Errors             : 0

╔═══════════════════════════════════════════════════════════════╗
║                                                               ║
║     ✅  TOUS LES TESTS RÉUSSIS - CONFORMITÉ 100% ✅           ║
║                                                               ║
╚═══════════════════════════════════════════════════════════════╝
```

### Option 2 : Tests Individuels

#### Backend (Maven)

```bash
# User Service (15 tests)
cd backend/user-service
mvn test

# Order Service (4 tests)
cd backend/order-service
mvn test

# Product Service (6 tests)
cd backend/product-service
mvn test
```

#### Frontend (npm)

```bash
# Auth Service (12 tests)
cd frontend
npm test
```

---

## 📊 Détail des Tests

### Backend Tests (25 tests)

#### 1️⃣ User Service (15 tests)

**Fichiers :**
- `UserServiceTest.java` (6 tests)
- `CartServiceTest.java` (9 tests)

**Commande :**
```bash
cd backend/user-service
mvn test
```

**Tests Couverts :**
```
UserServiceTest (6 tests):
├── testRegisterUser_Success
├── testRegisterUser_EmailAlreadyExists
├── testLoginUser_Success
├── testLoginUser_InvalidCredentials
├── testLoginUser_UserNotFound
└── testPasswordHashing

CartServiceTest (9 tests):
├── testAddToCart_NewItem
├── testAddToCart_ExistingItem
├── testGetCart_Success
├── testGetCart_EmptyCart
├── testUpdateCartItemQuantity_Success
├── testUpdateCartItemQuantity_InvalidQuantity
├── testRemoveFromCart_Success
├── testClearCart_Success
└── testCalculateTotalAmount
```

**Technologies :**
- JUnit 5 (Jupiter)
- Mockito
- BCryptPasswordEncoder
- MongoDB Mock Repository

---

#### 2️⃣ Order Service (4 tests)

**Fichier :**
- `OrderServiceTest.java` (4 tests)

**Commande :**
```bash
cd backend/order-service
mvn test
```

**Tests Couverts :**
```
OrderServiceTest (4 tests):
├── testCreateOrder_Success
├── testCalculateTotalAmount
├── testGetOrderById_Success
└── testCalculateOrderItemSubtotal
```

**Points Techniques :**
- Utilisation enum `PaymentMethod.CASH_ON_DELIVERY`
- Objet `ShippingAddress` complet (fullName, phone, address, city, postalCode, country)
- Mock MongoDB repository

---

#### 3️⃣ Product Service (6 tests)

**Fichier :**
- `ProductServiceTest.java` (6 tests)

**Commande :**
```bash
cd backend/product-service
mvn test
```

**Tests Couverts :**
```
ProductServiceTest (6 tests):
├── testGetAllProducts_Success
├── testGetProductById_Success
├── testGetProductById_NotFound
├── testSearchProducts_Success
├── testDecrementStock_Success
└── testDecrementStock_InsufficientStock
```

**Points Techniques :**
- Retour `Optional<ProductResponse>` pour getProductById
- Validation stock disponible
- Gestion exceptions stock épuisé

---

### Frontend Tests (12 tests)

#### 4️⃣ Auth Service (12 tests)

**Fichier :**
- `auth.spec.ts` (12 tests)

**Commande :**
```bash
cd frontend
npm test
```

**Tests Couverts :**
```
AuthService (12 tests):
├── should be created
├── should register a user
├── should login a user
├── should save token to localStorage on login
├── should logout and clear localStorage
├── should get token
├── should return null if no token
├── should return true if user is logged in
├── should return false if user is not logged in
├── should return true if user is a SELLER
├── should return false if user is not a SELLER
└── should get current user from localStorage
```

**Technologies :**
- Jasmine + Karma
- HttpClientTestingModule
- jasmine.SpyObj pour CartService
- localStorage mock

---

## 🔧 Prérequis

### Backend
- ✅ Java 17+
- ✅ Maven 3.9+
- ✅ Spring Boot 3.x

### Frontend
- ✅ Node.js 20+
- ✅ npm 10+
- ✅ Angular 19

---

## 📦 Installation Dépendances

### Backend
```bash
cd backend/user-service && mvn clean install
cd backend/order-service && mvn clean install
cd backend/product-service && mvn clean install
```

### Frontend
```bash
cd frontend
npm install
```

---

## 🐛 Debugging Tests

### Backend - Logs Détaillés
```bash
# Verbose mode
mvn test -X

# Spécifier un test
mvn test -Dtest=UserServiceTest

# Skip tests
mvn clean install -DskipTests
```

### Frontend - Mode Debug
```bash
# Mode watch (développement)
npm test

# Single run (CI/CD)
npm test -- --watch=false

# Avec couverture
npm test -- --code-coverage

# Browser spécifique
npm test -- --browsers=Chrome
```

---

## 📈 Couverture Code

### Générer Rapport Couverture

#### Backend (JaCoCo)
```bash
cd backend/user-service
mvn test jacoco:report

# Rapport disponible dans:
# target/site/jacoco/index.html
```

#### Frontend (Karma)
```bash
cd frontend
npm test -- --code-coverage

# Rapport disponible dans:
# coverage/index.html
```

---

## 🔗 CI/CD Integration

### Jenkins Pipeline

Le fichier [Jenkinsfile](../Jenkinsfile) inclut automatiquement l'exécution de tous les tests :

```groovy
stage('Test Backend') {
    parallel {
        stage('Test User Service') {
            steps {
                dir('backend/user-service') {
                    sh 'mvn test'
                }
            }
        }
        stage('Test Order Service') {
            steps {
                dir('backend/order-service') {
                    sh 'mvn test'
                }
            }
        }
        stage('Test Product Service') {
            steps {
                dir('backend/product-service') {
                    sh 'mvn test'
                }
            }
        }
    }
}

stage('Test Frontend') {
    steps {
        dir('frontend') {
            sh 'npm test -- --watch=false --browsers=ChromeHeadless'
        }
    }
}
```

---

## 📝 Bonnes Pratiques

### Écrire un Nouveau Test

#### Backend (JUnit 5)
```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {

    @Mock
    private MyRepository repository;

    @InjectMocks
    private MyService service;

    @Test
    void testMyMethod_Success() {
        // Arrange
        when(repository.findById("id")).thenReturn(Optional.of(entity));

        // Act
        Result result = service.myMethod("id");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result.getValue());
        verify(repository, times(1)).findById("id");
    }
}
```

#### Frontend (Jasmine)
```typescript
describe('MyService', () => {
  let service: MyService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MyService]
    });
    service = TestBed.inject(MyService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should retrieve data', () => {
    const mockData = { id: 1, name: 'Test' };

    service.getData().subscribe(data => {
      expect(data).toEqual(mockData);
    });

    const req = httpMock.expectOne('/api/data');
    expect(req.request.method).toBe('GET');
    req.flush(mockData);
  });
});
```

---

## 🚨 Troubleshooting

### Problème: Tests Backend Échouent

**Solution 1 : Clean Install**
```bash
cd backend/<service>
mvn clean install
```

**Solution 2 : Vérifier Java Version**
```bash
java -version  # Doit être 17+
mvn -version   # Doit utiliser Java 17+
```

### Problème: Tests Frontend Échouent

**Solution 1 : Réinstaller Dependencies**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

**Solution 2 : Vérifier Node Version**
```bash
node -v  # Doit être 20+
npm -v   # Doit être 10+
```

### Problème: ChromeHeadless Non Trouvé

**Solution : Installer Chrome ou Utiliser PhantomJS**
```bash
# Ubuntu/Debian
sudo apt-get install chromium-browser

# Ou modifier karma.conf.js
browsers: ['PhantomJS']
```

---

## 📚 Documentation Associée

- 📄 [TESTS_RAPPORT_AUDIT.md](../TESTS_RAPPORT_AUDIT.md) - Rapport complet audit
- 📄 [AMELIORATIONS_24DEC.md](AMELIORATIONS_24DEC.md) - Documentation améliorations
- 📄 [API_ENDPOINTS.md](API_ENDPOINTS.md) - Documentation API REST
- 📄 [Jenkinsfile](../Jenkinsfile) - Pipeline CI/CD

---

## 🎯 Résumé Rapide

```bash
# Lancer TOUS les tests
./run-all-tests.sh

# Backend uniquement
cd backend/user-service && mvn test
cd backend/order-service && mvn test
cd backend/product-service && mvn test

# Frontend uniquement
cd frontend && npm test

# Résultat attendu : 37 tests - 0 failures ✅
```

---

## 📞 Support

Pour toute question ou problème avec les tests, consulter :
1. Ce README
2. [TESTS_RAPPORT_AUDIT.md](../TESTS_RAPPORT_AUDIT.md)
3. Logs d'exécution des tests
4. Documentation Javadoc (backend)
5. JSDoc comments (frontend)

---

**Dernière mise à jour :** 24 Décembre 2024  
**Taux de réussite :** 100% (37/37 tests) ✅

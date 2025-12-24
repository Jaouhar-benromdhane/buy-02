import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Auth } from './auth';
import { CartService } from './cart-backend.service';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/user.model';

/**
 * TESTS UNITAIRES - AUTH SERVICE (Frontend)
 * 
 * Tests critiques :
 * 1. Service créé correctement
 * 2. Inscription utilisateur
 * 3. Connexion utilisateur
 * 4. Déconnexion
 * 5. Récupération token
 * 6. Vérification authentification
 * 7. Vérification rôle SELLER
 */
describe('Auth Service', () => {
  let service: Auth;
  let httpMock: HttpTestingController;
  let cartServiceSpy: jasmine.SpyObj<CartService>;

  const API_URL = 'https://localhost:8081/api/auth';

  beforeEach(() => {
    // Mock CartService
    const cartSpy = jasmine.createSpyObj('CartService', ['loadCart', 'clearCartCache']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        Auth,
        { provide: CartService, useValue: cartSpy }
      ]
    });

    service = TestBed.inject(Auth);
    httpMock = TestBed.inject(HttpTestingController);
    cartServiceSpy = TestBed.inject(CartService) as jasmine.SpyObj<CartService>;

    // Nettoyer localStorage avant chaque test
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  /**
   * TEST 1 : Service créé
   */
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /**
   * TEST 2 : INSCRIPTION - Succès
   */
  it('should register a new user', () => {
    const registerData: RegisterRequest = {
      email: 'test@example.com',
      password: 'Password123!',
      name: 'Test User',
      role: 'CLIENT'
    };

    const mockResponse = {
      token: 'jwt-token-123',
      userId: 'user123',
      email: 'test@example.com',
      name: 'Test User',
      role: 'CLIENT'
    };

    service.register(registerData).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${API_URL}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerData);
    req.flush(mockResponse);
  });

  /**
   * TEST 3 : CONNEXION - Succès
   */
  it('should login user and save token', () => {
    const loginData: LoginRequest = {
      email: 'test@example.com',
      password: 'Password123!'
    };

    const mockResponse: AuthResponse = {
      token: 'jwt-token-456',
      userId: 'user123',
      email: 'test@example.com',
      name: 'Test User',
      role: 'CLIENT',
      avatar: null
    };

    service.login(loginData).subscribe(response => {
      expect(response).toEqual(mockResponse);
      expect(service.getToken()).toBe('jwt-token-456');
      expect(service.isLoggedIn()).toBe(true);
      expect(service.getCurrentUser()?.email).toBe('test@example.com');
      expect(cartServiceSpy.loadCart).toHaveBeenCalled();
    });

    const req = httpMock.expectOne(`${API_URL}/login`);
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });

  /**
   * TEST 4 : DÉCONNEXION - Succès
   */
  it('should logout user and clear storage', () => {
    // Simuler un utilisateur connecté
    localStorage.setItem('auth_token', 'jwt-token-789');
    localStorage.setItem('current_user', JSON.stringify({
      id: 'user123',
      email: 'test@example.com',
      name: 'Test User',
      role: 'CLIENT'
    }));

    // Déconnexion
    service.logout();

    expect(service.getToken()).toBeNull();
    expect(service.isLoggedIn()).toBe(false);
    expect(service.getCurrentUser()).toBeNull();
    expect(cartServiceSpy.clearCartCache).toHaveBeenCalled();
  });

  /**
   * TEST 5 : GET TOKEN - Avec token
   */
  it('should return token when logged in', () => {
    localStorage.setItem('auth_token', 'my-jwt-token');
    expect(service.getToken()).toBe('my-jwt-token');
  });

  /**
   * TEST 6 : GET TOKEN - Sans token
   */
  it('should return null when not logged in', () => {
    expect(service.getToken()).toBeNull();
  });

  /**
   * TEST 7 : IS LOGGED IN - True
   */
  it('should return true when user is logged in', () => {
    localStorage.setItem('auth_token', 'jwt-token');
    expect(service.isLoggedIn()).toBe(true);
  });

  /**
   * TEST 8 : IS LOGGED IN - False
   */
  it('should return false when user is not logged in', () => {
    expect(service.isLoggedIn()).toBe(false);
  });

  /**
   * TEST 9 : IS SELLER - True
   */
  it('should return true when user is a seller', () => {
    localStorage.setItem('current_user', JSON.stringify({
      id: 'user123',
      email: 'seller@example.com',
      name: 'Test Seller',
      role: 'SELLER'
    }));

    expect(service.isSeller()).toBe(true);
  });

  /**
   * TEST 10 : IS SELLER - False
   */
  it('should return false when user is a client', () => {
    localStorage.setItem('current_user', JSON.stringify({
      id: 'user123',
      email: 'client@example.com',
      name: 'Test Client',
      role: 'CLIENT'
    }));

    expect(service.isSeller()).toBe(false);
  });

  /**
   * TEST 11 : GET CURRENT USER - User exists
   */
  it('should return current user when logged in', () => {
    const mockUser = {
      id: 'user123',
      email: 'test@example.com',
      name: 'Test User',
      role: 'CLIENT'
    };
    localStorage.setItem('current_user', JSON.stringify(mockUser));

    const user = service.getCurrentUser();
    expect(user).toEqual(mockUser);
  });

  /**
   * TEST 12 : GET CURRENT USER - No user
   */
  it('should return null when no user logged in', () => {
    expect(service.getCurrentUser()).toBeNull();
  });
});

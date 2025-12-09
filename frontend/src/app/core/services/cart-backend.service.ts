import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, catchError, map } from 'rxjs/operators';
import { CartItem, CartSummary, AddToCartRequest, UpdateCartQuantityRequest } from '../models/cart.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private apiUrl = 'https://localhost:8081/api/cart';
  private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);
  public cartItems$: Observable<CartItem[]> = this.cartItemsSubject.asObservable();

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  private getCurrentUserId(): string | null {
    const user = localStorage.getItem('current_user');
    if (user) {
      const userData = JSON.parse(user);
      return userData.id || userData.userId;
    }
    return null;
  }

  /**
   * Charger le panier depuis le backend
   */
  loadCart(): Observable<CartSummary> {
    const userId = this.getCurrentUserId();
    if (!userId) {
      this.cartItemsSubject.next([]);
      return throwError(() => new Error('User not logged in'));
    }

    return this.http.get<CartSummary>(`${this.apiUrl}/${userId}`, {
      headers: this.getHeaders()
    }).pipe(
      tap(summary => {
        this.cartItemsSubject.next(summary.items);
      }),
      catchError(error => {
        console.error('Error loading cart:', error);
        this.cartItemsSubject.next([]);
        return throwError(() => error);
      })
    );
  }

  /**
   * Ajouter un produit au panier
   */
  addToCart(request: AddToCartRequest): Observable<CartItem> {
    return this.http.post<CartItem>(this.apiUrl, request, {
      headers: this.getHeaders()
    }).pipe(
      tap(() => {
        // Recharger le panier après ajout
        this.loadCart().subscribe();
      }),
      catchError(error => {
        console.error('Error adding to cart:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Mettre à jour la quantité d'un item
   */
  updateQuantity(itemId: string, quantity: number): Observable<CartItem> {
    const request: UpdateCartQuantityRequest = { quantity };
    return this.http.put<CartItem>(`${this.apiUrl}/item/${itemId}`, request, {
      headers: this.getHeaders()
    }).pipe(
      tap(() => {
        // Recharger le panier après mise à jour
        this.loadCart().subscribe();
      }),
      catchError(error => {
        console.error('Error updating quantity:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Supprimer un item du panier
   */
  removeItem(itemId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/item/${itemId}`, {
      headers: this.getHeaders()
    }).pipe(
      tap(() => {
        // Recharger le panier après suppression
        this.loadCart().subscribe();
      }),
      catchError(error => {
        console.error('Error removing item:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Vider le panier
   */
  clearCart(): Observable<void> {
    const userId = this.getCurrentUserId();
    if (!userId) {
      return throwError(() => new Error('User not logged in'));
    }

    return this.http.delete<void>(`${this.apiUrl}/${userId}`, {
      headers: this.getHeaders()
    }).pipe(
      tap(() => {
        this.cartItemsSubject.next([]);
      }),
      catchError(error => {
        console.error('Error clearing cart:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Obtenir le nombre total d'items
   */
  getCartCount(): Observable<number> {
    const userId = this.getCurrentUserId();
    if (!userId) {
      return new BehaviorSubject(0).asObservable();
    }

    return this.http.get<number>(`${this.apiUrl}/${userId}/count`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error getting cart count:', error);
        return new BehaviorSubject(0).asObservable();
      })
    );
  }

  /**
   * Obtenir les items actuels du panier (depuis le cache)
   */
  getCartItems(): CartItem[] {
    return this.cartItemsSubject.value;
  }

  /**
   * Calculer le total du panier (depuis le cache)
   */
  getCartTotal(): number {
    return this.cartItemsSubject.value.reduce(
      (total, item) => total + (item.productPrice * item.quantity), 
      0
    );
  }

  /**
   * Vérifier si un produit est dans le panier
   */
  isProductInCart(productId: string): Observable<boolean> {
    const userId = this.getCurrentUserId();
    if (!userId) {
      return new BehaviorSubject(false).asObservable();
    }

    return this.http.get<boolean>(`${this.apiUrl}/${userId}/has/${productId}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(() => new BehaviorSubject(false).asObservable())
    );
  }

  /**
   * Nettoyer le cache lors de la déconnexion
   */
  clearCartCache(): void {
    this.cartItemsSubject.next([]);
  }
}

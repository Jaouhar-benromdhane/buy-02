import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Order, CreateOrderRequest, UpdateOrderStatusRequest, CancelOrderRequest } from '../models/order.model';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = 'https://localhost:8084/api/orders';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  /**
   * Créer une nouvelle commande
   */
  createOrder(request: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, request, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error creating order:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Récupérer les commandes d'un utilisateur
   */
  getUserOrders(userId: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/user/${userId}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching user orders:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Récupérer les commandes d'un vendeur
   */
  getSellerOrders(sellerId: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/seller/${sellerId}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching seller orders:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Récupérer une commande par ID
   */
  getOrderById(orderId: string): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/${orderId}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching order:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Récupérer une commande par numéro
   */
  getOrderByNumber(orderNumber: string): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/number/${orderNumber}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error fetching order by number:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Mettre à jour le statut d'une commande
   */
  updateOrderStatus(orderId: string, request: UpdateOrderStatusRequest): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${orderId}/status`, request, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error updating order status:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Annuler une commande
   */
  cancelOrder(orderId: string, request: CancelOrderRequest): Observable<Order> {
    return this.http.post<Order>(`${this.apiUrl}/${orderId}/cancel`, request, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error cancelling order:', error);
        return throwError(() => error);
      })
    );
  }

  /**
   * Rechercher des commandes par statut
   */
  searchOrdersByStatus(status: string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/search?status=${status}`, {
      headers: this.getHeaders()
    }).pipe(
      catchError(error => {
        console.error('Error searching orders:', error);
        return throwError(() => error);
      })
    );
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { OrderService } from '../../core/services/order.service';
import { Order, OrderStatus } from '../../core/models/order.model';

@Component({
  selector: 'app-order-history',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatToolbarModule,
    MatDividerModule,
    MatSnackBarModule
  ],
  templateUrl: './order-history.html',
  styleUrl: './order-history.scss'
})
export class OrderHistoryPage implements OnInit {
  orders: Order[] = [];
  loading = false;

  constructor(
    private orderService: OrderService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    const user = JSON.parse(localStorage.getItem('current_user') || '{}');
    const userId = user.id || user.userId;

    if (!userId) {
      this.snackBar.open('Utilisateur non connecté', 'Fermer', { duration: 3000 });
      this.router.navigate(['/login']);
      return;
    }

    this.orderService.getUserOrders(userId).subscribe({
      next: (orders) => {
        this.orders = orders;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading orders:', error);
        this.loading = false;
        this.snackBar.open('Erreur lors du chargement des commandes', 'Fermer', { duration: 3000 });
      }
    });
  }

  getStatusLabel(status?: OrderStatus): string {
    const labels: Record<OrderStatus, string> = {
      [OrderStatus.PENDING]: 'En attente',
      [OrderStatus.CONFIRMED]: 'Confirmée',
      [OrderStatus.SHIPPED]: 'Expédiée',
      [OrderStatus.DELIVERED]: 'Livrée',
      [OrderStatus.CANCELLED]: 'Annulée'
    };
    return status ? labels[status] : 'Inconnu';
  }

  getStatusColor(status?: OrderStatus): string {
    const colors: Record<OrderStatus, string> = {
      [OrderStatus.PENDING]: 'accent',
      [OrderStatus.CONFIRMED]: 'primary',
      [OrderStatus.SHIPPED]: 'primary',
      [OrderStatus.DELIVERED]: '',
      [OrderStatus.CANCELLED]: 'warn'
    };
    return status ? colors[status] : '';
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }
}

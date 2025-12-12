import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { Router } from '@angular/router';
import { Auth } from '../../core/services/auth';
import { OrderService } from '../../core/services/order.service';
import { User } from '../../core/models/user.model';
import { Order, OrderStatus } from '../../core/models/order.model';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatDividerModule,
    MatChipsModule
  ],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.scss'
})
export class UserProfilePage implements OnInit {
  user = signal<User | null>(null);
  orders = signal<Order[]>([]);
  loading = signal(true);
  
  // Statistics
  totalOrders = signal(0);
  totalSpent = signal(0);
  pendingOrders = signal(0);
  deliveredOrders = signal(0);

  constructor(
    private authService: Auth,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.loading.set(true);
    const currentUser = this.authService.getCurrentUser();
    
    if (!currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    this.user.set(currentUser);

    // Load user orders to calculate statistics
    this.orderService.getUserOrders(currentUser.id).subscribe({
      next: (orders) => {
        this.orders.set(orders);
        this.calculateStatistics(orders);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des commandes:', error);
        this.loading.set(false);
      }
    });
  }

  calculateStatistics(orders: Order[]): void {
    this.totalOrders.set(orders.length);
    
    const spent = orders
      .filter(o => o.status !== OrderStatus.CANCELLED)
      .reduce((sum, o) => sum + (o.totalAmount || 0), 0);
    this.totalSpent.set(spent);

    const pending = orders.filter(o => 
      o.status === OrderStatus.PENDING || 
      o.status === OrderStatus.CONFIRMED ||
      o.status === OrderStatus.SHIPPED
    ).length;
    this.pendingOrders.set(pending);

    const delivered = orders.filter(o => o.status === OrderStatus.DELIVERED).length;
    this.deliveredOrders.set(delivered);
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }

  viewOrders(): void {
    this.router.navigate(['/orders']);
  }

  editProfile(): void {
    // TODO: Implement edit profile functionality
    alert('Fonctionnalité à venir : Modifier le profil');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

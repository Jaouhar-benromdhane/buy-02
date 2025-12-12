import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router } from '@angular/router';
import { Auth } from '../../core/services/auth';
import { OrderService } from '../../core/services/order.service';
import { User } from '../../core/models/user.model';
import { Order, OrderStatus } from '../../core/models/order.model';

@Component({
  selector: 'app-seller-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatDividerModule,
    MatChipsModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './seller-profile.html',
  styleUrl: './seller-profile.scss'
})
export class SellerProfilePage implements OnInit {
  user = signal<User | null>(null);
  orders = signal<Order[]>([]);
  loading = signal(true);
  
  // Statistics
  totalSales = signal(0);
  totalRevenue = signal(0);
  pendingOrders = signal(0);
  completedOrders = signal(0);
  activeProducts = signal(0);

  constructor(
    private authService: Auth,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSellerProfile();
  }

  loadSellerProfile(): void {
    this.loading.set(true);
    const currentUser = this.authService.getCurrentUser();
    
    if (!currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    if (currentUser.role !== 'SELLER') {
      this.router.navigate(['/profile']);
      return;
    }

    this.user.set(currentUser);

    // Load seller orders to calculate statistics
    this.orderService.getSellerOrders(currentUser.id).subscribe({
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
    this.totalSales.set(orders.length);
    
    const revenue = orders
      .filter(o => o.status !== OrderStatus.CANCELLED)
      .reduce((sum, o) => sum + (o.totalAmount || 0), 0);
    this.totalRevenue.set(revenue);

    const pending = orders.filter(o => 
      o.status === OrderStatus.PENDING || 
      o.status === OrderStatus.CONFIRMED ||
      o.status === OrderStatus.SHIPPED
    ).length;
    this.pendingOrders.set(pending);

    const completed = orders.filter(o => o.status === OrderStatus.DELIVERED).length;
    this.completedOrders.set(completed);

    // Count unique products (would need product service endpoint in real app)
    const uniqueProducts = new Set<string>();
    orders.forEach(order => {
      order.items.forEach(item => uniqueProducts.add(item.productId));
    });
    this.activeProducts.set(uniqueProducts.size);
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }

  viewOrders(): void {
    this.router.navigate(['/seller/orders']);
  }

  viewDashboard(): void {
    this.router.navigate(['/seller/dashboard']);
  }

  editProfile(): void {
    alert('Fonctionnalité à venir : Modifier le profil');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

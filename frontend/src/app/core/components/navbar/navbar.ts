import { Component, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Auth } from '../../services/auth';
import { CartService } from '../../services/cart-backend.service';
import { OrderService } from '../../services/order.service';
import { OrderStatus } from '../../models/order.model';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatBadgeModule,
    MatDividerModule,
    MatTooltipModule
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})
export class Navbar {
  currentUser = computed(() => this.authService.getCurrentUser());
  isSeller = computed(() => this.currentUser()?.role === 'SELLER');
  cartItemCount = signal(0);
  pendingOrdersCount = signal(0);

  constructor(
    private authService: Auth,
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router
  ) {
    // Subscribe to cart items to update count
    this.cartService.cartItems$.subscribe(items => {
      const count = items.reduce((sum, item) => sum + item.quantity, 0);
      this.cartItemCount.set(count);
    });

    // Load pending orders count for sellers
    const user = this.currentUser();
    if (user && user.role === 'SELLER') {
      this.loadPendingOrdersCount(user.id);
      
      // Refresh every 30 seconds
      setInterval(() => {
        const currentUser = this.currentUser();
        if (currentUser && currentUser.role === 'SELLER') {
          this.loadPendingOrdersCount(currentUser.id);
        }
      }, 30000);
    }
  }

  private loadPendingOrdersCount(sellerId: string): void {
    this.orderService.getSellerOrders(sellerId).subscribe({
      next: (orders) => {
        const pending = orders.filter(o => 
          o.status === OrderStatus.PENDING || 
          o.status === OrderStatus.CONFIRMED
        ).length;
        this.pendingOrdersCount.set(pending);
      },
      error: (err) => console.error('Error loading pending orders:', err)
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

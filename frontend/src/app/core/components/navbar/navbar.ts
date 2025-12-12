import { Component, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';
import { Auth } from '../../services/auth';
import { CartService } from '../../services/cart-backend.service';

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
    MatDividerModule
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})
export class Navbar {
  currentUser = computed(() => this.authService.getCurrentUser());
  isSeller = computed(() => this.currentUser()?.role === 'SELLER');
  cartItemCount = signal(0);

  constructor(
    private authService: Auth,
    private cartService: CartService,
    private router: Router
  ) {
    // Subscribe to cart items to update count
    this.cartService.cartItems$.subscribe(items => {
      const count = items.reduce((sum, item) => sum + item.quantity, 0);
      this.cartItemCount.set(count);
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CartService } from '../../core/services/cart-backend.service';
import { ProductService } from '../../core/services/product.service';
import { CartItem } from '../../core/models/cart.model';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatToolbarModule,
    MatDividerModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './cart.html',
  styleUrl: './cart.scss'
})
export class CartPage implements OnInit {
  cartItems: CartItem[] = [];
  displayedColumns: string[] = ['image', 'name', 'price', 'quantity', 'total', 'actions'];
  loading = false;

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadCart();
    this.cartService.cartItems$.subscribe((items: CartItem[]) => {
      this.cartItems = items;
    });
  }

  loadCart(): void {
    this.loading = true;
    this.cartService.loadCart().subscribe({
      next: (summary) => {
        this.cartItems = summary.items;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading cart:', error);
        this.loading = false;
      }
    });
  }

  increaseQuantity(item: CartItem): void {
    if (!item.id) return;
    
    // Vérifier le stock actuel en temps réel depuis l'API
    this.productService.getProductById(item.productId).subscribe({
      next: (product) => {
        const currentStock = product.stock;
        
        if (item.quantity < currentStock) {
          // Stock suffisant, on peut augmenter
          this.cartService.updateQuantity(item.id!, item.quantity + 1).subscribe({
            next: () => {
              this.snackBar.open('Quantité mise à jour', 'Fermer', { duration: 2000 });
            },
            error: (error) => {
              console.error('Error updating quantity:', error);
              this.snackBar.open('Erreur lors de la mise à jour', 'Fermer', { duration: 3000 });
            }
          });
        } else {
          // Stock insuffisant
          this.snackBar.open(`Stock insuffisant. Disponible: ${currentStock}`, 'Fermer', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
        }
      },
      error: (error) => {
        console.error('Error checking stock:', error);
        this.snackBar.open('Erreur lors de la vérification du stock', 'Fermer', { duration: 3000 });
      }
    });
  }

  decreaseQuantity(item: CartItem): void {
    if (!item.id || item.quantity <= 1) return;
    
    this.cartService.updateQuantity(item.id, item.quantity - 1).subscribe({
      next: () => {
        this.snackBar.open('Quantité mise à jour', 'Fermer', { duration: 2000 });
      },
      error: (error) => {
        console.error('Error updating quantity:', error);
        this.snackBar.open('Erreur lors de la mise à jour', 'Fermer', { duration: 3000 });
      }
    });
  }

  removeItem(item: CartItem): void {
    if (!item.id) return;
    
    this.cartService.removeItem(item.id).subscribe({
      next: () => {
        this.snackBar.open(`${item.productName} retiré du panier`, 'Fermer', {
          duration: 2000,
          panelClass: ['success-snackbar']
        });
      },
      error: (error) => {
        console.error('Error removing item:', error);
        this.snackBar.open('Erreur lors de la suppression', 'Fermer', { duration: 3000 });
      }
    });
  }

  clearCart(): void {
    if (confirm('Vider tout le panier ?')) {
      this.cartService.clearCart().subscribe({
        next: () => {
          this.snackBar.open('Panier vidé', 'Fermer', { duration: 2000 });
        },
        error: (error) => {
          console.error('Error clearing cart:', error);
          this.snackBar.open('Erreur lors du vidage du panier', 'Fermer', { duration: 3000 });
        }
      });
    }
  }

  getTotal(): number {
    return this.cartService.getCartTotal();
  }

  getItemTotal(item: CartItem): number {
    return item.productPrice * item.quantity;
  }

  continueShopping(): void {
    this.router.navigate(['/products']);
  }

  checkout(): void {
    if (this.cartItems.length === 0) {
      this.snackBar.open('Votre panier est vide', 'Fermer', { duration: 3000 });
      return;
    }
    this.router.navigate(['/checkout']);
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }
}

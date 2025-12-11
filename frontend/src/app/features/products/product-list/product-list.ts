import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Product } from '../../../core/services/product';
import { MediaService } from '../../../core/services/media';
import { CartService } from '../../../core/services/cart-backend.service';
import { AddToCartRequest } from '../../../core/models/cart.model';
import { Auth } from '../../../core/services/auth';
import { Product as ProductModel } from '../../../core/models/product.model';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Component({
  selector: 'app-product-list',
  imports: [
    CommonModule,
    FormsModule,
    MatToolbarModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatBadgeModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './product-list.html',
  styleUrl: './product-list.scss',
})
export class ProductList implements OnInit {
  products: any[] = []; // Produits avec leurs images
  loading = false;
  errorMessage = '';
  searchKeyword = '';
  cartCount = 0;

  constructor(
    private productService: Product,
    private mediaService: MediaService,
    private cartService: CartService,
    private authService: Auth,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.updateCartCount();
    this.cartService.cartItems$.subscribe(() => {
      this.updateCartCount();
    });
  }

  loadProducts(): void {
    this.loading = true;
    this.errorMessage = '';

    this.productService.getAllProducts().subscribe({
      next: (products) => {
        // Pour chaque produit, charger ses images
        const productsWithImages$ = products.map(product => 
          this.mediaService.getMediaByProduct(product.id).pipe(
            map(media => ({
              ...product,
              imageUrl: media.length > 0 ? this.mediaService.getImageUrl(media[0].url) : null
            })),
            catchError(() => of({ ...product, imageUrl: null }))
          )
        );

        // Attendre que toutes les images soient chargées
        if (productsWithImages$.length > 0) {
          forkJoin(productsWithImages$).subscribe({
            next: (productsWithImages) => {
              this.products = productsWithImages;
              this.loading = false;
              console.log('Produits avec images:', productsWithImages);
            },
            error: (error) => {
              console.error('Erreur chargement images:', error);
              this.products = products;
              this.loading = false;
            }
          });
        } else {
          this.products = [];
          this.loading = false;
        }
      },
      error: (error) => {
        console.error('Erreur de chargement des produits:', error);
        this.errorMessage = 'Impossible de charger les produits. Vérifiez que le backend est démarré.';
        this.loading = false;
      }
    });
  }

  onSearch(): void {
    if (!this.searchKeyword.trim()) {
      this.loadProducts();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.productService.searchProducts(this.searchKeyword).subscribe({
      next: (products) => {
        // Charger les images pour les résultats de recherche
        const productsWithImages$ = products.map(product => 
          this.mediaService.getMediaByProduct(product.id).pipe(
            map(media => ({
              ...product,
              imageUrl: media.length > 0 ? this.mediaService.getImageUrl(media[0].url) : null
            })),
            catchError(() => of({ ...product, imageUrl: null }))
          )
        );

        if (productsWithImages$.length > 0) {
          forkJoin(productsWithImages$).subscribe({
            next: (productsWithImages) => {
              this.products = productsWithImages;
              this.loading = false;
            },
            error: (error) => {
              console.error('Erreur chargement images:', error);
              this.products = products;
              this.loading = false;
            }
          });
        } else {
          this.products = [];
          this.loading = false;
        }
      },
      error: (error) => {
        console.error('Erreur de recherche:', error);
        this.errorMessage = 'Erreur lors de la recherche';
        this.loading = false;
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  viewDetails(productId: string): void {
    this.router.navigate(['/products', productId]);
  }

  updateCartCount(): void {
    this.cartService.getCartCount().subscribe({
      next: (count) => {
        this.cartCount = count;
      },
      error: () => {
        this.cartCount = 0;
      }
    });
  }

  addToCart(product: any): void {
    if (product.stock === 0) {
      this.snackBar.open('Produit en rupture de stock', 'Fermer', {
        duration: 2000,
        panelClass: ['error-snackbar']
      });
      return;
    }

    // Obtenir userId
    const user = JSON.parse(localStorage.getItem('current_user') || '{}');
    const userId = user.id || user.userId;

    if (!userId) {
      this.snackBar.open('Veuillez vous connecter', 'Fermer', { duration: 3000 });
      return;
    }

    // Vérifier le stock
    const currentCart = this.cartService.getCartItems();
    const existingItem = currentCart.find((item: any) => item.productId === product.id);
    const currentQuantityInCart = existingItem ? existingItem.quantity : 0;

    if (currentQuantityInCart >= product.stock) {
      this.snackBar.open(`Stock maximum atteint (${product.stock} disponible)`, 'Fermer', {
        duration: 3000,
        panelClass: ['error-snackbar']
      });
      return;
    }

    // Préparer la requête
    const addToCartRequest: AddToCartRequest = {
      userId: userId,
      productId: product.id,
      productName: product.name,
      productImage: product.imageUrl || null,
      productPrice: product.price,
      quantity: 1,
      sellerId: product.sellerId,
      sellerName: product.sellerName
    };

    // Ajouter au panier via API
    this.cartService.addToCart(addToCartRequest).subscribe({
      next: () => {
        this.snackBar.open(`${product.name} ajouté au panier`, 'Voir le panier', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
          panelClass: ['success-snackbar']
        }).onAction().subscribe(() => {
          this.router.navigate(['/cart']);
        });
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
        this.snackBar.open('Erreur lors de l\'ajout au panier', 'Fermer', { duration: 3000 });
      }
    });
  }

  goToCart(): void {
    this.router.navigate(['/cart']);
  }
}


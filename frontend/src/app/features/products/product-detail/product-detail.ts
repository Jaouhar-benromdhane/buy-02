import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Product as ProductModel } from '../../../core/models/product.model';
import { Product } from '../../../core/services/product';
import { MediaService } from '../../../core/services/media';
import { CartService } from '../../../core/services/cart-backend.service';
import { AddToCartRequest } from '../../../core/models/cart.model';
import { forkJoin, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.scss'
})
export class ProductDetail implements OnInit {
  product: ProductModel | null = null;
  images: string[] = [];
  selectedImageIndex = 0;
  loading = true;
  errorMessage = '';
  quantity = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: Product,
    private mediaService: MediaService,
    private cartService: CartService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.loadProduct(productId);
    } else {
      this.errorMessage = 'ID produit manquant';
      this.loading = false;
    }
  }

  loadProduct(id: string): void {
    this.loading = true;
    this.errorMessage = '';

    forkJoin({
      product: this.productService.getProductById(id),
      media: this.mediaService.getMediaByProduct(id).pipe(
        catchError(() => of([]))
      )
    }).subscribe({
      next: (result) => {
        this.product = result.product;
        this.images = result.media.map(m => this.mediaService.getImageUrl(m.url));
        this.loading = false;
        console.log('Produit chargé:', this.product);
        console.log('Images:', this.images);
      },
      error: (error) => {
        console.error('Erreur chargement produit:', error);
        this.errorMessage = 'Impossible de charger le produit';
        this.loading = false;
      }
    });
  }

  selectImage(index: number): void {
    this.selectedImageIndex = index;
  }

  previousImage(): void {
    if (this.selectedImageIndex > 0) {
      this.selectedImageIndex--;
    }
  }

  nextImage(): void {
    if (this.selectedImageIndex < this.images.length - 1) {
      this.selectedImageIndex++;
    }
  }

  increaseQuantity(): void {
    if (this.product && this.quantity < this.product.stock) {
      this.quantity++;
    }
  }

  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  addToCart(): void {
    if (!this.product) return;

    // Obtenir userId
    const user = JSON.parse(localStorage.getItem('current_user') || '{}');
    const userId = user.id || user.userId;

    if (!userId) {
      this.snackBar.open('Veuillez vous connecter', 'Fermer', { duration: 3000 });
      return;
    }

    // Vérifier que le vendeur n'achète pas son propre produit
    if (userId === this.product.sellerId) {
      this.snackBar.open('Vous ne pouvez pas acheter vos propres produits', 'Fermer', { 
        duration: 3000,
        panelClass: ['error-snackbar']
      });
      return;
    }

    // Vérifier le stock
    const currentCart = this.cartService.getCartItems();
    const existingItem = currentCart.find((item: any) => item.productId === this.product!.id);
    const currentQuantityInCart = existingItem ? existingItem.quantity : 0;

    if (currentQuantityInCart + this.quantity > this.product.stock) {
      const remaining = this.product.stock - currentQuantityInCart;
      this.snackBar.open(
        remaining > 0 
          ? `Stock insuffisant ! Seulement ${remaining} disponible(s) en plus` 
          : `Stock maximum déjà atteint (${this.product.stock} en stock)`,
        'Fermer',
        {
          duration: 4000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['error-snackbar']
        }
      );
      return;
    }

    // Préparer la requête pour le backend
    const addToCartRequest: AddToCartRequest = {
      userId: userId,
      productId: this.product.id,
      productName: this.product.name,
      productImage: this.images[0] || null,
      productPrice: this.product.price,
      quantity: this.quantity,
      sellerId: this.product.sellerId,
      sellerName: this.product.sellerName
    };

    // Ajouter au panier via API
    this.cartService.addToCart(addToCartRequest).subscribe({
      next: () => {
        this.snackBar.open(`${this.quantity} x ${this.product!.name} ajouté au panier`, 'Voir le panier', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
          panelClass: ['success-snackbar']
        }).onAction().subscribe(() => {
          this.router.navigate(['/cart']);
        });
        this.quantity = 1;
      },
      error: (error) => {
        console.error('Error adding to cart:', error);
        this.snackBar.open('Erreur lors de l\'ajout au panier', 'Fermer', { duration: 3000 });
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }

  get stockStatus(): string {
    if (!this.product) return '';
    if (this.product.stock === 0) return 'Rupture de stock';
    if (this.product.stock < 10) return 'Stock limité';
    return 'En stock';
  }

  get stockStatusClass(): string {
    if (!this.product) return '';
    if (this.product.stock === 0) return 'out-of-stock';
    if (this.product.stock < 10) return 'low-stock';
    return 'in-stock';
  }
}

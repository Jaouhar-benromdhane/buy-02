import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatRadioModule } from '@angular/material/radio';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CartService } from '../../core/services/cart-backend.service';
import { OrderService } from '../../core/services/order.service';
import { CartItem } from '../../core/models/cart.model';
import { CreateOrderRequest, PaymentMethod, OrderItem } from '../../core/models/order.model';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatRadioModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatToolbarModule
  ],
  templateUrl: './checkout.html',
  styleUrl: './checkout.scss'
})
export class CheckoutPage implements OnInit {
  checkoutForm: FormGroup;
  cartItems: CartItem[] = [];
  loading = false;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.checkoutForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      phone: ['', [Validators.required, Validators.pattern(/^[+]?[\d\s-]+$/)]],
      address: ['', [Validators.required, Validators.minLength(10)]],
      city: ['', [Validators.required]],
      postalCode: ['', [Validators.required, Validators.pattern(/^\d{5}$/)]],
      country: ['France', [Validators.required]],
      paymentMethod: [PaymentMethod.CASH_ON_DELIVERY, [Validators.required]],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.loading = true;
    this.cartService.loadCart().subscribe({
      next: (summary) => {
        this.cartItems = summary.items;
        this.loading = false;
        
        if (this.cartItems.length === 0) {
          this.snackBar.open('Votre panier est vide', 'Fermer', { duration: 3000 });
          this.router.navigate(['/products']);
        }
      },
      error: (error) => {
        console.error('Error loading cart:', error);
        this.loading = false;
        this.snackBar.open('Erreur lors du chargement du panier', 'Fermer', { duration: 3000 });
      }
    });
  }

  getTotal(): number {
    return this.cartService.getCartTotal();
  }

  getShippingCost(): number {
    return 0; // Livraison gratuite
  }

  getTax(): number {
    return 0; // Pas de taxe pour l'instant
  }

  getFinalTotal(): number {
    return this.getTotal() + this.getShippingCost() + this.getTax();
  }

  onSubmit(): void {
    if (this.checkoutForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs requis', 'Fermer', { duration: 3000 });
      return;
    }

    if (this.cartItems.length === 0) {
      this.snackBar.open('Votre panier est vide', 'Fermer', { duration: 3000 });
      return;
    }

    this.submitting = true;

    const user = JSON.parse(localStorage.getItem('current_user') || '{}');
    const formValue = this.checkoutForm.value;

    // Convertir les CartItems en OrderItems
    const orderItems: OrderItem[] = this.cartItems.map(item => ({
      productId: item.productId,
      productName: item.productName,
      productImage: item.productImage,
      quantity: item.quantity,
      unitPrice: item.productPrice,
      sellerId: item.sellerId,
      sellerName: item.sellerName
    }));

    const orderRequest: CreateOrderRequest = {
      userId: user.id || user.userId,
      userName: user.name,
      userEmail: user.email,
      items: orderItems,
      paymentMethod: formValue.paymentMethod,
      shippingAddress: {
        fullName: formValue.fullName,
        phone: formValue.phone,
        address: formValue.address,
        city: formValue.city,
        postalCode: formValue.postalCode,
        country: formValue.country
      },
      shippingCost: this.getShippingCost(),
      tax: this.getTax(),
      notes: formValue.notes
    };

    this.orderService.createOrder(orderRequest).subscribe({
      next: (order) => {
        // Vider le panier après commande réussie
        this.cartService.clearCart().subscribe();
        
        this.submitting = false;
        this.snackBar.open(`Commande ${order.orderNumber} créée avec succès !`, 'Fermer', { duration: 5000 });
        this.router.navigate(['/orders']);
      },
      error: (error) => {
        console.error('Error creating order:', error);
        this.submitting = false;
        this.snackBar.open('Erreur lors de la création de la commande', 'Fermer', { duration: 3000 });
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/cart']);
  }
}

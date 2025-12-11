import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { Router } from '@angular/router';
import { OrderService } from '../../core/services/order.service';
import { Auth } from '../../core/services/auth';
import { Order, OrderStatus } from '../../core/models/order.model';

@Component({
  selector: 'app-seller-orders',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatToolbarModule,
    MatSelectModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatMenuModule,
    MatBadgeModule
  ],
  templateUrl: './seller-orders.html',
  styleUrl: './seller-orders.scss'
})
export class SellerOrdersPage implements OnInit {
  orders = signal<Order[]>([]);
  filteredOrders = signal<Order[]>([]);
  loading = signal(true);
  selectedStatus = signal<string>('ALL');
  
  readonly statusOptions = [
    { value: 'ALL', label: 'Toutes les commandes' },
    { value: OrderStatus.PENDING, label: 'En attente' },
    { value: OrderStatus.CONFIRMED, label: 'Confirmées' },
    { value: OrderStatus.SHIPPED, label: 'Expédiées' },
    { value: OrderStatus.DELIVERED, label: 'Livrées' },
    { value: OrderStatus.CANCELLED, label: 'Annulées' }
  ];

  constructor(
    private orderService: OrderService,
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSellerOrders();
  }

  loadSellerOrders(): void {
    this.loading.set(true);
    const currentUser = this.authService.getCurrentUser();
    
    if (!currentUser) {
      this.router.navigate(['/login']);
      return;
    }

    this.orderService.getSellerOrders(currentUser.id).subscribe({
      next: (orders) => {
        this.orders.set(orders);
        this.filterOrders();
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des commandes:', error);
        this.loading.set(false);
      }
    });
  }

  filterOrders(): void {
    const status = this.selectedStatus();
    if (status === 'ALL') {
      this.filteredOrders.set(this.orders());
    } else {
      this.filteredOrders.set(
        this.orders().filter(order => order.status === status)
      );
    }
  }

  onStatusFilterChange(status: string): void {
    this.selectedStatus.set(status);
    this.filterOrders();
  }

  updateOrderStatus(orderId: string, newStatus: OrderStatus): void {
    this.orderService.updateOrderStatus(orderId, { status: newStatus }).subscribe({
      next: (updatedOrder) => {
        // Mettre à jour la commande dans la liste
        const currentOrders = this.orders();
        const index = currentOrders.findIndex(o => o.id === orderId);
        if (index !== -1) {
          currentOrders[index] = updatedOrder;
          this.orders.set([...currentOrders]);
          this.filterOrders();
        }
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour du statut:', error);
        alert('Erreur lors de la mise à jour du statut de la commande');
      }
    });
  }

  getStatusLabel(status: OrderStatus): string {
    const statusMap: Record<OrderStatus, string> = {
      [OrderStatus.PENDING]: 'En attente',
      [OrderStatus.CONFIRMED]: 'Confirmée',
      [OrderStatus.SHIPPED]: 'Expédiée',
      [OrderStatus.DELIVERED]: 'Livrée',
      [OrderStatus.CANCELLED]: 'Annulée'
    };
    return statusMap[status] || status;
  }

  getStatusColor(status: OrderStatus): string {
    const colorMap: Record<OrderStatus, string> = {
      [OrderStatus.PENDING]: 'warn',
      [OrderStatus.CONFIRMED]: 'primary',
      [OrderStatus.SHIPPED]: 'accent',
      [OrderStatus.DELIVERED]: 'primary',
      [OrderStatus.CANCELLED]: ''
    };
    return colorMap[status] || '';
  }

  getStatusIcon(status: OrderStatus): string {
    const iconMap: Record<OrderStatus, string> = {
      [OrderStatus.PENDING]: 'schedule',
      [OrderStatus.CONFIRMED]: 'check_circle',
      [OrderStatus.SHIPPED]: 'local_shipping',
      [OrderStatus.DELIVERED]: 'done_all',
      [OrderStatus.CANCELLED]: 'cancel'
    };
    return iconMap[status] || 'help';
  }

  canConfirm(order: Order): boolean {
    return order.status === OrderStatus.PENDING;
  }

  canShip(order: Order): boolean {
    return order.status === OrderStatus.CONFIRMED;
  }

  canDeliver(order: Order): boolean {
    return order.status === OrderStatus.SHIPPED;
  }

  canCancel(order: Order): boolean {
    return order.status === OrderStatus.PENDING || order.status === OrderStatus.CONFIRMED;
  }

  confirmOrder(orderId: string): void {
    this.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
  }

  shipOrder(orderId: string): void {
    this.updateOrderStatus(orderId, OrderStatus.SHIPPED);
  }

  deliverOrder(orderId: string): void {
    this.updateOrderStatus(orderId, OrderStatus.DELIVERED);
  }

  cancelOrder(orderId: string): void {
    if (confirm('Êtes-vous sûr de vouloir annuler cette commande ?')) {
      this.updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
  }

  getTotalRevenue(): number {
    return this.filteredOrders().reduce((sum, order) => {
      if (order.status !== OrderStatus.CANCELLED && order.totalAmount) {
        return sum + order.totalAmount;
      }
      return sum;
    }, 0);
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}

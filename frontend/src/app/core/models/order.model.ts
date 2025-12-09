export interface OrderItem {
  productId: string;
  productName: string;
  productImage: string | null;
  quantity: number;
  unitPrice: number;
  subtotal?: number;
  sellerId: string;
  sellerName: string;
}

export interface ShippingAddress {
  fullName: string;
  phone: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export enum PaymentMethod {
  CASH_ON_DELIVERY = 'CASH_ON_DELIVERY',
  CARD = 'CARD',
  PAYPAL = 'PAYPAL'
}

export enum PaymentStatus {
  PENDING = 'PENDING',
  PAID = 'PAID',
  FAILED = 'FAILED'
}

export interface Order {
  id?: string;
  orderNumber?: string;
  userId: string;
  userName: string;
  userEmail: string;
  items: OrderItem[];
  subtotal?: number;
  shippingCost: number;
  tax: number;
  totalAmount?: number;
  status?: OrderStatus;
  paymentMethod: PaymentMethod;
  paymentStatus?: PaymentStatus;
  shippingAddress: ShippingAddress;
  createdAt?: string;
  updatedAt?: string;
  confirmedAt?: string;
  shippedAt?: string;
  deliveredAt?: string;
  cancelledAt?: string;
  notes?: string;
  cancellationReason?: string;
}

export interface CreateOrderRequest {
  userId: string;
  userName: string;
  userEmail: string;
  items: OrderItem[];
  paymentMethod: PaymentMethod;
  shippingAddress: ShippingAddress;
  shippingCost: number;
  tax: number;
  notes?: string;
}

export interface UpdateOrderStatusRequest {
  status: OrderStatus;
}

export interface CancelOrderRequest {
  reason: string;
}

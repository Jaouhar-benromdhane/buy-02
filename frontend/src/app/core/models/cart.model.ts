export interface CartItem {
  id?: string; // ID du cart item dans MongoDB
  userId: string;
  productId: string;
  productName: string;
  productImage: string | null;
  productPrice: number;
  quantity: number;
  sellerId: string;
  sellerName: string;
  addedAt?: string;
  updatedAt?: string;
  stock?: number; // Pour validation frontend
}

export interface CartSummary {
  items: CartItem[];
  totalItems: number;
  totalAmount: number;
}

export interface AddToCartRequest {
  userId: string;
  productId: string;
  productName: string;
  productImage: string | null;
  productPrice: number;
  quantity: number;
  sellerId: string;
  sellerName: string;
}

export interface UpdateCartQuantityRequest {
  quantity: number;
}

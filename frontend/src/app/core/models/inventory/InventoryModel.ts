export interface InventoryModel {
  id: number;
  productId: string;
  productSku: string;
  productName: string;
  locationId: number;
  locationCode: string;
  locationName: string;
  quantity: number;
  createdAt: string;
  updatedAt: string;
}

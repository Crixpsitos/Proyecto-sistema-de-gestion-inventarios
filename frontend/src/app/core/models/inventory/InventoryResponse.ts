import { InventoryModel } from '@/core/models/inventory/InventoryModel';

export interface InventoryResponse {
  content: InventoryModel[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}

export interface MovementCreateRequest {
  type: 'IN' | 'OUT' | 'TRANSFER';
  productId: string;
  quantity: number;
  sourceId: string | null;
  destinationId: string | null;
  notes: string | null;
}

export interface DashboardOverview {
  totalProducts: number;
  totalCategories: number;
  totalLocations: number;
  totalStock: number;
  totalMovements: number;
  lastMovementAt: string | null;
}

export interface DashboardMovementMetrics {
  today: number;
  last7Days: number;
  last7DaysIn: number;
  last7DaysOut: number;
  last7DaysTransfer: number;
}

export interface DashboardTopProduct {
  productId: string;
  name: string;
  sku: string;
  totalQuantity: number;
  totalMovements: number;
}

export interface DashboardStockByLocation {
  locationId: number;
  locationName: string;
  locationCode: string;
  totalStock: number;
}

export interface DashboardAlert {
  productId: string;
  name: string;
  sku: string;
  totalStock: number;
  threshold: number;
}

export interface DashboardModel {
  overview: DashboardOverview;
  movementMetrics: DashboardMovementMetrics;
  topProducts: DashboardTopProduct[];
  stockByLocation: DashboardStockByLocation[];
  alerts: DashboardAlert[];
}

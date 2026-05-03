export enum LocationType {
  WAREHOUSE = 'WAREHOUSE',
  STORE = 'STORE',
  VIRTUAL = 'VIRTUAL'
}

export const LOCATION_TYPE_LABELS: Record<LocationType, string> = {
  [LocationType.WAREHOUSE]: 'Bodega',
  [LocationType.STORE]: 'Tienda',
  [LocationType.VIRTUAL]: 'Virtual'
};

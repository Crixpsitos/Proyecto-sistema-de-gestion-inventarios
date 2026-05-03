export interface LocationRequest {
  name: string;
  code: string;
  type: LocationType;
  address: string;
  active: boolean;
}

export enum LocationType {
  WAREHOUSE = 'WAREHOUSE',
  STORE = 'STORE',
  OFFICE = 'OFFICE'
}

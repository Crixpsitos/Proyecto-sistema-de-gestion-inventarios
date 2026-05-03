import { LocationModel } from '@/core/models/location/LocationModel';

export interface LocationResponse {
  content: LocationModel[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}

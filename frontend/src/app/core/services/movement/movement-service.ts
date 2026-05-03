import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@env/environment.development';
import { MovementFilters } from '@/core/models/movement/MovementFilters';
import { Observable } from 'rxjs';
import { MovementResponse } from '@/core/models/movement/MovementResponse';
import { MovementCreateRequest } from '@/core/models/movement/MovementCreateRequest';
import { MovementModel } from '@/core/models/movement/MovementModel';

@Injectable()
export class MovementService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api/movements`;

  public getMovements(
    filters: MovementFilters,
    page: number,
    size: number,
  ): Observable<MovementResponse> {
    let params = new HttpParams();
    if (filters.type) params = params.set('type', filters.type);
    if (filters.productId) params = params.set('productId', filters.productId.toString());
    if (filters.locationId) params = params.set('locationId', filters.locationId.toString());
    if (filters.dateFrom) params = params.set('dateFrom', filters.dateFrom);
    if (filters.dateTo) params = params.set('dateTo', filters.dateTo);
    params = params.set('page', page.toString())
                   .set('size', size.toString());

    return this.http.get<MovementResponse>(this.baseUrl, { params });
  }

  public createMovement(movementData: MovementCreateRequest): Observable<MovementModel> {
    return this.http.post<MovementModel>(this.baseUrl, movementData);
  }


}

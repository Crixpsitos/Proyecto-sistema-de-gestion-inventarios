import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { InventoryResponse } from '../../models/inventory/InventoryResponse';

@Injectable()
export class InventoryService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/api/inventory`;

  getAll(page: number, size: number): Observable<InventoryResponse> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<InventoryResponse>(this.baseUrl, { params });
  }

  search(page: number, size: number, term: string): Observable<InventoryResponse> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('q', term);
    return this.http.get<InventoryResponse>(`${this.baseUrl}/search`, { params });
  }
}

import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LocationModel } from '../../models/location/LocationModel';
import { LocationRequest } from '../../models/location/LocationRequest';
import { LocationResponse } from '../../models/location/LocationResponse';
import { environment } from '../../../../environments/environment';

@Injectable()
export class LocationService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/api/locations`;

  getAll(page: number, size: number, type: string, onlyActive: boolean): Observable<LocationResponse> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('type', type)
      .set('onlyActive', onlyActive ? 'true' : 'false');
    return this.http.get<LocationResponse>(this.baseUrl, { params });
  }

  getById(id: number): Observable<LocationModel> {
    return this.http.get<LocationModel>(`${this.baseUrl}/${id}`);
  }

  create(location: LocationRequest): Observable<LocationModel> {
    console.log("location", location);
    return this.http.post<LocationModel>(this.baseUrl, location);
  }

  update(id: number, location: LocationRequest): Observable<LocationModel> {
    return this.http.patch<LocationModel>(`${this.baseUrl}/${id}`, location);
  }

  deactivate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/deactivate`, {});
  }

  activate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${id}/activate`, {});
  }

  isCodeAvailable(code: string): Observable<{ available: boolean }> {
    const params = new HttpParams().set('code', code);
    return this.http.get<{ available: boolean }>(`${this.baseUrl}/check-code`, { params });
  }

  getLocationTypes(): Observable<{ value: string, label: string }[]> {
    return this.http.get<{ value: string, label: string }[]>(`${this.baseUrl}/types`);
  }

  search(term: string, page: number, size: number): Observable<LocationResponse> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('q', term);
    return this.http.get<LocationResponse>(`${this.baseUrl}/search`, { params });
  }
}

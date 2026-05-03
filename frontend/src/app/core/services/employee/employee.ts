import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@env/environment.development';
import { EmployeeResponse } from '@/core/models/employee/EmployeeResponse';
import { EmployeeModel } from '@/core/models/employee/EmployeeModel';
import { EmployeeCreateRequest } from '@/core/models/employee/EmployeeCreateRequest';
import { EmployeeUpdateRequest } from '@/core/models/employee/EmployeeUpdateRequest';
import { EmployeePermissionsRequest } from '@/core/models/employee/EmployeePermissionsRequest';

@Injectable()
export class EmployeeService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/api/employees`;

  public search(page: number, size: number, search: string): Observable<EmployeeResponse> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('search', search || '');

    return this.http.get<EmployeeResponse>(this.baseUrl, { params });
  }

  public findById(id: number): Observable<EmployeeModel> {
    return this.http.get<EmployeeModel>(`${this.baseUrl}/${id}`);
  }

  public create(payload: EmployeeCreateRequest): Observable<EmployeeModel> {
    return this.http.post<EmployeeModel>(this.baseUrl, payload);
  }

  public updateProfile(id: number, payload: EmployeeUpdateRequest): Observable<EmployeeModel> {
    return this.http.patch<EmployeeModel>(`${this.baseUrl}/${id}`, payload);
  }

  public updatePermissions(id: number, payload: EmployeePermissionsRequest): Observable<EmployeeModel> {
    return this.http.patch<EmployeeModel>(`${this.baseUrl}/${id}/permissions`, payload);
  }

  public activate(id: number): Observable<EmployeeModel> {
    return this.http.patch<EmployeeModel>(`${this.baseUrl}/${id}/activate`, {});
  }

  public deactivate(id: number): Observable<EmployeeModel> {
    return this.http.patch<EmployeeModel>(`${this.baseUrl}/${id}/deactivate`, {});
  }
}

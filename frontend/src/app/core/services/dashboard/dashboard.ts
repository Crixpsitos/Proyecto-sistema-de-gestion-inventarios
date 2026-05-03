import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@env/environment.development';
import { DashboardModel } from '@/core/models/dashboard/Dashboard';

@Injectable()
export class DashboardService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/api/dashboard`;

  public getDashboard(topProductsLimit = 5, lowStockThreshold = 10): Observable<DashboardModel> {
    return this.http.get<DashboardModel>(this.baseUrl, {
      params: {
        topProductsLimit,
        lowStockThreshold,
      },
    });
  }
}

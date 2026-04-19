import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { CategoryResponse } from '../../models/category/CategoryResponse';
import { Category as CategoryModel } from '../../models/category/Category';

@Injectable({
  providedIn: 'root',
})
export class Category {
  private httpClient = inject(HttpClient);

  public getCategories(page: number, size: number): Observable<CategoryResponse> {
    return this.httpClient.get<CategoryResponse>(`${environment.apiUrl}/api/categories?page=${page}&size=${size}`);
  }

  public getSearchCategories(page: number, size: number, term: string): Observable<CategoryResponse> {
    return this.httpClient.get<CategoryResponse>(`${environment.apiUrl}/api/categories/search?q=${term}&page=${page}&size=${size}`);
  }


  public createCategory(name: string, description: string): Observable<CategoryResponse> {
    return this.httpClient.post<CategoryResponse>(`${environment.apiUrl}/api/categories`, {name, description});
  }

  public updateCategory(id: number, name: string, description: string): Observable<CategoryResponse> {
    return this.httpClient.put<CategoryResponse>(`${environment.apiUrl}/api/categories/${id}`, {name, description});
  }

  public getCategoryById(id: number): Observable<CategoryModel> {
    return this.httpClient.get<CategoryModel>(`${environment.apiUrl}/api/categories/${id}`);
  }

  public deleteCategory(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${environment.apiUrl}/api/categories/${id}`);
  }
}

import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductResponse } from '@/core/models/Product/ProductResponse';
import { environment } from '@env/environment.development';
import { Product as ProductModel } from '@/core/models/Product/Product';

@Injectable()
export class Product {

  private http = inject(HttpClient);

  public getAllProducts(page: number, size: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${environment.apiUrl}/api/products?page=${page}&size=${size}`);
  }

  public createProduct(product: FormData): Observable<ProductModel> {
    return this.http.post<ProductModel>(`${environment.apiUrl}/api/products`, product);
  }

  public searchProducts(page: number, size: number, search: string): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${environment.apiUrl}/api/products/search?page=${page}&size=${size}&search=${search}`);
  }

  public getProductsByCategory(page: number, size: number, categoryId: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${environment.apiUrl}/api/products/category/${categoryId}?page=${page}&size=${size}`);
  }

  public getProductById(id: string): Observable<ProductModel> {
    return this.http.get<ProductModel>(`${environment.apiUrl}/api/products/${id}`);
  }

  public updateProduct(id: string, product: FormData): Observable<ProductModel> {
    return this.http.put<ProductModel>(`${environment.apiUrl}/api/products/${id}`, product);
  }

}

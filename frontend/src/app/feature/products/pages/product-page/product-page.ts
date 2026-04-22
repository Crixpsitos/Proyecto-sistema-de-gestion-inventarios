import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute, Router } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';
import { switchMap } from 'rxjs/operators';

import { Product } from '../../../../core/services/product/product';
import { Product as ProductModel } from '../../../../core/models/Product/Product';
import { ProductTableComponent } from '../../components/product-table/product-table';
import { Paginator } from '../../../shared/component/paginator/paginator';
import { ToolbarProduct } from '../../components/toolbar-product/toolbar-product';

@Component({
  selector: 'app-product-page',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, ProductTableComponent, Paginator, ToolbarProduct],
  templateUrl: './product-page.html',
  styleUrl: './product-page.css',
  providers: [Product],
})
export class ProductPage implements OnInit {
  private productService = inject(Product);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private snackBar = inject(MatSnackBar);

  public totalElements = signal(0);
  public loadingData = signal(true);
  public search = signal('');

  public currentPage = signal(0);
  public pageSize = signal(10);

  public products = signal<ProductModel[]>([]);

  ngOnInit(): void {
    // 1. Escuchamos los Query Params
    this.route.queryParams
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        // 2. Usamos switchMap para gestionar las llamadas HTTP sin anidar .subscribe()
        switchMap((params) => {
          const page = params['page'] ? Number(params['page']) : 0;
          const size = params['size'] ? Number(params['size']) : 10;
          const search = params['search'] ? params['search'] : '';

          this.currentPage.set(page);
          this.pageSize.set(size);
          this.search.set(search);

          this.loadingData.set(true);

          // 3. Retornamos el Observable correspondiente
          if (search) {
            return this.productService.searchProducts(page, size, search);
          } else {
            return this.productService.getAllProducts(page, size);
          }
        })
      )

      .subscribe({
        next: (response) => {
          console.log(response);
          this.products.set(response.content);
          this.totalElements.set(response.totalElements);
          this.loadingData.set(false);
        },
        error: () => {
          this.loadingData.set(false);
          this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 });
        },
      });
  }

  public navigateToAddProduct(): void {
    this.router.navigate(['/añadir-producto']);
  }

  public handlePagination(event: PageEvent) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: event.pageIndex,
        size: event.pageSize,
      },
      queryParamsHandling: 'merge',
    });
  }

  public handleSearch(term: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        search: term || null,
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }
}

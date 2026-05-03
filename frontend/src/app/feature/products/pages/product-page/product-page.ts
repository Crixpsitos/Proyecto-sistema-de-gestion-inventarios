import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { PageEvent } from '@angular/material/paginator';
import { switchMap, catchError } from 'rxjs/operators'; // <-- IMPORTA catchError
import { of } from 'rxjs';

import { Product } from '../../../../core/services/product/product';
import { Product as ProductModel } from '../../../../core/models/Product/Product';
import { Category as CategoryModel } from '../../../../core/models/category/Category';
import { ProductTableComponent } from '../../components/product-table/product-table';
import { Paginator } from '../../../shared/component/paginator/paginator';
import { ToolbarProduct } from '../../components/toolbar-product/toolbar-product';
import { MovementFormComponent } from '../../../movement/components/movement-form-component/movement-form-component';
import { User } from '../../../../core/services/user/user';


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
  private dialog = inject(MatDialog);
  private userService = inject(User);

  public totalElements = signal(0);
  public loadingData = signal(true);
  public search = signal('');
  public categoryId = signal<number | null>(null);
  public currentPage = signal(0);
  public pageSize = signal(10);
  public products = signal<ProductModel[]>([]);
  public canManageProducts = signal(false);

  constructor() {
    this.canManageProducts.set(this.userService.hasRole('ADMIN', 'MANAGER'));
  }

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        switchMap((params) => {
          const page = params['page'] ? Number(params['page']) : 0;
          const size = params['size'] ? Number(params['size']) : 10;
          const search = params['search'] ? params['search'] : '';
          const category = params['category'] ? Number(params['category']) : null;

          this.currentPage.set(page);
          this.pageSize.set(size);
          this.search.set(search);
          this.categoryId.set(category);

          this.loadingData.set(true);

          let request$;

          if (search) {
            request$ = this.productService.searchProducts(page, size, search);
          } else if (category) {
            request$ = this.productService.getProductsByCategory(page, size, category);
          } else {
            request$ = this.productService.getAllProducts(page, size);
          }

          return request$.pipe(
            catchError((error) => {
              this.loadingData.set(false);
              this.snackBar.open('Error al cargar productos', 'Cerrar', { duration: 3000 });
              // Retornamos of(null) para mantener el flujo vivo
              return of(null);
            })
          );
        }),
      )
      .subscribe({
        next: (response) => {
          if (response) {
            this.products.set(response.content);
            this.totalElements.set(response.totalElements);
            this.loadingData.set(false);
          }
        }
      });
  }

  public navigateToAddProduct(): void {
    this.router.navigate(['/añadir-producto']);
  }

  public navigateToEditProduct(id: string): void {
    this.router.navigate(['/productos', id, 'editar']);
  }

  public handleQuickMovement(product: ProductModel): void {
    this.dialog.open(MovementFormComponent, {
      width: '500px',
      disableClose: true,
      data: { productId: product.id }
    });
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
        category: null,
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }

  public handleCategoryChange(category: CategoryModel | null): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        category: category?.id || null,
        search: null,
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }
}

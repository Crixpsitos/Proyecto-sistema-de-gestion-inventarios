import { ChangeDetectionStrategy, Component, DestroyRef, inject, signal, OnInit } from '@angular/core';
import { ToolbarCategorie } from "@/feature/categories/components/toolbar-categorie/toolbar-categorie";
import { TableCategorie } from "@/feature/categories/components/table-categorie/table-categorie";
import { Category } from '@/core/services/category/category';
import { Category as CategoryModel } from '@/core/models/category/Category';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PageEvent } from '@angular/material/paginator';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from '@/core/user/services/user.service';

@Component({
  selector: 'app-categorie-page',
  imports: [ToolbarCategorie, TableCategorie],
  templateUrl: './categorie-page.html',
  styleUrl: './categorie-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CategoriePage implements OnInit {

  private categoryService = inject(Category);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private snackBar = inject(MatSnackBar);
  private userService = inject(User);

  public categories = signal<CategoryModel[]>([]);
  public totalElements = signal(0);
  public loadingData = signal(true);
  public search = signal('');

  public currentPage = signal(0);
  public pageSize = signal(10);
  public canManageCategories = signal(false);

  constructor() {
    this.canManageCategories.set(this.userService.hasRole('ADMIN', 'MANAGER'));
  }

  ngOnInit() {
    this.route.queryParams
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params) => {
        const page = params['page'] ? Number(params['page']) : 0;
        const size = params['size'] ? Number(params['size']) : 10;
        const search = params['search'] ? params['search'] : '';

        this.currentPage.set(page);
        this.pageSize.set(size);
        this.search.set(search);

        this.loadData(page, size, search);
      });
  }

  private loadData(page: number, size: number, search: string) {
    this.loadingData.set(true);

    if (!search) {
      this.categoryService.getCategories(page, size).subscribe({
        next: (data) => {
          this.categories.set(data.content);
          this.totalElements.set(data.totalElements);
          this.loadingData.set(false);
        },
        error: (err: HttpErrorResponse) => {
          this.loadingData.set(false);
          this.snackBar.open(err.message, 'Cerrar', { duration: 4000 });
        }
      });
    } else {
      this.categoryService.getSearchCategories(page, size, search).subscribe({
        next: (data) => {
          this.categories.set(data.content);
          this.totalElements.set(data.totalElements);
          this.loadingData.set(false);
        },
        error: (err: HttpErrorResponse) => {
          this.loadingData.set(false);
          this.snackBar.open(err.message, 'Cerrar', { duration: 4000 });
        }
      });
    }
  }

  public handlePagination(event: PageEvent) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: event.pageIndex,
        size: event.pageSize
      },
      queryParamsHandling: 'merge'
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

  public handleAddCategory(data: {name: string, description: string}) {
    this.categoryService.createCategory(data.name, data.description).subscribe({
      next: () => {
        this.snackBar.open('Categoría creada exitosamente', 'Cerrar', { duration: 4000 });

        if (this.currentPage() === 0) {
          this.loadData(0, this.pageSize(), this.search());
        } else {
          this.router.navigate([], {
            relativeTo: this.route,
            queryParams: { page: 0 },
            queryParamsHandling: 'merge',
          });
        }
      },
      error: (err: HttpErrorResponse) => {
        this.loadingData.set(false);
        this.snackBar.open(err.message, 'Cerrar', { duration: 4000 });
      }
    });
  }

  public handleEditCategory(category: CategoryModel): void {
    this.router.navigate(['/categorias', category.id, 'editar']);
  }

}

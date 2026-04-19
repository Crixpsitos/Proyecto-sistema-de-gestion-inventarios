import { ChangeDetectionStrategy, Component, inject, input, output } from '@angular/core';
import { Category } from '../../../../core/models/category/Category';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-table-categorie',
  imports: [MatTableModule, MatIconModule, MatProgressSpinnerModule, MatPaginatorModule],
  templateUrl: './table-categorie.html',
  styleUrl: './table-categorie.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TableCategorie {
  private router = inject(Router);

  public categoriesData = input<Category[]>([]);
  public loading = input<boolean>(false);


  public pageSizeOptions = [5, 10, 25];
  public pageSize = input<number>(10);
  public currentPage = input<number>(0);
  public totalElements = input<number>(0);
  public pageEvent = output<PageEvent>();

  public deleteCategory = output<number>();

  public handleDeleteCategory(id: number): void {
    this.deleteCategory.emit(id);
  }

  public handlePageEvent(event: PageEvent) {
    this.pageEvent.emit(event);
  }

  public handleEditCategory(category: Category): void {
    this.router.navigate(['/categorias', category.id, 'editar']);
  }



}

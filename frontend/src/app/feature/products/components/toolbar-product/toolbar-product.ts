import { Component, inject, OnInit, output, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { SelectCategory } from '../select-category/select-category';
import { Category } from '../../../../core/models/category/Category';
import { Category as ModelCategory } from '../../../../core/models/category/Category';
import { Category as CategoryService } from '../../../../core/services/category/category';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-product-toolbar',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './toolbar-product.html',
  styleUrls: ['./toolbar-product.css']
})
export class ToolbarProduct implements OnInit {
  search = output<string>();
  categorySelect = output<Category | null>();

  categoryControl = new FormControl<number | null>(null);

  onSearch(event: Event): void {
    const element = event.target as HTMLInputElement;
    this.search.emit(element.value);
  }

  onCategoryChange(category: Category | null): void {
    console.log("esta pasando la categoria", category);
    this.categorySelect.emit(category);
  }

  private categoryService = inject(CategoryService);


  public categories = signal<ModelCategory[]>([]);
  public currentPage = signal<number>(0);
  public isLoading = signal<boolean>(false);
  public totalPages = signal<number>(0);
  public hasMorePages = signal<boolean>(true);

  ngOnInit(): void {
    this.loadCategories();

    // Si quieres que el FormControl devuelva eventos automáticamente:
    this.categoryControl.valueChanges.subscribe(selectedId => {
      const category = this.categories().find(c => c.id === selectedId) || null;
      this.categorySelect.emit(category);
    });
  }

  public loadCategories(event?: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    if (!this.hasMorePages() || this.isLoading()) return;

    this.isLoading.set(true);
    this.fetchCategoriesFromApi(this.currentPage());
  }


  public onCategorySelected(): void {
    const selectedId = this.categoryControl.value;
    const selectedCategory = this.categories().find(category => category.id === selectedId) || null;
    // Asegúrate de emitir al output que el ProductPage está escuchando
    this.categorySelect.emit(selectedCategory);
  }

  private fetchCategoriesFromApi(page: number): void {
    this.categoryService.getCategories(page, 5).subscribe({
      next: (response) => {
        this.categories.update(current => [...current, ...response.content]);
        this.totalPages.set(response.totalPages);
        this.currentPage.update(p => p + 1);

        if (this.currentPage() >= this.totalPages()) {
          this.hasMorePages.set(false);
        }

        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }
}

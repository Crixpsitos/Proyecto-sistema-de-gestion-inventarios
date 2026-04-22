import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { Category as ModelCategory } from '../../../../core/models/category/Category';
import { Category as CategoryService } from '../../../../core/services/category/category';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-select-category',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './select-category.html',
  styleUrl: './select-category.css',
})
export class SelectCategory implements OnInit {

  private categoryService = inject(CategoryService);

  public categoryControl = input.required<FormControl>();

  public categories = signal<ModelCategory[]>([]);
  public currentPage = signal<number>(0);
  public isLoading = signal<boolean>(false);
  public totalPages = signal<number>(0);
  public hasMorePages = signal<boolean>(true);

  public selectedCategory = output<ModelCategory | null>();

  ngOnInit(): void {
    this.loadCategories();
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
    const selectedId = this.categoryControl().value();
    const selectedCategory = this.categories().find(category => category.id === selectedId) || null;
    this.selectedCategory.emit(selectedCategory);
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

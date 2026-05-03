import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Category } from '@/core/services/category/category';

@Component({
  selector: 'app-edit-category-page',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './edit-category-page.html',
  styleUrl: './edit-category-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditCategoryPage {

  private categoryService = inject(Category);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  public loading = signal(true);
  public saving = signal(false);

  private categoryId!: number;

  public form = new FormGroup({
    name: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    description: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
  });

  ngOnInit(): void {
    this.categoryId = Number(this.route.snapshot.paramMap.get('id'));

    this.categoryService.getCategoryById(this.categoryId).subscribe({
      next: (category) => {
        this.form.setValue({ name: category.name, description: category.description });
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.snackBar.open(err.message, 'Cerrar', { duration: 4000 });
        this.router.navigate(['/categorias']);
      },
    });
  }

  public onSubmit(): void {
    if (this.form.invalid) return;

    this.saving.set(true);
    const { name, description } = this.form.getRawValue();

    this.categoryService.updateCategory(this.categoryId, name, description).subscribe({
      next: () => {
        this.snackBar.open('Categoría actualizada exitosamente', 'Cerrar', { duration: 4000 });
        this.router.navigate(['/categorias']);
      },
      error: (err: HttpErrorResponse) => {
        this.saving.set(false);
        this.snackBar.open(err.message, 'Cerrar', { duration: 4000 });
      },
    });
  }

  public onCancel(): void {
    this.router.navigate(['/categorias']);
  }

}

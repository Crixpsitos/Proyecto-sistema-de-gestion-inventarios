import { Component, inject, output } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, Validators } from '@angular/forms';
import { validatorImage } from '@/feature/products/validators/ValidatorImage';
import { MatIconModule } from '@angular/material/icon';
import { SelectCategory } from '@/feature/products/components/select-category/select-category';
import { Category } from '@/core/models/category/Category';
import { skuAsyncValidator } from '@/feature/products/validators/product-validators';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-form-product',
  imports: [MatInputModule, MatButtonModule, ReactiveFormsModule, MatIconModule, SelectCategory],
  templateUrl: './add-form-product.html',
  styleUrl: './add-form-product.css',
})
export class AddFormProduct {

  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private router = inject(Router);

  public submitEvent = output<Partial<{
    sku: string | null;
    name: string | null;
    description: string | null;
    price: number | null;
    image: null | File;
    brand: string | null;
    model: string | null;
    category: number | null;
    color: string | null;
    size: string | null;
    quantity: number | null;
    currency: string | null;
  }>>();


public form = this.fb.group({
    sku: ['', [Validators.required], skuAsyncValidator(this.http)],
    name: ['', [Validators.required]],
    description: [''],
    price: [0, [Validators.required, Validators.min(0)]],

    image: [null as File | null, [validatorImage]],

    brand: ['', [Validators.required]],
    model: ['', [Validators.required]],

    category: [null as number | null, [Validators.required, Validators.min(0)]],

    color: ['', [Validators.required]],
    size: ['', [Validators.required]],

    quantity: [0, [Validators.required, Validators.min(0)]],

    currency: ['COP', [Validators.required]],
  });

  public onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.form.patchValue({ image: file });
      this.form.get('image')?.markAsTouched();
    }
  }

  public onCategorySelected(category: Category | null): void {
    if (!category) return;
    this.form.patchValue({ category: category?.id });
    this.form.get('category')?.markAsTouched();
  }

  public onSubmit() {
    this.submitEvent.emit(this.form.value);
  }


  public navigateBack() {
    this.router.navigate(['/productos']);
  }

}

import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators, FormControl } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Product as ProductService } from '../../../../core/services/product/product';
import { SelectCategory } from '../../components/select-category/select-category';
import { Category } from '../../../../core/models/category/Category';
import { validatorImage } from '../../validators/ValidatorImage';
import { skuAsyncValidator } from '../../validators/product-validators';
import { HttpClient } from '@angular/common/http';
import { Location } from '@angular/common';

@Component({
  selector: 'app-edit-product',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    SelectCategory
  ],
  templateUrl: './edit-product.html',
  styleUrl: './edit-product.css',
  providers: [ProductService]
})
export class EditProduct implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private productService = inject(ProductService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);
  private http = inject(HttpClient);
  private location = inject(Location);

  public productId = signal<string | null>(null);
  public currentImageUrl = signal<string | null>(null);
  public isLoading = signal(false);
  public productCategory = signal<Category | null>(null);

  public form = this.fb.group({
    sku: ['', [Validators.required]],
    name: ['', [Validators.required]],
    description: [''],
    price: [0, [Validators.required, Validators.min(0)]],
    brand: ['', [Validators.required]],
    model: ['', [Validators.required]],
    category: [null as number | null, [Validators.required]],
    color: ['', [Validators.required]],
    size: ['', [Validators.required]],
    quantity: [0, [Validators.required, Validators.min(0)]],
    currency: ['COP', [Validators.required]],
    image: [null as File | null, [validatorImage]],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.productId.set(id);
      this.loadProduct(id);
    }
  }

  private loadProduct(id: string): void {
    this.isLoading.set(true);
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.productCategory.set(product.category);
        this.form.patchValue({
          sku: product.sku,
          name: product.name,
          description: product.description,
          price: product.price || 0,
          brand: product.brand,
          model: product.model,
          category: product.category?.id || null,
          color: product.color,
          size: product.size,
          quantity: product.quantity,
          currency: product.currency || 'COP'
        });

        const skuControl = this.form.get('sku');
        if (skuControl) {
          skuControl.setAsyncValidators([skuAsyncValidator(this.http, product.sku)]);
          skuControl.updateValueAndValidity({ emitEvent: false });
        }

        this.currentImageUrl.set(product.image);
        this.isLoading.set(false);
      },
      error: () => {
        this.snackBar.open('Error al cargar el producto', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/productos']);
      }
    });
  }

  public onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.form.patchValue({ image: file });
      const reader = new FileReader();
      reader.onload = () => this.currentImageUrl.set(reader.result as string);
      reader.readAsDataURL(file);
    }
  }

  public goBack(): void {
    this.location.back();
  }

  public onCategorySelected(category: Category | null): void {
    this.form.patchValue({ category: category?.id || null });
  }

  public getCategoryControl(): FormControl {
    return this.form.get('category') as FormControl;
  }

  public onSubmit(): void {
    if (this.form.invalid || !this.productId()) return;

    this.isLoading.set(true);

    const formData = new FormData();
    const productData = this.form.value;

    formData.append('sku', productData.sku || '');
    formData.append('name', productData.name || '');
    formData.append('description', productData.description || '');
    formData.append('price', (productData.price || 0).toString());
    formData.append('brand', productData.brand || '');
    formData.append('model', productData.model || '');
    formData.append('category', (productData.category || 0).toString());
    formData.append('color', productData.color || '');
    formData.append('size', productData.size || '');
    formData.append('quantity', (productData.quantity || 0).toString());
    formData.append('currency', productData.currency || '');

    if (productData.image) {
      formData.append('image', productData.image);
    }

    this.productService.updateProduct(this.productId()!, formData).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.snackBar.open('Producto actualizado con exito', 'Cerrar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
        });
        this.router.navigate(['/productos']);
      },
      error: (error) => {
        this.isLoading.set(false);
        console.error('Error al actualizar el producto:', error);
        this.snackBar.open('Error al actualizar el producto', 'Cerrar', {
          duration: 5000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
        });
      }
    });
  }
}

import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Product as ProductService } from '../../../../core/services/product/product';
import { AddFormProduct } from "../../components/add-form-product/add-form-product";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [AddFormProduct],
  templateUrl: './add-product.html',
  styleUrl: './add-product.css',
  providers: [ProductService]
})
export class AddProduct {

  private productService = inject(ProductService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  public isLoading = signal<boolean>(false);

  public addProduct(product: Partial<{
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
  }>) {


    this.isLoading.set(true);

    const formData = new FormData();

    formData.append('sku', product.sku || '');
    formData.append('name', product.name || '');
    formData.append('description', product.description || '');
    formData.append('price', (product.price || 0).toString());
    formData.append('brand', product.brand || '');
    formData.append('model', product.model || '');
    formData.append('category', (product.category || 0).toString());
    formData.append('color', product.color || '');
    formData.append('size', product.size || '');
    formData.append('quantity', (product.quantity || 0).toString());
    formData.append('currency', product.currency || '');

    if (product.image) {
      formData.append('image', product.image);
    }

    this.productService.createProduct(formData).subscribe({
      next: (response) => {
        this.isLoading.set(false);

        const createdId = response?.id || response?.sku || 'desconocido';


        this.snackBar.open(`✅ Producto con ID ${createdId} fue creado con éxito`, 'Cerrar', {
          duration: 4000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: ['bg-green-600', 'text-white']
        });


        this.router.navigate(['/productos']);
      },
      error: (error) => {
        this.isLoading.set(false);
        console.error('Error al crear el producto:', error);

        this.snackBar.open('❌ Hubo un error al crear el producto', 'Cerrar', {
          duration: 5000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: ['bg-red-600', 'text-white']
        });
      }
    });
  }
}

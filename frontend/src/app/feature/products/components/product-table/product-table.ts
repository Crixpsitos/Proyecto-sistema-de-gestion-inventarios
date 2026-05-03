import { ChangeDetectionStrategy, Component, inject, input, output } from '@angular/core';
import { CurrencyPipe } from '@angular/common';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';

import { Product } from '../../../../core/models/Product/Product'; // Ajusta la ruta
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-table',
  imports: [
    CurrencyPipe,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
  ],
  templateUrl: './product-table.html',
  styleUrl: './product-table.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductTableComponent {

  private router = inject(Router);

  public products = input.required<Product[]>();
  public canManage = input(false);
  public registerMovement = output<Product>();

  public displayedColumns: string[] = [
    'image',
    'sku',
    'name',
    'brand',
    'category',
    'price',
    'actions',
  ];
  public defaultImage = 'https://placehold.co/100x100/e0e0e0/757575?text=Sin+Imagen';

  public handleImageError(event: Event): void {
    (event.target as HTMLImageElement).src = this.defaultImage;
  }

  public navigateToEditProduct(id: string): void {
    this.router.navigate(['/productos', id, 'editar']);
  }

  public handleQuickMovement(product: Product): void {
    this.registerMovement.emit(product);
  }
}

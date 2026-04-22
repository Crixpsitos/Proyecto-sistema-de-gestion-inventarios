import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { CurrencyPipe } from '@angular/common';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';

import { Product } from '../../../../core/models/Product/Product'; // Ajusta la ruta

@Component({
  selector: 'app-product-table',
  imports: [
    CurrencyPipe,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatTooltipModule,
  ],
  templateUrl: './product-table.html',
  styleUrl: './product-table.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductTableComponent {
  public products = input.required<Product[]>();

  public displayedColumns: string[] = [
    'image',
    'sku',
    'name',
    'brand',
    'category',
    'stock',
    'price',
    'actions',
  ];
  public defaultImage = 'https://placehold.co/100x100/e0e0e0/757575?text=Sin+Imagen';

  public handleImageError(event: Event): void {
    (event.target as HTMLImageElement).src = this.defaultImage;
  }
}

import { Component, output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { SelectCategory } from '../select-category/select-category';

@Component({
  selector: 'app-product-toolbar',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    SelectCategory
  ],
  templateUrl: './toolbar-product.html',
  styleUrls: ['./toolbar-product.css']
})
export class ToolbarProduct {
  search = output<string>();
  categorySelect = output<any>();

  categoryControl = new FormControl(null);

  onSearch(event: Event): void {
    const element = event.target as HTMLInputElement;
    this.search.emit(element.value);
  }

  onCategoryChange(category: any): void {
    this.categorySelect.emit(category);
  }
}

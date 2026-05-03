import { Component, signal, output, inject, OnInit, input } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MovementFilters } from '@/core/models/movement/MovementFilters';
import { Product as ProductService } from '@/core/services/product/product';
import { LocationService } from '@/core/services/location/LocationService';
import { Product } from '@/core/models/Product/Product';
import { LocationModel } from '@/core/models/location/LocationModel';
import { DropdownInfiniteScroll } from '@/feature/shared/component/dropdown-infinite-scroll/dropdown-infinite-scroll';
import { FetchFn } from '@/feature/shared/interfaces/FetchFn';
import { map } from 'rxjs';
import { MapperFn } from '@/feature/shared/interfaces/MapperFn';

@Component({
  selector: 'app-movement-toolbar',
  imports: [
    ReactiveFormsModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    DropdownInfiniteScroll,
  ],
  templateUrl: './movement-toolbar.html',
  styleUrl: './movement-toolbar.css',
  providers: [ProductService, LocationService],
})
export class MovementToolbar implements OnInit {
  private fb = inject(FormBuilder);
  private productService = inject(ProductService);
  private locationService = inject(LocationService);

  filterForm!: FormGroup<{
    product: FormControl<string | null>;
    location: FormControl<number | null>;
    dateFrom: FormControl<Date | null>;
    dateTo: FormControl<Date | null>;
  }>;

  selectedType = signal<string | null>('ALL');

  currentFilters = input<MovementFilters | null>(null);
  filtersChanged = output<MovementFilters>();

ngOnInit() {
  this.filterForm = this.fb.group({
    product: [null],
    location: [null],
    dateFrom: [null],
    dateTo: [null],
  }) as FormGroup<{
    product: FormControl<string | null>;
    location: FormControl<number | null>;
    dateFrom: FormControl<Date | null>;
    dateTo: FormControl<Date | null>;
  }>;
  this.setInitialValues();

  this.filterForm.valueChanges.subscribe(() => {
    this.emitFilters();
  });
}

  selectType(type: string | null) {
    this.selectedType.set(type);
    this.emitFilters();
  }

  fetchLocations: FetchFn<LocationModel> = (page, pageSize) => {
    return this.locationService
      .getAll(page, pageSize, '', true)
      .pipe(map((response) => response.content));
  };

  locationMapper: MapperFn<LocationModel> = (item: LocationModel) => ({
    id: item.id,
    label: item.name,
  });

  fetchProducts: FetchFn<Product> = (page, pageSize) => {
    return this.productService
      .getAllProducts(page, pageSize)
      .pipe(map((response) => response.content));
  };

  productMapper: MapperFn<Product> = (item: Product) => ({
    id: item.id,
    label: item.name,
  });

  get productControl(): FormControl {
    return this.filterForm.get('product')! as FormControl;
  }

  get locationControl(): FormControl {
    return this.filterForm.get('location')! as FormControl;
  }

  private emitFilters() {
    const formValues = this.filterForm.getRawValue();

    const filterState: MovementFilters = {
      type: this.selectedType() === 'ALL' ? null : this.selectedType(),
      productId: formValues.product ? formValues.product : null,
      locationId: formValues.location ? Number(formValues.location) : null,
      dateFrom: formValues.dateFrom ? formValues.dateFrom.toISOString().split('T')[0] : null,
      dateTo: formValues.dateTo ? formValues.dateTo.toISOString().split('T')[0] : null,
    };
    this.filtersChanged.emit(filterState);
  }

   private setInitialValues(): void {
    const filters = this.currentFilters();
    if (!filters) return;


    if (filters.type) {
      this.selectedType.set(filters.type);
    }


    this.filterForm.patchValue({
      product: filters.productId ?? null,
      location: filters.locationId ? Number(filters.locationId) : null,
      dateFrom: filters.dateFrom ? new Date(filters.dateFrom) : null,
      dateTo: filters.dateTo ? new Date(filters.dateTo) : null,
    }, { emitEvent: false });
  }
}

import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { map } from 'rxjs';

import { DropdownInfiniteScroll } from '@/feature/shared/component/dropdown-infinite-scroll/dropdown-infinite-scroll';
import { FetchFn } from '@/feature/shared/interfaces/FetchFn';
import { MapperFn } from '@/feature/shared/interfaces/MapperFn';
import { Product as ProductService } from '@/core/services/product/product';
import { LocationService } from '@/core/services/location/LocationService';

import { Product } from '@/core/models/Product/Product';
import { LocationModel } from '@/core/models/location/LocationModel';
import { MovementService } from '@/core/services/movement/movement-service';
import { MovementCreateRequest } from '@/core/models/movement/MovementCreateRequest';

type MovementType = 'IN' | 'OUT' | 'TRANSFER';

@Component({
  selector: 'app-movement-form-component',
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    DropdownInfiniteScroll,
  ],
  templateUrl: './movement-form-component.html',
  styleUrl: './movement-form-component.css',
  providers: [ProductService, LocationService, MovementService],
})
export class MovementFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<MovementFormComponent>);
  private productService = inject(ProductService);
  private locationService = inject(LocationService);
  private movementService = inject(MovementService);
  private dialogData = inject<{ productId?: string } | null>(MAT_DIALOG_DATA, { optional: true });

  isLoading = signal(false);

  form = this.fb.group({
    type: new FormControl<MovementType | null>(null, Validators.required),
    product: new FormControl<string | null>(null, Validators.required),
    quantity: new FormControl<number | null>(null, [Validators.required, Validators.min(1)]),
    source: new FormControl<string | null>(null),
    destination: new FormControl<string | null>(null),
    notes: new FormControl<string | null>(null),
  });

  selectedType = signal<MovementType | null>(null);
  showSource = computed(() => {
    const type = this.selectedType();
    return type === 'OUT' || type === 'TRANSFER';
  });

  showDestination = computed(() => {
    const type = this.selectedType();
    return type === 'IN' || type === 'TRANSFER';
  });

  get productControl(): FormControl {
    return this.form.get('product') as FormControl;
  }

  get sourceControl(): FormControl {
    return this.form.get('source') as FormControl;
  }

  get destinationControl(): FormControl {
    return this.form.get('destination') as FormControl;
  }

  ngOnInit(): void {
    if (this.dialogData?.productId) {
      this.form.patchValue({ product: this.dialogData.productId });
      this.productControl.markAsTouched();
    }

    this.form.get('type')?.valueChanges.subscribe((type) => {
      this.selectedType.set(type);
      this.updateValidators(type);
    });
  }

  private updateValidators(type: MovementType | null): void {
    const source = this.form.get('source');
    const destination = this.form.get('destination');

    source?.reset();
    destination?.reset();

    if (type === 'OUT' || type === 'TRANSFER') {
      source?.setValidators(Validators.required);
    } else {
      source?.clearValidators();
    }

    if (type === 'IN' || type === 'TRANSFER') {
      destination?.setValidators(Validators.required);
    } else {
      destination?.clearValidators();
    }

    source?.updateValueAndValidity();
    destination?.updateValueAndValidity();
  }

  fetchProducts: FetchFn<Product> = (page, pageSize) =>
    this.productService.getAllProducts(page, pageSize).pipe(map((response) => response.content));

  productMapper: MapperFn<Product> = (item: Product) => ({
    id: item.id,
    label: item.name,
  });

  fetchLocations: FetchFn<LocationModel> = (page, pageSize) =>
    this.locationService.getAll(page, pageSize, '', true).pipe(map((response) => response.content));

  locationMapper: MapperFn<LocationModel> = (item: LocationModel) => ({
    id: item.id,
    label: item.name,
  });

  onSubmit(): void {
    console.log('Form value:', this.form.value);
    console.log('Form valid:', this.form.valid);
    console.log('Form errors:', this.form.errors);
    console.log('Type errors:', this.form.get('type')?.errors);
    console.log('Product errors:', this.form.get('product')?.errors);
    console.log('Quantity errors:', this.form.get('quantity')?.errors);
    console.log('Source errors:', this.form.get('source')?.errors);
    console.log('Destination errors:', this.form.get('destination')?.errors);
    console.log('Notes errors:', this.form.get('notes')?.errors);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    const values = this.form.value;

    const payload: MovementCreateRequest = {
      type: values.type!,
      productId: values.product!,
      quantity: values.quantity!,
      sourceId: values.source ?? null,
      destinationId: values.destination ?? null,
      notes: values.notes ?? null,
    };

    this.movementService.createMovement(payload).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        this.dialogRef.close(response);
      },
      error: (error) => {
        console.log('Error:', error);
        this.isLoading.set(false);
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }
}

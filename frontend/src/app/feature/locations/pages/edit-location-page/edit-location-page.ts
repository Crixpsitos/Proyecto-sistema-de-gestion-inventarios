import { Component, inject, OnInit, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

// Material Imports
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Location } from '@angular/common';

// Services & Models
import {
  form,
  required,
  FormField,
  FormRoot,
  disabled
} from '@angular/forms/signals';
import { LocationService } from '../../../../core/services/location/LocationService';
import { LocationRequest } from '../../../../core/models/location/LocationRequest';

@Component({
  selector: 'app-edit-location-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormRoot,
    FormField,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatSnackBarModule
  ],
  templateUrl: './edit-location-page.html',
  styleUrl: './edit-location-page.css',
  providers: [LocationService]
})
export class EditLocationPage implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private locationService = inject(LocationService);
  private snackBar = inject(MatSnackBar);
  private location = inject(Location);


  public locationId = signal<number | null>(null);
  public isLoading = signal<boolean>(true);
  public isSaving = signal<boolean>(false);

  public locationTypes = signal<{ value: string, label: string }[]>([]);

  // Modelo de datos vinculado al formulario
  public locationModel = signal({
    name: '',
    code: '',
    type: '',
    address: ''
  });

  // Definición del formulario mediante Signals
  public editForm = form(this.locationModel, (schema) => {
    required(schema.name, { message: 'El nombre es obligatorio' });
    required(schema.type, { message: 'El tipo es obligatorio' });
    required(schema.address, { message: 'La dirección es obligatoria' });
    disabled(schema.code);
  }, {
    submission: {
      action: async (f) => {
        if (!this.locationId()) return;

        this.isSaving.set(true);
        const request: LocationRequest = f().value() as LocationRequest;

        this.locationService.update(this.locationId()!, { ...request, active: true }).subscribe({
          next: () => {
            this.snackBar.open('Ubicación actualizada correctamente', 'Éxito', { duration: 3000 });
            this.router.navigate(['/locaciones']);
          },
          error: () => {
            this.isSaving.set(false);
            this.snackBar.open('Error al actualizar la ubicación', 'Error', { duration: 3000 });
          }
        });
      },
      onInvalid: (field) => {
        this.snackBar.open('Por favor, revisa los campos marcados en rojo', 'Cerrar', { duration: 3000 });
      }
    }
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.locationId.set(Number(id));
      this.loadLocationData(Number(id));
      this.locationService.getLocationTypes().subscribe(response => {
        this.locationTypes.set(response.map(locationType => ({ value: locationType.value, label: locationType.label })));
      });
    }
  }

  private loadLocationData(id: number): void {
    this.locationService.getById(id).subscribe({
      next: (location) => {
        this.locationModel.set({
          name: location.name,
          code: location.code,
          type: location.type,
          address: location.address
        });
        this.isLoading.set(false);
      },
      error: () => {
        this.snackBar.open('Error al cargar la ubicación', 'Cerrar', { duration: 3000 });
        this.goBack();
      }
    });
  }

  public goBack(): void {
    this.location.back();
  }
}

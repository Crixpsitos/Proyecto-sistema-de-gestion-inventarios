import { Component, inject, OnInit, signal } from '@angular/core';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import {
  form,
  required,
  FormField,
  FormRoot,
  validateHttp,
  ChildFieldContext,
  TreeValidationResult
} from '@angular/forms/signals';
import { Router } from '@angular/router';
import { LocationService } from '@/core/services/location/LocationService';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LocationRequest, LocationType } from '@/core/models/location/LocationRequest';
import { environment } from '@env/environment';



@Component({
  selector: 'app-add-location',
  imports: [MatInputModule, MatIconModule, MatSelectModule, MatCardModule, MatCheckboxModule, MatDividerModule, ReactiveFormsModule, FormRoot, FormField],
  templateUrl: './add-location.html',
  styleUrl: './add-location.css',
  providers: [LocationService]
})
export class AddLocation implements OnInit {


  private router = inject(Router)
  private locationService = inject(LocationService)

  private locationTypes = signal<{ value: string, label: string }[]>([]);
  private snackBar = inject(MatSnackBar);



  ngOnInit(): void {
    this.locationService.getLocationTypes().subscribe(response => {
      this.locationTypes.set(response.map(locationType => ({ value: locationType.value, label: locationType.label })));
    })
  }

  //form signals - aprendiendo angular 21

  locationModel = signal({
    name: '',
    code: '',
    type: '',
    active: false,
    address: ''
  })

  locationForm = form(this.locationModel, (schema) => {
    required(schema.name, {message: 'El nombre es obligatorio'});
    required(schema.type, {message: 'El tipo es obligatorio'});
    validateHttp(schema.code, {
      request: ({value}) => {
        const code = value();

        return code ? `${environment.apiUrl}/api/locations/check-code?code=${code}` : undefined;
      },
      onSuccess: function (result: { isAvailable: boolean }, ctx: ChildFieldContext<string>): TreeValidationResult {
        return result.isAvailable ? null : { kind: 'codeTaken', message: 'El código ya está en uso' };
      },
      onError: function (error: unknown, ctx: ChildFieldContext<string>): TreeValidationResult {
        console.log(error);
        return { kind: 'invalid', message: 'Error al validar el código' };
      }
    });
    required(schema.address, {message: 'La dirección es obligatoria'});
  }, {
    submission: {
      action: async (f) => {
        console.log(f().value());
        this.locationService.create(f().value() as LocationRequest).subscribe({
          next: (response) => {
            this.snackBar.open('Ubicación creada exitosamente', 'Cerrar', {
              duration: 3000,
            });
            this.router.navigate(['/locaciones'])
        },
        error: (err) => {
          this.snackBar.open('Error al crear la ubicación', 'Cerrar', {
            duration: 3000,
          });
        }
      })
      },
      onInvalid: (field, detail) => {
        this.snackBar.open('El campo ' + field.name().value() + ' es obligatorio', 'Cerrar', {
          duration: 3000,
        });

      }

    }
  })


  public goBack() {
    this.router.navigate(['/locaciones']);
  }

}

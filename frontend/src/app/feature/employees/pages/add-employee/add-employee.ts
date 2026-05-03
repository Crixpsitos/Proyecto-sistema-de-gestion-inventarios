import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { EmployeeService } from '../../../../core/services/employee/employee';
import { EmployeeCreateRequest } from '../../../../core/models/employee/EmployeeCreateRequest';

@Component({
  selector: 'app-add-employee',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
  ],
  templateUrl: './add-employee.html',
  styleUrl: './add-employee.css',
  providers: [EmployeeService],
})
export class AddEmployee {
  private readonly fb = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  public form = this.fb.group({
    name: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    phone: [''],
    documentType: ['CC', Validators.required],
    documentNumber: ['', Validators.required],
    role: ['EMPLOYEE', Validators.required],
  });

  public submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const payload: EmployeeCreateRequest = {
      name: value.name || '',
      lastName: value.lastName || '',
      email: value.email || '',
      password: value.password || '',
      phone: value.phone || '',
      documentType: value.documentType || 'CC',
      documentNumber: value.documentNumber || '',
      role: value.role || '',
    };

    this.employeeService.create(payload).subscribe({
      next: () => {
        this.snackBar.open('Empleado creado correctamente', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/empleados']);
      },
      error: () => this.snackBar.open('No se pudo crear el empleado', 'Cerrar', { duration: 3500 }),
    });
  }

  public cancel(): void {
    this.router.navigate(['/empleados']);
  }
}

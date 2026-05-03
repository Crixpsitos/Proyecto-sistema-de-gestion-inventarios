import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { EmployeeService } from '@/core/services/employee/employee';

@Component({
  selector: 'app-edit-employee',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './edit-employee.html',
  styleUrl: './edit-employee.css',
  providers: [EmployeeService],
})
export class EditEmployee implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);
  private readonly snackBar = inject(MatSnackBar);

  private employeeId = 0;

  public profileForm = this.fb.group({
    name: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    documentType: ['CC', Validators.required],
    documentNumber: ['', Validators.required],
  });

  public permissionsForm = this.fb.group({
    role: ['', Validators.required],
  });

  ngOnInit(): void {
    this.employeeId = Number(this.route.snapshot.paramMap.get('id'));

    this.employeeService.findById(this.employeeId).subscribe({
      next: (employee) => {
        this.profileForm.patchValue({
          name: employee.name,
          lastName: employee.lastName,
          email: employee.email,
          phone: employee.phone || '',
          documentType: employee.documentType,
          documentNumber: employee.documentNumber,
        });

        this.permissionsForm.patchValue({
          role: employee.role,
        });
      },
      error: () => this.snackBar.open('No se pudo cargar el empleado', 'Cerrar', { duration: 3500 }),
    });
  }

  public saveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const value = this.profileForm.getRawValue();
    this.employeeService
      .updateProfile(this.employeeId, {
        name: value.name || '',
        lastName: value.lastName || '',
        email: value.email || '',
        phone: value.phone || '',
        documentType: value.documentType || 'CC',
        documentNumber: value.documentNumber || '',
      })
      .subscribe({
        next: () => this.snackBar.open('Datos actualizados', 'Cerrar', { duration: 2500 }),
        error: () => this.snackBar.open('No se pudo actualizar el perfil', 'Cerrar', { duration: 3500 }),
      });
  }

  public savePermissions(): void {
    if (this.permissionsForm.invalid) {
      this.permissionsForm.markAllAsTouched();
      return;
    }

    const value = this.permissionsForm.getRawValue();
    this.employeeService
      .updatePermissions(this.employeeId, {
        role: value.role || '',
      })
      .subscribe({
        next: () => this.snackBar.open('Rol actualizado', 'Cerrar', { duration: 2500 }),
        error: () => this.snackBar.open('No se pudo actualizar el rol', 'Cerrar', { duration: 3500 }),
      });
  }

  public back(): void {
    this.router.navigate(['/empleados']);
  }
}

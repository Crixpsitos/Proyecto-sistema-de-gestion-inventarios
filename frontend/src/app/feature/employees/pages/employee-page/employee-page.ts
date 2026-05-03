import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { switchMap } from 'rxjs';
import { EmployeeService } from '@/core/services/employee/employee';
import { EmployeeModel } from '@/core/models/employee/EmployeeModel';
import { EmployeeToolbar } from '@/feature/employees/components/employee-toolbar/employee-toolbar';
import { EmployeeTable } from '@/feature/employees/components/employee-table/employee-table';
import { Paginator } from '@/feature/shared/component/paginator/paginator';
import { PageEvent } from '@angular/material/paginator';
import { User } from '@/core/user/services/user.service';

@Component({
  selector: 'app-employee-page',
  standalone: true,
  imports: [EmployeeToolbar, EmployeeTable, Paginator],
  templateUrl: './employee-page.html',
  styleUrl: './employee-page.css',
  providers: [EmployeeService],
})
export class EmployeePage implements OnInit {
  private readonly employeeService = inject(EmployeeService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly snackBar = inject(MatSnackBar);
  private readonly userService = inject(User);

  public employees = signal<EmployeeModel[]>([]);
  public totalElements = signal(0);
  public pageSize = signal(10);
  public currentPage = signal(0);
  public search = signal('');
  public canCreate = computed(() => this.userService.hasRole('ADMIN', 'MANAGER'));
  public canUpdate = computed(() => this.userService.hasRole('ADMIN', 'MANAGER'));

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        switchMap((params) => {
          const page = params['page'] ? Number(params['page']) : 0;
          const size = params['size'] ? Number(params['size']) : 10;
          const search = params['search'] ? String(params['search']) : '';

          this.currentPage.set(page);
          this.pageSize.set(size);
          this.search.set(search);

          return this.employeeService.search(page, size, search);
        })
      )
      .subscribe({
        next: (response) => {
          this.employees.set(response.content);
          this.totalElements.set(response.totalElements);
        },
        error: () => {
          this.snackBar.open('Error al cargar empleados', 'Cerrar', { duration: 3500 });
        },
      });
  }

  public onSearch(search: string): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { search: search || null, page: 0 },
      queryParamsHandling: 'merge',
    });
  }

  public onAdd(): void {
    this.router.navigate(['/añadir-empleado']);
  }

  public onEdit(id: number): void {
    this.router.navigate(['/empleados', id, 'editar']);
  }

  public onActivate(id: number): void {
    this.employeeService.activate(id).subscribe({
      next: () => {
        this.snackBar.open('Empleado activado', 'Cerrar', { duration: 2500 });
        this.reload();
      },
      error: () => this.snackBar.open('No se pudo activar el empleado', 'Cerrar', { duration: 3500 }),
    });
  }

  public onDeactivate(id: number): void {
    this.employeeService.deactivate(id).subscribe({
      next: () => {
        this.snackBar.open('Empleado inactivado', 'Cerrar', { duration: 2500 });
        this.reload();
      },
      error: () => this.snackBar.open('No se pudo inactivar el empleado', 'Cerrar', { duration: 3500 }),
    });
  }

  public onPaginate(event: PageEvent): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: event.pageIndex, size: event.pageSize },
      queryParamsHandling: 'merge',
    });
  }

  private reload(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: this.currentPage(),
        size: this.pageSize(),
        search: this.search() || null,
      },
      queryParamsHandling: 'merge',
    });
  }
}

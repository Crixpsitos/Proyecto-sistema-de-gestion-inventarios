import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { EmployeeModel } from '@/core/models/employee/EmployeeModel';

@Component({
  selector: 'app-employee-table',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatTooltipModule],
  templateUrl: './employee-table.html',
  styleUrl: './employee-table.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EmployeeTable {
  public employees = input.required<EmployeeModel[]>();
  public canUpdate = input(false);
  public editEvent = output<number>();
  public activateEvent = output<number>();
  public deactivateEvent = output<number>();

  public displayedColumns = ['name', 'email', 'role', 'enabled', 'actions'];

  public roleLabel(role: string): string {
    switch (role) {
      case 'ADMIN':
        return 'Administrador';
      case 'MANAGER':
        return 'Encargado';
      case 'EMPLOYEE':
        return 'Empleado';
      default:
        return role;
    }
  }
}

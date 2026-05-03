import { Component, computed, input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MovementModel } from '../../../../core/models/movement/MovementModel';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-movement-table',
  imports: [MatTableModule, DatePipe, MatIconModule, RouterLink],
  templateUrl: './movement-table.html',
  styleUrl: './movement-table.css',
})
export class MovementTable {
  public movements = input.required<MovementModel[]>();

  public dataSource = computed(() => {
    return new MatTableDataSource<MovementModel>(this.movements());
  });

  protected displayedColumns: string[] = [
    'id',
    'type',
    'product',
    'source',
    'destination',
    'quantity',
    'notes',
    'createdAt',
    'createdBy',
  ];

  protected getMovementClass(type?: string): string {
    if (!type)
      return 'inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold bg-gray-100 text-gray-700 border border-gray-200';

    const baseClasses =
      'inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold border ';

    const styles: Record<string, string> = {
      IN: 'bg-green-100 text-green-700 border-green-200',
      OUT: 'bg-red-100 text-red-700 border-red-200',
      TRANSFER: 'bg-blue-100 text-blue-700 border-blue-200',
    };

    return (
      baseClasses + (styles[type.toUpperCase()] || 'bg-gray-100 text-gray-700 border-gray-200')
    );
  }

  protected getMovementIcon(type?: string): string {
    if (!type) return '';

    const icons: Record<string, string> = {
      IN: 'south_east',
      OUT: 'north_west',
      TRANSFER: 'sync_alt',
    };

    return icons[type.toUpperCase()] || 'help_outline';
  }
}

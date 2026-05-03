import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { MovementService } from '../../../../core/services/movement/movement-service';
import { MovementModel } from '../../../../core/models/movement/MovementModel';
import { MovementTable } from '../../components/movement-table/movement-table';
import { MovementToolbar } from '../../components/movement-toolbar/movement-toolbar';
import { MovementFilters } from '../../../../core/models/movement/MovementFilters';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MovementFormComponent } from '../../components/movement-form-component/movement-form-component';
import { MatDialog } from '@angular/material/dialog';
import { Paginator } from '../../../shared/component/paginator/paginator';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-movement-page',
  imports: [MovementTable, MovementToolbar, MatButtonModule, MatIconModule, Paginator],
  templateUrl: './movement-page.html',
  styleUrl: './movement-page.css',
  providers: [MovementService],
})
export class MovementPage implements OnInit {
  public currentFilters = signal<MovementFilters>({
    type: null,
    productId: null,
    locationId: null,
    dateFrom: null,
    dateTo: null,
  });

  ngOnInit(): void {
    this.activatedRoute.queryParams
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        switchMap((params) => {
          const page = params['page'] ? Number(params['page']) : 0;
          const size = params['size'] ? Number(params['size']) : 10;

          const filters: MovementFilters = {
            type: params['type'] || null,
            productId: params['productId'] || null,
            locationId: params['locationId'] || null,
            dateFrom: params['dateFrom'] || null,
            dateTo: params['dateTo'] || null,
          };

          this.currentFilters.set(filters);
          this.currentPage.set(page);
          this.pageSize.set(size);

          return this.movementService.getMovements(filters, page, size);
        }),
      )
      .subscribe({
        next: (response) => {
          if (response) {
            this.movements.set(response.content);
            this.pageSize.set(response.size);
            this.currentPage.set(response.page);
            this.totalElements.set(response.totalElements);
          }
        },
      });
  }
  private movementService = inject(MovementService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);

  public movements = signal<MovementModel[]>([]);
  public pageSize = signal<number>(10);
  public currentPage = signal<number>(0);
  public totalElements = signal<number>(0);

  public resetFilters(): void {
    this.router.navigate(['/movimientos'], { queryParams: {} });
  }

  public handlePagination(event: PageEvent): void {
    this.router.navigate(['/movimientos'], {
      queryParams: {
        page: event.pageIndex,
        size: event.pageSize,
      },
      queryParamsHandling: 'merge',
    });
  }

  openCreateDialog(): void {
    const ref = this.dialog.open(MovementFormComponent, {
      width: '500px',
      disableClose: true,
    });

    ref.afterClosed().subscribe((Movement: MovementModel) => {
      if (Movement) {
        this.movements.set([Movement, ...this.movements()]);
      }
    });
  }

  public changeMovementFilters(filters: MovementFilters) {
    this.router.navigate(['/movimientos'], {
      queryParams: {
        ...filters,
        page: 0,
      },
    });
  }
}

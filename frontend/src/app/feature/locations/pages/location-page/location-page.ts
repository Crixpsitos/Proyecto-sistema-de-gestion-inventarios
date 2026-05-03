import { Component, DestroyRef, inject, signal } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { LocationService } from '../../../../core/services/location/LocationService';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { LocationModel } from '../../../../core/models/location/LocationModel';
import { LocationCard } from '../../components/location-card/location-card';
import { PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Paginator } from '../../../shared/component/paginator/paginator';
import { of } from 'rxjs';
import { LocationToolbar } from '../../components/location-toolbar/location-toolbar';
import { User } from '../../../../core/services/user/user';

@Component({
  selector: 'app-location',
  imports: [LocationCard, Paginator, MatProgressSpinnerModule, LocationToolbar],
  templateUrl: './location-page.html',
  styleUrl: './location-page.css',
  providers: [LocationService],
})
export class LocationPage {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private snackBar = inject(MatSnackBar);
  private locationService = inject(LocationService);
  private userService = inject(User);
  protected pageSize = signal(10);
  protected currentPage = signal(0);
  protected totalElements = signal(0);
  protected loadingData = signal(true);
  protected search = signal('');
  protected type = signal<string>('all');
  protected onlyActive = signal(true);
  protected canManageLocations = signal(false);

  public locations = signal<LocationModel[]>([]);

  constructor() {
    this.canManageLocations.set(this.userService.hasRole('ADMIN', 'MANAGER'));
  }


  public activateLocation(id: number): void {
    this.locationService.activate(id).subscribe({
      next: () => {
        this.snackBar.open('Ubicación activada exitosamente', 'Cerrar', { duration: 3000 });
        this.locations.set(this.locations().map(location => location.id === id ? { ...location, active: true } : location));
      },
      error: (err) => {
        this.snackBar.open('Error al activar la ubicación', 'Cerrar', { duration: 3000 });
      }
    })
  }

  public deactivateLocation(id: number): void {
    this.locationService.deactivate(id).subscribe({
      next: () => {
        this.snackBar.open('Ubicación desactivada exitosamente', 'Cerrar', { duration: 3000 });
        this.locations.set(this.locations().map(location => location.id === id ? { ...location, active: false } : location));
      },
      error: (err) => {
        this.snackBar.open('Error al desactivar la ubicación', 'Cerrar', { duration: 3000 });
      }
    })
  }

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        switchMap((params) => {
          const page = params['page'] ? Number(params['page']) : 0;
          const size = params['size'] ? Number(params['size']) : 10;
          const type = params['type'] ?? 'all';
          const search = params['search'] ?? '';
          const onlyActive = params['onlyActive'] === 'true';

          this.currentPage.set(page);
          this.pageSize.set(size);
          this.type.set(type);
          this.search.set(search);
          this.onlyActive.set(onlyActive);

          this.loadingData.set(true);

          if (search) {
            return this.locationService.search(search, page, size);
          } else {
            return this.locationService.getAll(page, size, type, onlyActive);
          }
        }),
      )
      .subscribe({
        next: (response) => {
          console.log(response);
          if (response) {
            this.locations.set(response.content);
            this.totalElements.set(response.totalElements);
            this.loadingData.set(false);
          }
        },
        error: (error) => {
          this.loadingData.set(false);
          this.snackBar.open('Error al cargar la lista de ubicaciones', 'Cerrar', {
            duration: 3000,
          });
          return of(null);
        },
      });
  }

  public navigateToAddLocation(): void {
    this.router.navigate(['/añadir-ubicacion']);
  }

  public handlePagination(event: PageEvent) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: event.pageIndex,
        size: event.pageSize,
        type: this.type(),
        search: this.search(),
        onlyActive: this.onlyActive() ? 'true' : 'false',
      },
      queryParamsHandling: 'merge',
    });
  }

  public handleTypeChange(type: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        type: type,
        onlyActive: this.onlyActive() ? 'true' : 'false',
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }

  public handleOnlyActiveChange(onlyActive: boolean) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        type: this.type(),
        onlyActive: onlyActive ? 'true' : 'false',
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }

  public handleSearch(term: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        search: term || null,
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }
}

import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatIconModule } from '@angular/material/icon';
import { InventoryService } from '../../../core/services/inventory/inventory-service';
import { InventoryModel } from '../../../core/models/inventory/InventoryModel';
import { InventoryResponse } from '../../../core/models/inventory/InventoryResponse';
import { Paginator } from '../../shared/component/paginator/paginator';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-inventory-page',
  imports: [MatIconModule, Paginator],
  templateUrl: './inventory-page.html',
  styleUrl: './inventory-page.css',
  providers: [InventoryService],
})
export class InventoryPage implements OnInit {
  private readonly inventoryService = inject(InventoryService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  public readonly items = signal<InventoryModel[]>([]);
  public readonly loading = signal(true);
  public readonly page = signal(0);
  public readonly size = signal(10);
  public readonly totalElements = signal(0);
  public readonly searchTerm = signal('');

  public readonly totalQuantity = computed(
    () => this.items().reduce((sum, item) => sum + item.quantity, 0)
  );

  public readonly uniqueLocations = computed(
    () => new Set(this.items().map((item) => item.locationId)).size
  );

  ngOnInit(): void {
    this.route.queryParams
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params) => {
        const page = params['page'] ? Number(params['page']) : 0;
        const size = params['size'] ? Number(params['size']) : 10;
        const search = params['search'] ? String(params['search']) : '';
        this.page.set(page);
        this.size.set(size);
        this.searchTerm.set(search);
        this.loadData(page, size, search);
      });
  }

  public onSearch(value: string): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        search: value || null,
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }

  public onPageChange(event: PageEvent): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: event.pageIndex,
        size: event.pageSize,
      },
      queryParamsHandling: 'merge',
    });
  }

  public trackByInventoryId(index: number, item: InventoryModel): number {
    return item.id ?? index;
  }

  public formatDate(dateIso: string): string {
    const date = new Date(dateIso);
    return new Intl.DateTimeFormat('es-CO', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  }

  private loadData(page: number, size: number, search: string): void {
    this.loading.set(true);
    const source$ = search
      ? this.inventoryService.search(page, size, search)
      : this.inventoryService.getAll(page, size);

    source$.subscribe({
      next: (response: InventoryResponse) => {
        this.items.set(response.content);
        this.totalElements.set(response.totalElements);
        this.loading.set(false);
      },
      error: () => {
        this.items.set([]);
        this.totalElements.set(0);
        this.loading.set(false);
      },
    });
  }
}

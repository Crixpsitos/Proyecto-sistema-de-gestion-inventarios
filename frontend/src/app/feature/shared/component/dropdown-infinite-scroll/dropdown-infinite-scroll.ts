import {
  Component,
  forwardRef,
  HostListener,
  input,
  OnDestroy,
  OnInit,
  output,
  signal,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { Subject, takeUntil } from 'rxjs';
import type { FetchFn } from '@/feature/shared/interfaces/FetchFn';
import type { MapperFn } from '@/feature/shared/interfaces/MapperFn';
import type { SelectItem } from '@/feature/shared/interfaces/SelectItem';

@Component({
  selector: 'app-dropdown-infinite-scroll',
  imports: [InfiniteScrollDirective],
  templateUrl: './dropdown-infinite-scroll.html',
  styleUrl: './dropdown-infinite-scroll.css',

  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DropdownInfiniteScroll),
      multi: true,
    },
  ],
})
export class DropdownInfiniteScroll<T = any> implements OnInit, OnDestroy, ControlValueAccessor {
  readonly fetchFn = input.required<FetchFn<T>>();
  readonly placeholder = input<string>('Seleccione una opción');
  readonly pageSize = input<number>(5);
  readonly mapper = input<MapperFn<T>>((item: any) => ({
    id: item.id,
    label: item.name,
  }));

  readonly selectionChange = output<SelectItem>();

  private pendingValue: any = null;

  items = signal<SelectItem[]>([]);
  selectedItem = signal<SelectItem | null>(null);
  isOpen = signal<boolean>(false);
  isLoading = signal<boolean>(false);
  noMoreItems = signal<boolean>(false);

  private currentPage = 0;
  private destroy$ = new Subject<void>();

  private onChange = (_: any) => {};
  private onTouched = () => {};

  ngOnInit(): void {
    this.loadItems();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  writeValue(id: any): void {
    if (id == null) {
      this.selectedItem.set(null);
      this.pendingValue = null;
      return;
    }
    const found = this.items().find((i) => i.id == id) ?? null;
    if (found) {
      this.selectedItem.set(found);
      this.pendingValue = null;
    } else {
      this.pendingValue = id;
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
  setDisabledState(isDisabled: boolean): void {}

  toggleDropdown(): void {
    this.isOpen.update((v) => !v);
  }

  selectItem(item: SelectItem): void {
    this.selectedItem.set(item);
    this.isOpen.set(false);
    this.onChange(item.id);
    this.onTouched();
    this.selectionChange.emit(item);
  }

  onScrollDown(): void {
    if (this.isLoading() || this.noMoreItems()) return;
    this.currentPage++;
    this.loadItems();
  }

  private loadItems() {
    this.isLoading.set(true);
    this.fetchFn()(this.currentPage, this.pageSize())
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (rawItems) => {
          const mapped = rawItems.map(this.mapper());
          this.items.update((prev) => [...prev, ...mapped]);
          this.noMoreItems.set(rawItems.length < this.pageSize());
          this.isLoading.set(false);

          this.resolvePendingValue();
        },
        error: () => {
          this.isLoading.set(false);
        },
      });
  }

  private resolvePendingValue(): void {
    if (this.pendingValue == null) return;

    const found = this.items().find((i) => i.id == this.pendingValue) ?? null;

    if (found) {
      this.selectedItem.set(found);
      this.pendingValue = null;
    }
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.select-wrapper')) {
      this.isOpen.set(false);
    }
  }
}

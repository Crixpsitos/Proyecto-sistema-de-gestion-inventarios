import { Component, effect, inject, input, OnInit, output, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { debounceTime } from 'rxjs/internal/operators/debounceTime';
import { distinctUntilChanged } from 'rxjs/internal/operators/distinctUntilChanged';
import { LocationService } from '@/core/services/location/LocationService';
import { LocationType } from '@/core/models/location/LocationRequest';
import { Router } from '@angular/router';

@Component({
  selector: 'app-location-toolbar',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
    ReactiveFormsModule
  ],
  templateUrl: './location-toolbar.html',
  providers: [LocationService]
})
export class LocationToolbar implements OnInit {


  readonly search = new FormControl('');

  private locationService = inject(LocationService);
  private router = inject(Router);


  searchEvent = output<string>();
  typeChange = output<string>();
  activeOnly = output<boolean>();
  addLocation = output<void>();

  public types = signal<{ value: string, label: string }[]>([]);
  public selectedType = input.required<string>();
  public onlyActive = input.required<boolean>();
  public searchTerm = input.required<string>();
  public canManage = input(false);

  constructor() {
    effect(() => {
    const term = this.searchTerm();
    if (this.search.value !== term) {
      this.search.setValue(term, { emitEvent: false });
    }
  });
  }

  ngOnInit(): void {
    this.search.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged(),
    ).subscribe(value => this.onSearch(value ?? ''));
    this.locationService.getLocationTypes().subscribe({
      next: (types) => {
        this.loadLocationsType(types);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }


  onTypeChange(type: string): void {
    this.typeChange.emit(type);
  }

  toggleFilterActive(): void {
    this.activeOnly.emit(!this.onlyActive());
  }

  navigateToAddLocation(): void {
    this.router.navigate(['/añadir-ubicacion']);
  }


  private onSearch(value: string): void {
    this.searchEvent.emit(value);
  }

  private loadLocationsType(types: { value: string, label: string }[]) {
    this.types.set(types);
  }
}

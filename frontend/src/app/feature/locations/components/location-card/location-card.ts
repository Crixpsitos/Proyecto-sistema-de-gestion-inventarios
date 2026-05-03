import { Component, inject, input, output } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { LocationModel } from '../../../../core/models/location/LocationModel';
import { Router } from '@angular/router';


@Component({
  selector: 'app-location-card',
  imports: [MatCardModule, MatIconModule, MatDividerModule],
  templateUrl: './location-card.html',
  styleUrl: './location-card.css',
})
export class LocationCard {

  private router = inject(Router);

  public locations = input.required<LocationModel[]>();
  public canManage = input(false);
  public deactiveLocationEvent = output<number>();
  public activateLocationEvent = output<number>();



  public editLocation(location: LocationModel): void {
    this.router.navigate(['/editar-ubicacion', location.id]);
  }

  public deactivateLocation(id: number): void {
    this.deactiveLocationEvent.emit(id);
  }
  public activateLocation(id: number): void {
    this.activateLocationEvent.emit(id);
  }


}

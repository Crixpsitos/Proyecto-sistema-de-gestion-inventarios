import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLocation } from '@/feature/locations/pages/add-location/add-location';

describe('AddLocation', () => {
  let component: AddLocation;
  let fixture: ComponentFixture<AddLocation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddLocation],
    }).compileComponents();

    fixture = TestBed.createComponent(AddLocation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

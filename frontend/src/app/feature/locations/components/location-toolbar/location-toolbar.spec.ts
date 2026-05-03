import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationToolbar } from './location-toolbar';

describe('LocationToolbar', () => {
  let component: LocationToolbar;
  let fixture: ComponentFixture<LocationToolbar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocationToolbar],
    }).compileComponents();

    fixture = TestBed.createComponent(LocationToolbar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

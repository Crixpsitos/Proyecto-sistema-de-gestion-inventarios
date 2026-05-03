import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovementFormComponent } from './movement-form-component';

describe('MovementFormComponent', () => {
  let component: MovementFormComponent;
  let fixture: ComponentFixture<MovementFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovementFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MovementFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

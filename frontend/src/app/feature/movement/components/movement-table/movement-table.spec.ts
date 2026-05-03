import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovementTable } from './movement-table';

describe('MovementTable', () => {
  let component: MovementTable;
  let fixture: ComponentFixture<MovementTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovementTable],
    }).compileComponents();

    fixture = TestBed.createComponent(MovementTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

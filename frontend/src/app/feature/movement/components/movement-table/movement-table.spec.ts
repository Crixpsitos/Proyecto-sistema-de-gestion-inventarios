import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovementTable } from '@/feature/movement/components/movement-table/movement-table';

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

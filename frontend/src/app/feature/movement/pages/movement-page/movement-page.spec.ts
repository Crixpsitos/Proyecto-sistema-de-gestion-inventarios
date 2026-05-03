import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovementPage } from '@/feature/movement/pages/movement-page/movement-page';

describe('MovementPage', () => {
  let component: MovementPage;
  let fixture: ComponentFixture<MovementPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovementPage],
    }).compileComponents();

    fixture = TestBed.createComponent(MovementPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

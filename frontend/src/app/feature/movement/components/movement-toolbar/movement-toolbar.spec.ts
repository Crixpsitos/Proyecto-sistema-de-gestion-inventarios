import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovementToolbar } from './movement-toolbar';

describe('MovementToolbar', () => {
  let component: MovementToolbar;
  let fixture: ComponentFixture<MovementToolbar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovementToolbar],
    }).compileComponents();

    fixture = TestBed.createComponent(MovementToolbar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

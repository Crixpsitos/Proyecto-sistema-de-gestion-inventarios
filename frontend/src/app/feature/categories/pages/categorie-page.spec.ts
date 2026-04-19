import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoriePage } from './categorie-page';

describe('CategoriePage', () => {
  let component: CategoriePage;
  let fixture: ComponentFixture<CategoriePage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoriePage],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoriePage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

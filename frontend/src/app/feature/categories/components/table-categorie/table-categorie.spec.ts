import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableCategorie } from '@/feature/categories/components/table-categorie/table-categorie';

describe('TableCategorie', () => {
  let component: TableCategorie;
  let fixture: ComponentFixture<TableCategorie>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TableCategorie],
    }).compileComponents();

    fixture = TestBed.createComponent(TableCategorie);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

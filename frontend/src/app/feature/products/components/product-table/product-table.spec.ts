import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductTable } from '@/feature/products/components/product-table/product-table';

describe('ProductTable', () => {
  let component: ProductTable;
  let fixture: ComponentFixture<ProductTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductTable],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

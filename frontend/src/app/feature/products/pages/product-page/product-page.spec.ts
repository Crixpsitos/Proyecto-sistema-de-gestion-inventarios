import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductPage } from '@/feature/products/pages/product-page/product-page';

describe('ProductPage', () => {
  let component: ProductPage;
  let fixture: ComponentFixture<ProductPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductPage],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

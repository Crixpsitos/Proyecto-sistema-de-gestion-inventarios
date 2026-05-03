import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFormProduct } from '@/feature/products/components/add-form-product/add-form-product';

describe('AddFormProduct', () => {
  let component: AddFormProduct;
  let fixture: ComponentFixture<AddFormProduct>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddFormProduct],
    }).compileComponents();

    fixture = TestBed.createComponent(AddFormProduct);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

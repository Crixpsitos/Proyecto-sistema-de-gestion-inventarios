import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropdownInfiniteScroll } from './dropdown-infinite-scroll';

describe('DropdownInfiniteScroll', () => {
  let component: DropdownInfiniteScroll;
  let fixture: ComponentFixture<DropdownInfiniteScroll>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DropdownInfiniteScroll],
    }).compileComponents();

    fixture = TestBed.createComponent(DropdownInfiniteScroll);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

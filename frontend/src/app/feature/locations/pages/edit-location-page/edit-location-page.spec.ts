import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditLocationPage } from '@/feature/locations/pages/edit-location-page/edit-location-page';

describe('EditLocationPage', () => {
  let component: EditLocationPage;
  let fixture: ComponentFixture<EditLocationPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditLocationPage],
    }).compileComponents();

    fixture = TestBed.createComponent(EditLocationPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

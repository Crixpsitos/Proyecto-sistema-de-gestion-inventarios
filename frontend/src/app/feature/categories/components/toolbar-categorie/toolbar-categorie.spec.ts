import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToolbarCategorie } from '@/feature/categories/components/toolbar-categorie/toolbar-categorie';

describe('ToolbarCategorie', () => {
  let component: ToolbarCategorie;
  let fixture: ComponentFixture<ToolbarCategorie>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToolbarCategorie],
    }).compileComponents();

    fixture = TestBed.createComponent(ToolbarCategorie);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

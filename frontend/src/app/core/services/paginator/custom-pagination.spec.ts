import { TestBed } from '@angular/core/testing';

import { CustomPagination } from './custom-pagination';

describe('CustomPagination', () => {
  let service: CustomPagination;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomPagination);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

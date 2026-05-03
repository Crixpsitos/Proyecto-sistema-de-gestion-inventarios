import { TestBed } from '@angular/core/testing';

import { Jwt } from '@/core/services/jwt/jwt';

describe('Jwt', () => {
  let service: Jwt;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Jwt);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

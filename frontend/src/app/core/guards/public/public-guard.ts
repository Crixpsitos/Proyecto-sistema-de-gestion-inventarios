import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Jwt } from '../../services/jwt/jwt';

export const publicGuard: CanActivateFn = (route, state) => {
  const jwtService = inject(Jwt);
  const router = inject(Router);
  const token = jwtService.getToken();

  if (token && token.expiresAt > Date.now()) {
    return router.createUrlTree(['/']);
  }
  return true;
};

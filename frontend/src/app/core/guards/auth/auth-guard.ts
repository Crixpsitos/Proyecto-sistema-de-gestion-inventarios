import { CanActivateFn, Router } from '@angular/router';
import { Jwt } from '../../services/jwt/jwt';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const jwtService = inject(Jwt);
  const router = inject(Router);

  const token = jwtService.getToken();


  if (!token) {
    return router.createUrlTree(['/login']);
  }
  if (token.expiresAt < Date.now()) {
    jwtService.removeToken();
    return router.createUrlTree(['/login']);
  } else if (route.root.url.some(segment => segment.path === 'login')) {
    return router.createUrlTree(['/']);
  }

  return true;
};

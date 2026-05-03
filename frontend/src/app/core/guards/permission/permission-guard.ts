import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { Auth } from '@/core/services/auth/auth';
import { User } from '@/core/user/services/user.service';

export const permissionGuard: CanActivateFn = (route) => {
  const requiredRoles = (route.data?.['roles'] as string[] | undefined)?.map((r) => r.toUpperCase()) || [];
  if (!requiredRoles.length) {
    return true;
  }

  const userService = inject(User);
  const authService = inject(Auth);
  const router = inject(Router);

  const evaluate = () => {
    if (userService.hasRole(...requiredRoles)) {
      return true;
    }

    return router.createUrlTree([userService.getFirstAllowedPathByRole()]);
  };

  if (userService.user()) {
    return evaluate();
  }

  return authService.getMe().pipe(
    map(() => evaluate()),
    catchError(() => of(router.createUrlTree(['/login'])))
  );
};

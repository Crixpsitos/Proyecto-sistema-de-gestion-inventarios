import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { Jwt } from '@/core/services/jwt/jwt';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { Auth } from '@/core/services/auth/auth';
import { Router } from '@angular/router';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const jwtService = inject(Jwt);
  const authService = inject(Auth);
  const router = inject(Router);
  const token = jwtService.getToken();

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token.accessToken}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !req.url.includes('/api/auth/refresh') && !req.url.includes('/api/auth/login')) {
        return authService.refreshToken().pipe(
          switchMap(() => {
            const newToken = jwtService.getToken();
            const retryReq = newToken
              ? req.clone({ setHeaders: { Authorization: `Bearer ${newToken.accessToken}` } })
              : req;
            return next(retryReq);
          }),
          catchError((refreshError) => {
            jwtService.removeToken();
            router.navigateByUrl('/login');
            return throwError(() => refreshError);
          })
        );
      }
      return throwError(() => error);
    })
  );
};

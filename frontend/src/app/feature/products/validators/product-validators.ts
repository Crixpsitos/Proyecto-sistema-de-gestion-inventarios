import { HttpClient } from '@angular/common/http';
import { AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import { catchError, map, Observable, of, switchMap, timer } from 'rxjs';
import { environment } from '../../../../environments/environment.development';

export function skuAsyncValidator(http: HttpClient, currentSku: string = ''): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    const sku = control.value;

    if (!sku) {
      return of(null);
    }

    if (sku === currentSku) {
      return of(null);
    }

    return timer(500).pipe(
      switchMap(() =>
        http.get<{ exist: boolean }>(`${environment.apiUrl}/api/products/sku/${sku}`).pipe(
          map(response => {
            return response.exist ? { skuExists: true } : null;
          }),
          catchError((e) => {
            console.error('Error en el validador SKU:', e);
            return of({
              serverError: 'No se pudo verificar el SKU. Intente de nuevo.'
            });
          })
        )
      )
    );
  };
}

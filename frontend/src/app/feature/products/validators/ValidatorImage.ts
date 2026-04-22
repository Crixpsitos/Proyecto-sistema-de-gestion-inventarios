import type { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function validatorImage(control: AbstractControl): ValidationErrors | null {
  const file = control.value;

  if (!file) return null;

  if (!file.type.startsWith('image/')) {
    return { invalidImageType: true };
  }

  if (file.size > 5000000) {
    return { fileTooLarge: true };
  }

  return null;
}

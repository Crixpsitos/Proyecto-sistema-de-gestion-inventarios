import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { Auth } from '@/core/services/auth/auth';
import { User } from '@/core/user/models/user.model';
export const userResolver: ResolveFn<User> = () => {
  return inject(Auth).getMe();
};

import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { Auth } from '../../services/auth/auth';
import { User } from '../../models/user/User';
export const userResolver: ResolveFn<User> = () => {
  return inject(Auth).getMe();
};

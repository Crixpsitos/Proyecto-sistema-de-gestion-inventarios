import { Injectable, signal } from '@angular/core';
import { User as UserModel } from '../../models/user/User';

@Injectable({
  providedIn: 'root',
})
export class User {

  readonly user = signal<UserModel | null>(null);

  public saveUser(data: UserModel): void {
    this.user.set(data);
  }

  public removeUser(): void {
    this.user.set(null);
  }

}

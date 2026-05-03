import { Injectable, signal } from '@angular/core';
import { User as UserModel } from '@/core/user/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class User {

  readonly user = signal<UserModel | null>(null);

  private readonly moduleRouteRoles: Array<{ path: string; roles: string[] }> = [
    { path: '', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'inventarios', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'categorias', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'productos', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'locaciones', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'movimientos', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'empleados', roles: ['ADMIN', 'MANAGER'] },
  ];

  public saveUser(data: UserModel): void {
    this.user.set(data);
  }

  public removeUser(): void {
    this.user.set(null);
  }

  public hasRole(...roles: string[]): boolean {
    const currentUser = this.user();
    if (!currentUser?.role) {
      return false;
    }

    return roles.includes(currentUser.role.toUpperCase());
  }

  public getFirstAllowedPathByRole(): string {
    const currentUser = this.user();
    if (!currentUser?.role) {
      return '/login';
    }

    const role = currentUser.role.toUpperCase();
    const first = this.moduleRouteRoles.find((route) => route.roles.includes(role));
    if (!first) {
      return '/login';
    }

    return first.path ? `/${first.path}` : '/';
  }

}

import { ChangeDetectionStrategy, Component, inject, model } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { User } from '../../core/services/user/user';
import { MatMenuModule } from '@angular/material/menu';
import { Auth } from '../../core/services/auth/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [MatIconModule, MatToolbarModule, MatButtonModule, MatMenuModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Navbar {
  private userService = inject(User);
  protected user = this.userService.user;
  protected authService = inject(Auth);
  protected router = inject(Router);

  drawerOpened = model<boolean>(false);

  toggleDrawer(): void {
    this.drawerOpened.update(v => !v);
  }

  public logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');

  }

  constructor() {
    console.log(this.user());
  }
}

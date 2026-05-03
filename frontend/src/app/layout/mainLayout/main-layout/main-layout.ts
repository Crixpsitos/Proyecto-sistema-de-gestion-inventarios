import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { Navbar } from "@/layout/navbar/navbar";
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Footer } from '@/layout/footer/footer';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { User } from '@/core/user/services/user.service';

@Component({
  selector: 'app-main-layout',
  imports: [Navbar, RouterOutlet, Footer, MatSidenavModule, RouterLink, RouterLinkActive, MatIconModule],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MainLayout {

  private readonly userService = inject(User);

  protected drawerOpened = signal(false);

  private readonly allRoutes = [
    { path: '', name: 'Dashboard', icon: 'dashboard', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'inventarios', name: 'Inventarios', icon: 'inventory_2', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'categorias', name: 'Categorías', icon: 'category', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'productos', name: 'Productos', icon: 'label', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'locaciones', name: 'Locaciones', icon: 'location_on', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'movimientos', name: 'Movimientos', icon: 'sync_alt', roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    { path: 'empleados', name: 'Empleados', icon: 'groups', roles: ['ADMIN', 'MANAGER'] }
  ];

  protected routes = computed(() => this.allRoutes.filter((route) => this.userService.hasRole(...route.roles)));

}

import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { Navbar } from "../../navbar/navbar";
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Footer } from '../../footer/footer';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-main-layout',
  imports: [Navbar, RouterOutlet, Footer, MatSidenavModule, RouterLink, RouterLinkActive, MatIconModule],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MainLayout {

  protected drawerOpened = signal(false);

  protected routes = [
    { path: '', name: 'Inventarios', icon: 'inventory_2' },
    { path: 'categorias', name: 'Categorías', icon: 'category' },
  ];

}

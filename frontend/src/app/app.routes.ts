import { Routes } from '@angular/router';
import { Login } from './feature/auth/pages/login/login';
import { MainLayout } from './layout/mainLayout/main-layout/main-layout';
import { InventoryPage } from './feature/inventory/pages/inventory-page';
import { userResolver } from './core/resolver/user/user-resolver';
import { authGuard } from './core/guards/auth/auth-guard';
import { publicGuard } from './core/guards/public/public-guard';
import { CategoriePage } from './feature/categories/pages/categorie-page';
import { EditCategoryPage } from './feature/categories/pages/edit-category-page/edit-category-page';

export const routes: Routes = [
  {
    path: 'login',
    component: Login,
    canActivate: [publicGuard]
  },
  {
    path: '',
    resolve: { user: userResolver },
    canActivate: [authGuard],
    component: MainLayout,
    children: [
      {
        path: "",
        component: InventoryPage
      },
      {
        path: 'categorias',
        component: CategoriePage
      },
      {
        path: 'categorias/:id/editar',
        component: EditCategoryPage
      }
    ]
  }
];

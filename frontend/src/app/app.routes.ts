import { Routes } from '@angular/router';
import { userResolver } from '@/core/user/resolvers/user.resolver';
import { authGuard } from '@/core/guards/auth/auth-guard';
import { publicGuard } from '@/core/guards/public/public-guard';
import { roleGuard } from '@/core/guards/role/role-guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('@/feature/auth/pages/login/login').then(m => m.Login),
    canActivate: [publicGuard]
  },
  {
    path: '',
    resolve: { user: userResolver },
    canActivate: [authGuard],
    loadComponent: () => import('@/layout/mainLayout/main-layout/main-layout').then(m => m.MainLayout),
    children: [
      {
        path: "",
        loadComponent: () => import('@/feature/dashboard/pages/dashboard-page').then(m => m.DashboardPage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'inventarios',
        loadComponent: () => import('@/feature/inventory/pages/inventory-page').then(m => m.InventoryPage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'categorias',
        loadComponent: () => import('@/feature/categories/pages/categorie-page').then(m => m.CategoriePage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'categorias/:id/editar',
        loadComponent: () => import('@/feature/categories/pages/edit-category-page/edit-category-page').then(m => m.EditCategoryPage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'productos',
        loadComponent: () => import('@/feature/products/pages/product-page/product-page').then(m => m.ProductPage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'añadir-producto',
        loadComponent: () => import('@/feature/products/pages/add-product/add-product').then(m => m.AddProduct),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'productos/:id/editar',
        loadComponent: () => import('@/feature/products/pages/edit-product/edit-product').then(m => m.EditProduct),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'locaciones',
        loadComponent: () => import('@/feature/locations/pages/location-page/location-page').then(m => m.LocationPage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'añadir-ubicacion',
        loadComponent: () => import('@/feature/locations/pages/add-location/add-location').then(m => m.AddLocation),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'editar-ubicacion/:id',
        loadComponent: () => import('@/feature/locations/pages/edit-location-page/edit-location-page').then(m => m.EditLocationPage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'movimientos',
        loadComponent: () => import('@/feature/movement/pages/movement-page/movement-page').then(m => m.MovementPage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'empleados',
        loadComponent: () => import('@/feature/employees/pages/employee-page/employee-page').then(m => m.EmployeePage),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'añadir-empleado',
        loadComponent: () => import('@/feature/employees/pages/add-employee/add-employee').then(m => m.AddEmployee),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'empleados/:id/editar',
        loadComponent: () => import('@/feature/employees/pages/edit-employee/edit-employee').then(m => m.EditEmployee),
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      }
    ]
  }
];

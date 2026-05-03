import { Routes } from '@angular/router';
import { Login } from '@/feature/auth/pages/login/login';
import { MainLayout } from '@/layout/mainLayout/main-layout/main-layout';
import { InventoryPage } from '@/feature/inventory/pages/inventory-page';
import { userResolver } from '@/core/user/resolvers/user.resolver';
import { authGuard } from '@/core/guards/auth/auth-guard';
import { publicGuard } from '@/core/guards/public/public-guard';
import { CategoriePage } from '@/feature/categories/pages/categorie-page';
import { EditCategoryPage } from '@/feature/categories/pages/edit-category-page/edit-category-page';
import { ProductPage } from '@/feature/products/pages/product-page/product-page';
import { AddProduct } from '@/feature/products/pages/add-product/add-product';
import { EditProduct } from '@/feature/products/pages/edit-product/edit-product';
import { LocationPage } from '@/feature/locations/pages/location-page/location-page';
import { AddLocation } from '@/feature/locations/pages/add-location/add-location';
import { EditLocationPage } from '@/feature/locations/pages/edit-location-page/edit-location-page';
import { MovementPage } from '@/feature/movement/pages/movement-page/movement-page';
import { EmployeePage } from '@/feature/employees/pages/employee-page/employee-page';
import { AddEmployee } from '@/feature/employees/pages/add-employee/add-employee';
import { EditEmployee } from '@/feature/employees/pages/edit-employee/edit-employee';
import { roleGuard } from '@/core/guards/role/role-guard';
import { DashboardPage } from '@/feature/dashboard/pages/dashboard-page';

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
        component: DashboardPage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'inventarios',
        component: InventoryPage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'categorias',
        component: CategoriePage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'categorias/:id/editar',
        component: EditCategoryPage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'productos',
        component: ProductPage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'añadir-producto',
        component: AddProduct,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'productos/:id/editar',
        component: EditProduct,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'locaciones',
        component: LocationPage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'añadir-ubicacion',
        component: AddLocation,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'editar-ubicacion/:id',
        component: EditLocationPage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'movimientos',
        component: MovementPage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
      },
      {
        path: 'empleados',
        component: EmployeePage,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'añadir-empleado',
        component: AddEmployee,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      },
      {
        path: 'empleados/:id/editar',
        component: EditEmployee,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'MANAGER'] }
      }
    ]
  }
];

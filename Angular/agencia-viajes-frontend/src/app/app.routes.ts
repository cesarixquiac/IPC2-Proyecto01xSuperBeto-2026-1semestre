import { Routes } from '@angular/router';
import { AuthLayout } from './layouts/auth-layout/auth-layout';
import { MainLayout } from './layouts/main-layout/main-layout';
import { Login } from './features/auth/login/login';
import { DashboardAdmin } from './features/admin/dashboard-admin/dashboard-admin';
import { DashboardAgente } from './features/agente/dashboard-agente/dashboard-agente';
import { DashboardOperaciones } from './features/operaciones/dashboard-operaciones/dashboard-operaciones';
import { authGuard } from './core/guards/auth-guard';
import { CargaMasiva } from './features/admin/carga-masiva/carga-masiva'
import { GestionPaquetes } from './features/admin/gestion-paquetes/gestion-paquetes';
import { GestionPaquetesOperaciones } from './features/operaciones/gestion-paquetes/gestion-paquetes';
import { CrearReservacion } from './features/agente/crear-reservacion/crear-reservacion';
import { CajaPagos } from './features/agente/caja-pagos/caja-pagos';
import { GestionDestinos } from './features/operaciones/gestion-destinos/gestion-destinos';
import { GestionProveedores } from './features/operaciones/gestion-proveedores/gestion-proveedores';
import { Reportes } from './features/admin/reportes/reportes';

export const routes: Routes = [
  {
    path: '',
    component: AuthLayout,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', component: Login }
    ]
  },
  {
    path: '',
    component: MainLayout,
    children: [
      {
        path: 'admin',
        component: DashboardAdmin,
        canActivate: [authGuard], // Le ponemos el candado
        data: { expectedRole: 3 } // Exigimos que sea Rol 3
      },
      {
        path: 'atencion-cliente',
        component: DashboardAgente,
        canActivate: [authGuard],
        data: { expectedRole: 1 }
      },
      {
        path: 'operaciones',
        component: DashboardOperaciones,
        canActivate: [authGuard],
        data: { expectedRole: 2 }
      },
      {
        path: 'admin/carga-masiva',
        component: CargaMasiva,
        canActivate: [authGuard],
        data: { expectedRole: 3 }
      },
      {
        path: 'admin/gestion-paquetes',
        component: GestionPaquetes,
        canActivate: [authGuard],
        data: { expectedRole: 3 }
      },
      {
        path: 'agente/crear-reservacion/:dpi',
        component: CrearReservacion,
        canActivate: [authGuard],
        data: { expectedRole: 1 }
      },
      {
        path: 'agente/caja-pagos',
        component: CajaPagos,
        canActivate: [authGuard],
        data: { expectedRole: 1 }
      },
      {
        path: 'operaciones/destinos',
        component: GestionDestinos,
        canActivate: [authGuard],
        data: { expectedRole: 2 } 
      },
      {
        path: 'operaciones/proveedores',
        component: GestionProveedores,
        canActivate: [authGuard],
        data: { expectedRole: 2 }
      },
      {
        path: 'operaciones/paquetes',
        component: GestionPaquetesOperaciones,
        canActivate: [authGuard],
        data: { expectedRole: 2 }
      },
      {
        path: 'admin/reportes',
        component: Reportes,
        canActivate: [authGuard],
        data: { expectedRole: 3 }
      }

    ]
  }
];
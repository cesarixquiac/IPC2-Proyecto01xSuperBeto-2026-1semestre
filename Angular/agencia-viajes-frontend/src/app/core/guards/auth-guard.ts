import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
  const expectedRole = route.data['expectedRole']; 
  
  const currentRole = localStorage.getItem('userRol');

  if (currentRole && parseInt(currentRole, 10) === expectedRole) {
    return true;
  } else {
    console.warn('Acceso denegado: No tienes permisos para esta ruta.');
    router.navigate(['/login']);
    return false;
  }
};
import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, Router } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink], 
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css'
})
export class MainLayout {
  
  private router = inject(Router);

  cerrarSesion() {
    
    localStorage.removeItem('userRol');
   
    this.router.navigate(['/login']);
  }
}
import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {
  private authService = inject(AuthService);
  private router = inject(Router);



  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  onSubmit() {
    if (this.loginForm.valid) {
      const username = this.loginForm.value.username!;
      const password = this.loginForm.value.password!;

      this.authService.login(username, password).subscribe({
        next: (response) => {
          if (response.success) {
            console.log('Login exitoso desde la BD. Rol:', response.rol);
            localStorage.setItem('userRol', response.rol.toString());

            if (response.rol === 3) {
              this.router.navigate(['/admin']);
            } else if (response.rol === 1) {
              this.router.navigate(['/atencion-cliente']);
            } else if (response.rol === 2) {
              this.router.navigate(['/operaciones']);
            }
          }
        },
        error: (err) => {
          console.error('Error en el login', err);
          alert('Usuario o contraseña incorrectos (Validados en BD)');
        }
      });
    }
  }
}

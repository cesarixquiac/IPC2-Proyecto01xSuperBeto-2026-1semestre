import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { Admin } from '../../../core/service/admin';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms'; 

@Component({
  selector: 'app-dashboard-admin',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.css'
})
export class DashboardAdmin implements OnInit {
  
  private admin = inject(Admin);
  usuarios: any[] = []; 
  usuarioEditando: any = null;
  esNuevoUsuario: boolean = false;

  
  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.admin.obtenerUsuarios().subscribe({
      next: (datos) => {
        this.usuarios = datos;
        console.log('Usuarios cargados:', this.usuarios);
      },
      error: (err) => {
        console.error('Error al cargar usuarios:', err);
      }
    });
  }

  abrirModalNuevo() {
    this.esNuevoUsuario = true;
   this.usuarioEditando = { username: '', password: '', id_rol: 1 }; 
  }

  abrirModal(usuario: any) { 
    this.esNuevoUsuario = false;
    this.usuarioEditando = { ...usuario };
  }

  cerrarModal() {
    this.usuarioEditando = null;
  }

 guardarCambios() {
    if (this.esNuevoUsuario) {

      this.admin.crearUsuario(this.usuarioEditando).subscribe({
        next: (res) => {
          alert('¡Usuario creado con éxito!');
          this.cargarUsuarios();
          this.cerrarModal();
        },
        error: (err) => alert(err.error?.error || 'Error al crear usuario')
      });
    } else {

      this.admin.actualizarUsuario(this.usuarioEditando).subscribe({
        next: (res) => {
          alert('¡Actualizado!');
          this.cargarUsuarios();
          this.cerrarModal();
        },
        error: (err) => alert('Error al actualizar')
      });
    }
  }
}

  




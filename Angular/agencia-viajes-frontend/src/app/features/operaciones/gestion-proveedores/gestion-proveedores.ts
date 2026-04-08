import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Operaciones } from '../../../core/services/operaciones';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-gestion-proveedores',
  imports: [CommonModule,FormsModule,RouterModule],
  templateUrl: './gestion-proveedores.html',
  styleUrl: './gestion-proveedores.css',
})

export class GestionProveedores implements OnInit {
  private operacionesService = inject(Operaciones);

  proveedores: any[] = [];
  mostrarModal: boolean = false;

  nuevoProveedor = {
    nombre: '',
    tipo: 1, // Por defecto: 1 = Aerolínea
    pais: '',
    contacto: ''
  };

  ngOnInit() {
    this.cargarProveedores();
  }

  cargarProveedores() {
    this.operacionesService.obtenerProveedores().subscribe({
      next: (datos) => this.proveedores = datos,
      error: () => alert('Error al cargar la lista de proveedores.')
    });
  }

  abrirModal() {
    this.nuevoProveedor = { nombre: '', tipo: 1, pais: '', contacto: '' };
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
  }

  guardarProveedor() {
    if (!this.nuevoProveedor.nombre || !this.nuevoProveedor.pais) {
      alert('El nombre y el país son obligatorios.');
      return;
    }

    this.operacionesService.crearProveedor(this.nuevoProveedor).subscribe({
      next: () => {
        alert('Proveedor registrado exitosamente. 🏢');
        this.cerrarModal();
        this.cargarProveedores();
      },
      error: () => alert('Error al guardar el proveedor. Verifique que el nombre no esté duplicado.')
    });
  }

 
  obtenerNombreTipo(tipo: number): string {
    const tipos: { [key: number]: string } = {
      1: ' Aerolínea',
      2: ' Hotel',
      3: ' Tour Operador',
      4: ' Traslado/Transporte',
      5: ' Otro'
    };
    return tipos[tipo] || 'Desconocido';
  }
}

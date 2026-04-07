import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Admin } from '../../../core/service/admin';

@Component({
  selector: 'app-gestion-paquetes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gestion-paquetes.html',
  styleUrls: ['./gestion-paquetes.css']
})
export class GestionPaquetes implements OnInit {
  private adminService = inject(Admin);

 
  destinos: any[] = [];
  proveedores: any[] = [];


  paquete = {
    nombre: '',
    id_destino: null,
    duracion_dias: 1,
    precio_venta: 0,
    capacidad_max: 1,
    servicios: [] as any[] 
  };

  ngOnInit() {
    this.cargarCatalogos();
  }

  cargarCatalogos() {
    this.adminService.obtenerCatalogos().subscribe({
      next: (res) => {
        this.destinos = res.destinos;
        this.proveedores = res.proveedores;
      },
      error: (err) => console.error('Error cargando catálogos', err)
    });
  }

  agregarServicio() {
    this.paquete.servicios.push({
      id_proveedor: null,
      descripcion: '',
      costo_agencia: 0
    });
  }

  eliminarServicio(index: number) {
    this.paquete.servicios.splice(index, 1);
  }

  guardarPaquete() {
    if (!this.paquete.nombre || !this.paquete.id_destino) {
      alert('Por favor llene el nombre y el destino del paquete.');
      return;
    }
    if (this.paquete.servicios.length === 0) {
      alert('El paquete debe incluir al menos un servicio.');
      return;
    }

    this.adminService.crearPaquete(this.paquete).subscribe({
      next: (res) => {
        alert('¡Paquete creado exitosamente!');
        this.paquete = { nombre: '', id_destino: null, duracion_dias: 1, precio_venta: 0, capacidad_max: 1, servicios: [] };
      },
      error: (err) => alert(err.error?.error || 'Error al guardar el paquete')
    });
  }
}
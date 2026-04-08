import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Operaciones } from '../../../core/services/operaciones';

@Component({
  selector: 'app-gestion-paquetes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './gestion-paquetes.html',
  styleUrls: ['./gestion-paquetes.css']
})
export class GestionPaquetesOperaciones implements OnInit {
  private operacionesService = inject(Operaciones);

  paquetes: any[] = [];
  destinos: any[] = [];
  proveedores: any[] = [];

  mostrarModal: boolean = false;


  nuevoPaquete = {
    nombre: '',
    id_destino: null,
    duracion_dias: 1,
    descripcion: '',
    precio_venta: 0,
    capacidad_max: 1,
    estado: 1
  };

  servicioTemp = {
    id_proveedor: null,
    descripcion: '',
    costo_agencia: 0
  };


  serviciosAgregados: any[] = [];

  ngOnInit() {
    this.cargarDatosBase();
  }

  cargarDatosBase() {

    this.operacionesService.obtenerPaquetes().subscribe(res => this.paquetes = res);
    // Cargamos destinos y proveedores para los selectores del formulario
    this.operacionesService.obtenerDestinos().subscribe(res => this.destinos = res);
    this.operacionesService.obtenerProveedores().subscribe(res => this.proveedores = res);
  }

  abrirModal() {
    this.nuevoPaquete = { nombre: '', id_destino: null, duracion_dias: 1, descripcion: '', precio_venta: 0, capacidad_max: 1, estado: 1 };
    this.serviciosAgregados = [];
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
  }


  agregarServicio() {
    if (!this.servicioTemp.id_proveedor || !this.servicioTemp.descripcion || this.servicioTemp.costo_agencia <= 0) {
      alert('Llene todos los datos del servicio correctamente.');
      return;
    }

    // Buscamos el nombre del proveedor para mostrarlo en la tabla
    const provEncontrado = this.proveedores.find(p => p.id_proveedor == this.servicioTemp.id_proveedor);

    this.serviciosAgregados.push({
      id_proveedor: this.servicioTemp.id_proveedor,
      nombre_proveedor: provEncontrado ? provEncontrado.nombre : 'Desconocido',
      descripcion: this.servicioTemp.descripcion,
      costo_agencia: this.servicioTemp.costo_agencia
    });

    // Limpiamos los campos temporales
    this.servicioTemp = { id_proveedor: null, descripcion: '', costo_agencia: 0 };
  }

  quitarServicio(index: number) {
    this.serviciosAgregados.splice(index, 1);
  }

  get costoTotalAgencia(): number {
    return this.serviciosAgregados.reduce((total, s) => total + s.costo_agencia, 0);
  }

  get gananciaBruta(): number {
    return this.nuevoPaquete.precio_venta - this.costoTotalAgencia;
  }

  guardarPaquete() {
    if (!this.nuevoPaquete.nombre || !this.nuevoPaquete.id_destino || this.serviciosAgregados.length === 0) {
      alert('Debe llenar los datos generales y agregar al menos un servicio.');
      return;
    }

    if (this.gananciaBruta < 0) {
      alert('¡Alerta! El precio de venta es menor al costo. La agencia perdería dinero.');
      return;
    }

    const payload = {
      ...this.nuevoPaquete,
      servicios: this.serviciosAgregados
    };

    if (this.esEdicion) {

      this.operacionesService.actualizarPaquete(payload).subscribe({
        next: () => {
          alert('Paquete actualizado correctamente.');
          this.cerrarModal();
          this.cargarDatosBase();
        },
        error: () => alert('Error al actualizar el paquete.')
      });
    } else {

      this.operacionesService.crearPaquete(payload).subscribe({
        next: () => {
          alert('🧳 Paquete creado exitosamente.');
          this.cerrarModal();
          this.cargarDatosBase();
        },
        error: () => alert('Error al crear el paquete.')
      });
    }
  }



  esEdicion: boolean = false;
  idPaqueteEditando: number | null = null;


  comprobarAltaDemanda(paquete: any): boolean {

    const porcentajeOcupacion = (paquete.pax_reservados / paquete.capacidad_max) * 100;
    return porcentajeOcupacion >= 80;
  }

  prepararEdicion(paquete: any) {
    this.esEdicion = true;
    this.idPaqueteEditando = paquete.id_paquete;


    this.nuevoPaquete = { ...paquete };


    this.operacionesService.obtenerServiciosDePaquete(paquete.id_paquete).subscribe(servicios => {
      this.serviciosAgregados = servicios;
      this.mostrarModal = true;
    });
  }


  verDetalles(paquete: any) {
    this.prepararEdicion(paquete);

  }


}
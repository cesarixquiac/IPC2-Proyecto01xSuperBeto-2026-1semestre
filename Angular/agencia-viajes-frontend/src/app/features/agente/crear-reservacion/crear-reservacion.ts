import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Agente } from '../../../core/services/agente';

@Component({
  selector: 'app-crear-reservacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-reservacion.html',
  styleUrls: ['./crear-reservacion.css']
})
export class CrearReservacion implements OnInit {
  
  private agenteService = inject(Agente);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  dpiTitular: string = '';
  paquetes: any[] = [];
  
  
  reservacion = {
    id_paquete: null as number | null,
    id_usuario: 2, 
    fecha_viaje: '',
    cantidad_pasajeros: 1,
    costo_total: 0,
    pasajeros: [] as string[]
  };

  ngOnInit() {

    this.dpiTitular = this.route.snapshot.paramMap.get('dpi') || '';
    

    this.reservacion.pasajeros = [this.dpiTitular];

  
    this.cargarPaquetes();
  }

  cargarPaquetes() {
    this.agenteService.obtenerPaquetesDisponibles().subscribe({
      next: (datos) => this.paquetes = datos,
      error: (err) => console.error('Error al cargar paquetes', err)
    });
  }

 
  actualizarCalculos() {
    if (this.reservacion.id_paquete) {
      const paqueteSeleccionado = this.paquetes.find(p => p.id_paquete === this.reservacion.id_paquete);
      
      if (paqueteSeleccionado) {

        if (this.reservacion.cantidad_pasajeros > paqueteSeleccionado.capacidad_max) {
          alert(`El paquete solo permite hasta ${paqueteSeleccionado.capacidad_max} personas.`);
          this.reservacion.cantidad_pasajeros = paqueteSeleccionado.capacidad_max;
        }

        this.reservacion.costo_total = paqueteSeleccionado.precio_venta * this.reservacion.cantidad_pasajeros;
      }
    }


    this.ajustarArregloPasajeros();
  }

  ajustarArregloPasajeros() {
    const cantidadDeseada = this.reservacion.cantidad_pasajeros;
    const cantidadActual = this.reservacion.pasajeros.length;

    if (cantidadDeseada > cantidadActual) {

      for (let i = cantidadActual; i < cantidadDeseada; i++) {
        this.reservacion.pasajeros.push('');
      }
    } else if (cantidadDeseada < cantidadActual) {

      this.reservacion.pasajeros.splice(cantidadDeseada);
    }
  }

  trackByIndex(index: number, obj: any): any {
    return index;
  }

  confirmarReservacion() {
    
    if (!this.reservacion.id_paquete || !this.reservacion.fecha_viaje) {
      alert('Debe seleccionar un paquete y una fecha de viaje.'); return;
    }
    if (this.reservacion.pasajeros.some(dpi => dpi.trim() === '')) {
      alert('Debe llenar los DPIs de todos los pasajeros.'); return;
    }

    this.agenteService.crearReservacion(this.reservacion).subscribe({
      next: (res) => {
        alert(` ¡ÉXITO! ${res.mensaje}\nID Interno: ${res.id_reservacion}`);

        this.router.navigate(['/agente']);
      },
      error: (err) => alert('Error al crear la reservación. Verifique los datos.')
    });
  }
}
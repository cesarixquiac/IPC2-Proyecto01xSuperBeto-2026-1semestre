import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Agente } from '../../../core/services/agente';

@Component({
  selector: 'app-dashboard-agente',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './dashboard-agente.html',
  styleUrls: ['./dashboard-agente.css']
})
export class DashboardAgente {
  
  private agenteService = inject(Agente);

  // Variables para la búsqueda
  dpiBuscado: string = '';
  busquedaRealizada: boolean = false;
  clienteEncontrado: any = null;
  historialReservaciones: any[] = [];
  mostrarModalHistorial: boolean = false;

  nuevoCliente = {
    dpi: '',
    nombre_completo: '',
    fecha_nacimiento: '', // YYYY-MM-DD
    telefono: '',
    email: '',
    nacionalidad: ''
  };

  buscarDPI() {
    if (!this.dpiBuscado || this.dpiBuscado.length < 5) {
      alert('Ingrese un DPI válido para buscar.');
      return;
    }

    this.agenteService.buscarClientePorDPI(this.dpiBuscado).subscribe({
      next: (cliente) => {
        
        this.clienteEncontrado = cliente;
        this.busquedaRealizada = true;
      },
      error: (err) => {
        
        if (err.status === 404) {
          this.clienteEncontrado = null;
          this.busquedaRealizada = true;
        
          this.nuevoCliente.dpi = this.dpiBuscado; 
        } else {
          alert('Error de conexión con el servidor.');
        }
      }
    });
  }

  registrarCliente() {
    this.agenteService.crearCliente(this.nuevoCliente).subscribe({
      next: (res) => {
        alert('¡Cliente registrado con éxito!');
        
        this.dpiBuscado = this.nuevoCliente.dpi;
        this.buscarDPI(); 
      },
      error: (err) => alert('Error al registrar cliente. Intente de nuevo.')
    });
  }

  limpiarBusqueda() {
    this.dpiBuscado = '';
    this.busquedaRealizada = false;
    this.clienteEncontrado = null;
    this.nuevoCliente = { dpi: '', nombre_completo: '', fecha_nacimiento: '', telefono: '', email: '', nacionalidad: '' };
  }

  abrirHistorial() {
    if (!this.clienteEncontrado) return;
    
    this.agenteService.obtenerHistorialCliente(this.clienteEncontrado.dpi).subscribe({
      next: (datos) => {
        this.historialReservaciones = datos;
        this.mostrarModalHistorial = true;
      },
      error: (err) => alert('Error al cargar el historial del cliente.')
    });
  }

  cerrarHistorial() {
    this.mostrarModalHistorial = false;
  }

  


}
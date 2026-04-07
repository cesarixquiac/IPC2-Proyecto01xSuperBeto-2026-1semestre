import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Agente } from '../../../core/services/agente';

@Component({
  selector: 'app-caja-pagos',
  imports: [CommonModule, FormsModule],
  templateUrl: './caja-pagos.html', 
  styleUrl: './caja-pagos.css',
})

export class CajaPagos {
  private agenteService = inject(Agente);

  codigoBuscado: string = '';
  reservaActual: any = null;
  errorBusqueda: boolean = false;

  nuevoPago = {
    id_reservacion: 0,
    monto: 0,
    metodo_pago: 1, // 1: Efectivo, 2: Tarjeta, 3: Transferencia
    fecha_pago: new Date().toISOString().split('T')[0], // Fecha de hoy (YYYY-MM-DD)
    costo_total: 0 
  };

  mostrarRecibo: boolean = false;

  buscarReserva() {
    if (!this.codigoBuscado) return;
    this.errorBusqueda = false;
    this.mostrarRecibo = false;

    this.agenteService.consultarDeuda(this.codigoBuscado).subscribe({
      next: (datos) => {
        this.reservaActual = datos;
        this.nuevoPago.id_reservacion = datos.id_reservacion;
        this.nuevoPago.costo_total = datos.costo_total;
        // Sugerimos pagar el total pendiente por defecto
        this.nuevoPago.monto = datos.saldo_pendiente; 
      },
      error: () => {
        this.reservaActual = null;
        this.errorBusqueda = true;
      }
    });
  }

  procesarPago() {
    if (this.nuevoPago.monto <= 0 || this.nuevoPago.monto > this.reservaActual.saldo_pendiente) {
      alert('El monto ingresado no es válido. No puede ser mayor al saldo pendiente.');
      return;

      
    }

    this.agenteService.registrarPago(this.nuevoPago).subscribe({
      next: (res) => {
        alert('Pago registrado correctamente.');
        
      
        this.reservaActual.total_pagado += this.nuevoPago.monto;
        this.reservaActual.saldo_pendiente = res.nuevo_saldo;

        
        if (res.reserva_confirmada) {
          this.imprimirComprobante();
        } else {
         
          this.nuevoPago.monto = this.reservaActual.saldo_pendiente;
        }
      },
      error: () => alert('Error al procesar el pago.')
    });
  }

 imprimirComprobante() {
    
    this.mostrarRecibo = true; 

   
    setTimeout(() => {
      window.print();
    }, 150);
  }

  obtenerNombreMetodo(id: number): string {
    if (id == 1) return 'Efectivo';
    if (id == 2) return 'Tarjeta de Créd./Déb.';
    return 'Transferencia Bancaria';
  }
}

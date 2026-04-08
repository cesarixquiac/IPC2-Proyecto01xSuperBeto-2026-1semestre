import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Admin } from '../../../core/service/admin'; // Ajusta tu ruta

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.html',
  styleUrls: ['./reportes.css']
})
export class Reportes {
  private adminService = inject(Admin);

  // Filtros de fecha \\
  fechaInicio: string = '2026-01-01';
  fechaFin: string = '2026-12-31';

  // Variables para la tabla de resultados
  tituloReporte: string = '';
  resultados: any[] = [];
  columnas: string[] = [];

  generarReporte(tipo: string, titulo: string) {
    if (!this.fechaInicio || !this.fechaFin) {
      alert('Por favor, seleccione un intervalo de fechas válido.');
      return;
    }

    this.tituloReporte = titulo;
    this.adminService.obtenerReporte(tipo, this.fechaInicio, this.fechaFin).subscribe({
      next: (data) => {
        this.resultados = data;
        
        if (this.resultados.length > 0) {
          this.columnas = Object.keys(this.resultados[0]);
        } else {
          this.columnas = [];
        }
      },
      error: () => alert('Error al generar el reporte.')
    });
  }

  imprimirPDF() {
    window.print(); 
  }

 
  formatearColumna(columna: string): string {
    return columna.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
  }
}
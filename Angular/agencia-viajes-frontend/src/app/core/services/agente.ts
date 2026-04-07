import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Agente {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/clientes';

  buscarClientePorDPI(dpi: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}?dpi=${dpi}`);
  }

  crearCliente(cliente: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, cliente);
  }

  obtenerPaquetesDisponibles(): Observable<any> {
    return this.http.get<any>('http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/paquetes');
  }

  crearReservacion(reservacion: any): Observable<any> {
    return this.http.post<any>('http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/reservaciones', reservacion);
  }

  obtenerHistorialCliente(dpi: string): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/reservaciones?dpi=${dpi}`);
  }

  consultarDeuda(codigo: string): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/pagos?codigo=${codigo}`);
  }

  registrarPago(pago: any): Observable<any> {
    return this.http.post<any>('http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/pagos', pago);
  }
}

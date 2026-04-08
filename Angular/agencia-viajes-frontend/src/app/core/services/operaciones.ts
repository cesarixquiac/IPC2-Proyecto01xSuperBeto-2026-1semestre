import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Operaciones {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api';

 
  obtenerDestinos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/destinos`);
  }

  crearDestino(destino: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/destinos`, destino);
  }

  obtenerProveedores(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/proveedores`);
  }

  crearProveedor(proveedor: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/proveedores`, proveedor);
  }

  
  obtenerPaquetes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/paquetes-operaciones`);
  }

  crearPaquete(paqueteData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/paquetes-operaciones`, paqueteData);
  }

  obtenerServiciosDePaquete(idPaquete: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/paquetes-operaciones?id=${idPaquete}`);
  }


  actualizarPaquete(paqueteData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/paquetes-operaciones`, paqueteData);
  }
}
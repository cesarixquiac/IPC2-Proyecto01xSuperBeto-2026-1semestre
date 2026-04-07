import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Admin {
  
  private http = inject(HttpClient);
  
  private apiUrl = 'http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/usuarios';

  obtenerCatalogos(): Observable<any> {
    return this.http.get<any>('http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/catalogos');
  }

  crearPaquete(paquete: any): Observable<any> {
    return this.http.post<any>('http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/paquetes', paquete);
  }

  obtenerUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  actualizarUsuario(usuario: any): Observable<any> {
    return this.http.put<any>(this.apiUrl, usuario);
  }

  crearUsuario(usuario: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, usuario);
  }
}
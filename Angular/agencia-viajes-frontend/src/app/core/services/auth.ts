import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private http = inject(HttpClient);
  
  private apiUrl = 'http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/login'; 

  login(username: string, password: string): Observable<any> {
   
    return this.http.post<any>(this.apiUrl, { 
      username: username, 
      password: password 
    });
  }
}
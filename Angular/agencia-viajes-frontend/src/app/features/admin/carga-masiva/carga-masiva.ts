      import { Component } from '@angular/core';
      import { HttpClient } from '@angular/common/http';
      import { CommonModule } from '@angular/common'

      @Component({
        selector: 'app-carga-masiva',
        imports: [CommonModule],
        templateUrl: './carga-masiva.html',
        styleUrl: './carga-masiva.css',
      })
      export class CargaMasiva {
        selectedFile: File | null = null;
        resumen: any;

        constructor(private http: HttpClient) {}

        onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

      subirArchivo() {
        if (!this.selectedFile) {
      alert('Por favor selecciona un archivo primero.');
      return;
    }


        const formData = new FormData();
        formData.append('archivo', this.selectedFile);

        this.http.post('http://localhost:8080/IPC2-Proyecto01xSuperBeto-2026-1semestre-AgenciaViajes/api/carga-masiva', formData)
      .subscribe({
        next: (res: any) => {
          this.resumen = res;
          alert('Procesamiento completado con éxito');
        },
        error: (err) => {
          console.error('Error enviando el archivo:', err);
          alert('Error de conexión con el servidor. Revisa la consola.');
        }
          });
      }

      }

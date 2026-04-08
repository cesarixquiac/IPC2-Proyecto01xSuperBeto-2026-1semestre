import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Operaciones } from '../../../core/services/operaciones';

@Component({
  selector: 'app-gestion-destinos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gestion-destinos.html',
  styleUrls: ['./gestion-destinos.css']
})
export class GestionDestinos implements OnInit {
  private operacionesService = inject(Operaciones);

  destinos: any[] = [];
  mostrarModal: boolean = false;

  nuevoDestino = {
    nombre: '',
    pais: '',
    descripcion: '',
    clima_epoca_ideal: '',
    imagen_url: ''
  };

  ngOnInit() {
    this.cargarDestinos();
  }

  cargarDestinos() {
    this.operacionesService.obtenerDestinos().subscribe({
      next: (datos) => this.destinos = datos,
      error: () => alert('Error al cargar la lista de destinos.')
    });
  }

  abrirModal() {
    // Limpiamos el formulario antes de abrirlo
    this.nuevoDestino = { nombre: '', pais: '', descripcion: '', clima_epoca_ideal: '', imagen_url: '' };
    this.mostrarModal = true;
  }

  cerrarModal() {
    this.mostrarModal = false;
  }

  guardarDestino() {
    if (!this.nuevoDestino.nombre || !this.nuevoDestino.pais) {
      alert('El nombre y el país son obligatorios.');
      return;
    }

    this.operacionesService.crearDestino(this.nuevoDestino).subscribe({
      next: () => {
        alert('Destino creado exitosamente. ');
        this.cerrarModal();
        this.cargarDestinos(); // Recargamos la cuadrícula
      },
      error: () => alert('Ocurrió un error al guardar el destino.')
    });
  }
}
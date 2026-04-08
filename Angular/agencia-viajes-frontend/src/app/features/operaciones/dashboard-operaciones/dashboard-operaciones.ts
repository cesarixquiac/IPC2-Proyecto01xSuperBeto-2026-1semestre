import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Operaciones } from '../../../core/services/operaciones';


@Component({
  selector: 'app-dashboard-operaciones',
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './dashboard-operaciones.html',
  styleUrl: './dashboard-operaciones.css',
})
export class DashboardOperaciones {

}

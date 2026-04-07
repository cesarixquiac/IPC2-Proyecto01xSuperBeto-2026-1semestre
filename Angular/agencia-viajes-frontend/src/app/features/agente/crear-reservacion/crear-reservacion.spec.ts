import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearReservacion } from './crear-reservacion';

describe('CrearReservacion', () => {
  let component: CrearReservacion;
  let fixture: ComponentFixture<CrearReservacion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearReservacion]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearReservacion);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

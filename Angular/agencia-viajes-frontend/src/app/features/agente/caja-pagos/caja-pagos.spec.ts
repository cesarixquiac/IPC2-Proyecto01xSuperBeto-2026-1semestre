import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CajaPagos } from './caja-pagos';

describe('CajaPagos', () => {
  let component: CajaPagos;
  let fixture: ComponentFixture<CajaPagos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CajaPagos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CajaPagos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

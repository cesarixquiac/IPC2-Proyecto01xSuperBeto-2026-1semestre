import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardAgente } from './dashboard-agente';

describe('DashboardAgente', () => {
  let component: DashboardAgente;
  let fixture: ComponentFixture<DashboardAgente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardAgente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardAgente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

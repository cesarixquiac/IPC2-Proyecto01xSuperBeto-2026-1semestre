import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionDestinos } from './gestion-destinos';

describe('GestionDestinos', () => {
  let component: GestionDestinos;
  let fixture: ComponentFixture<GestionDestinos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionDestinos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionDestinos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

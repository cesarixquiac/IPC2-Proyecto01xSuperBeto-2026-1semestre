import { TestBed } from '@angular/core/testing';

import { Agente } from './agente';

describe('Agente', () => {
  let service: Agente;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Agente);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

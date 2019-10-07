import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallarAnexoComponent } from './detallar-anexo.component';

describe('DetallarAnexoComponent', () => {
  let component: DetallarAnexoComponent;
  let fixture: ComponentFixture<DetallarAnexoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetallarAnexoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetallarAnexoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

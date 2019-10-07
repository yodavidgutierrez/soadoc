import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormGestionarConstantesComponent } from './form-gestionar-constantes.component';

describe('FormGestionarConstantesComponent', () => {
  let component: FormGestionarConstantesComponent;
  let fixture: ComponentFixture<FormGestionarConstantesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormGestionarConstantesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormGestionarConstantesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReciboDocumentosFisicosComponent } from './recibo-documentos-fisicos.component';

describe('ReciboDocumentosFisicosComponent', () => {
  let component: ReciboDocumentosFisicosComponent;
  let fixture: ComponentFixture<ReciboDocumentosFisicosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReciboDocumentosFisicosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReciboDocumentosFisicosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

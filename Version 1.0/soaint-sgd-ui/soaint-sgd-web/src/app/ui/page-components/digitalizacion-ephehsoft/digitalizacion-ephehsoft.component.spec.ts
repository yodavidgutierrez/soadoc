import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitalizacionEphehsoftComponent } from './digitalizacion-ephehsoft.component';

describe('DigitalizacionEphehsoftComponent', () => {
  let component: DigitalizacionEphehsoftComponent;
  let fixture: ComponentFixture<DigitalizacionEphehsoftComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitalizacionEphehsoftComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitalizacionEphehsoftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

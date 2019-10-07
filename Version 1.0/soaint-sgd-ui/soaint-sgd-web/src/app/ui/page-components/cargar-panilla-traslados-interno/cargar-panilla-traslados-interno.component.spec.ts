import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CargarPanillaTrasladosInternoComponent } from './cargar-panilla-traslados-interno.component';

describe('CargarPanillaTrasladosInternoComponent', () => {
  let component: CargarPanillaTrasladosInternoComponent;
  let fixture: ComponentFixture<CargarPanillaTrasladosInternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CargarPanillaTrasladosInternoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CargarPanillaTrasladosInternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

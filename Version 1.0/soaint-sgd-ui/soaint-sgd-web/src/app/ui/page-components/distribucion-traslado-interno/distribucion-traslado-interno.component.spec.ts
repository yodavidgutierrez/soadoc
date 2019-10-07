import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistribucionTrasladoInternoComponent } from './distribucion-traslado-interno.component';

describe('DistribucionTrasladoInternoComponent', () => {
  let component: DistribucionTrasladoInternoComponent;
  let fixture: ComponentFixture<DistribucionTrasladoInternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistribucionTrasladoInternoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistribucionTrasladoInternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

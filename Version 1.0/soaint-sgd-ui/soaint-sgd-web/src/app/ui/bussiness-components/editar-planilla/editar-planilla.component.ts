import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {Observable} from "rxjs/Observable";
import {ConstanteApiService} from "../../../infrastructure/api/constantes.api";


@Component({
  selector: 'app-editar-planilla',
  templateUrl: './editar-planilla.component.html',
  styleUrls: ['./editar-planilla.component.css']
})
export class EditarPlanillaComponent implements OnInit {

  form: FormGroup;

  @Input()
  planilla: any;

  @Input() keyEstadoEntrega;

  @Output()
  completado: EventEmitter<any> = new EventEmitter<any>();

  @Output() formInitialized : EventEmitter<FormGroup>  = new EventEmitter;

  @Output() onPressCloseButton : EventEmitter<FormGroup>  = new EventEmitter;

  maxDateValue: Date = new Date();

  estadoEntregaSuggestions: Observable<ConstanteDTO[]>

  dependencias: DependenciaDTO[] = [];

  constructor(private formBuilder: FormBuilder,
              private _store: Store<RootState>,
              private _dependenciaSandbox: DependenciaSandbox,
              private _constanteApi: ConstanteApiService

  ) {
    this.initForm();
    }

  ngOnInit() {
    this.estadoEntregaSuggestions = this._constanteApi.Listar({key:this.keyEstadoEntrega});
  }

  resetData() {
    this.form.reset();
  }

  initForm() {
    this.form = this.formBuilder.group({
      estadoEntrega: [null, [Validators.required]],
      fechaEntrega: [null,  [Validators.required]],
      observaciones: [null,  [Validators.required]]
    });

    this.formInitialized.emit(this.form);
  }
  editarPlanilla() {
    this.completado.emit(this.form.value);
  }

  hideEditarPlanillaDialog() {
  this.onPressCloseButton.emit();
  }
}

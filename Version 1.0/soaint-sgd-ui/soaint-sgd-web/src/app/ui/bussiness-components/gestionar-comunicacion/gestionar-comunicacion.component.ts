import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input, OnChanges,
  OnDestroy,
  OnInit,
  Output, SimpleChanges,
  ViewChild
} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import {AgentDTO} from '../../../domain/agentDTO';
import {Sandbox as TaskSandbox} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Store} from "@ngrx/store";
import { State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {Subscription} from "rxjs/Subscription";
import {getActiveTask} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {COMUNICACION_INTERNA_ENVIADA} from "../../../shared/bussiness-properties/radicacion-properties";
import {isNullOrUndefined} from "util";
import {FuncionarioDTO} from "../../../domain/funcionarioDTO";
import {getAuthenticatedFuncionario} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";

@Component({
  selector: 'app-gestionar-comunicacion',
  templateUrl: './gestionar-comunicacion.component.html',
  styleUrls: ['./gestionar-comunicacion.component.scss']
})
export class GestionarComunicacionComponent implements OnInit,OnDestroy,OnChanges {

  procesosSuggestions: { nombre: string, id: number }[];

  form: FormGroup;

  validations: any = {};

  @Input() tipoComunicacion;

  @Input()
  remitente: AgentDTO;

  @Input() reqDigital;

  @Input()  destinatarioPrincipal: AgentDTO;

  task: any;

  subscriptions:Subscription[] = [];

  @Output()
  onDevolverTriggered = new EventEmitter<any>();

  @Output()
  onRedireccionarTriggered = new EventEmitter<any>();

  procesoSeguir: number;

  rejectDialogVisible = false;

  justificationDialogVisible = false;

  //hideCheckBox = true;

  @ViewChild('popupjustificaciones') popupjustificaciones;

  @ViewChild('popupReject') popupReject;

  funcionarioLog: FuncionarioDTO;



  constructor(private formBuilder: FormBuilder,
              private _tareaSandbox: TaskSandbox,
              private _store:Store<RootState>,
              private detectChange:ChangeDetectorRef
  ) {
    this.initForm();
    this.listenForErrors();
  }

  ngOnInit() {

    this.subscriptions.push(
      this._store.select(getAuthenticatedFuncionario).subscribe(funcionario => this.funcionarioLog = funcionario)
    );

    this.subscriptions.push( this._store.select(getActiveTask).subscribe( tarea =>{
      this.task = tarea;
      this.detectChange.detectChanges();
    }));
    this.procesosSuggestions = [{
      nombre: 'Archivar documento',
      id: 1
    }, {
      nombre: 'Devolver a Gestión Documental',
      id: 2
    }, {
      nombre: 'Producir documento respuesta',
      id: 4
    }, {
      nombre: 'Devolver al asignador',
      id: 5
    }];


  }

  gestionarProceso() {
    console.log(this.form.get('proceso').value);
    //console.log(this.form.get('responseToRem').value);
    switch (this.form.get('proceso').value.id) {
      case 1 : {
        this.procesoSeguir = 2;
        this.completeTask();
        break;
      }
      case 2 : {
        this.popupReject.listCauseReturn(this.reqDigital);
        this.rejectDialogVisible = true;
        break;
      }
      case 3 : {
        this.procesoSeguir = 3;
        this.completeTask();
        break;
      }
      case 4 : {
        this.procesoSeguir = 4;
        this.completeTask();
        break;
      }
      case 5 : {
        this.procesoSeguir = 0;
        this.justificationDialogVisible = true;
        this.popupjustificaciones.fillDependenciaOptions();

        const form = this.popupjustificaciones.form;


        form.get("sedeAdministrativa").disable();
        form.get("sedeAdministrativa").setValue(this.destinatarioPrincipal.codSede);
        form.get("dependenciaGrupo").disable();
        form.get("dependenciaGrupo").setValue(this.destinatarioPrincipal.codDependencia);

        form.updateValueAndValidity();

        break;
      }
    }
  }

  completeTask() {
    this._tareaSandbox.completeTaskDispatch(this.getTaskToCompletePayload());
  }

  getTaskToCompletePayload() {
    return {
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
        procesoSeguir: this.procesoSeguir,
        codDependenciaCo: '',
        causalDevolucion: '',
        usuarioProyectorRadicador: this.funcionarioLog.loginName
      }
    }
  }

  redirectComunications(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }) {
    this.onRedireccionarTriggered.emit({
      justificationValues: justificationValues,
      taskToCompletePayload: this.getTaskToCompletePayload()
    });
    this.justificationDialogVisible = false;
    }

  rejectComunications($event) {
    this.onDevolverTriggered.emit({
      payload: $event,
      taskToCompletePayload: this.getTaskToCompletePayload()
    });
    this.hideRejectDialog();
  }

  onChange() {
      //this.form.get('responseToRem').disable();
    //this.hideCheckBox = true;
    //if (this.form.get('proceso').value.id === 4) {
    //  this.hideCheckBox = false;
      //this.form.get('responseToRem').enable();
    //}
  }

  initForm() {
    this.form = this.formBuilder.group({
      'proceso': [{value: null, disabled: false}, Validators.required],
      //'responseToRem': [{value: false, disabled: true}, Validators.required]
    });
  }

  listenForErrors() {
    //this.bindToValidationErrorsOf('responseToRem');
    this.bindToValidationErrorsOf('proceso');
  }

  bindToValidationErrorsOf(control: string) {
    const ac = this.form.get(control);
    ac.valueChanges.subscribe(value => {
      if ((ac.touched || ac.dirty) && ac.errors) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
        delete this.validations[control];
      }
    });
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  hideRejectDialog() {
    this.rejectDialogVisible = false;
  }

  hideJustificationPopup() {
    this.justificationDialogVisible = false;
  }

  sendRedirect() {
    this.popupjustificaciones.redirectComunications();
  }

  sendReject() {
    this.procesoSeguir = this.popupReject.form.get('causalDevolucion').value.id === 1 ? 5 : 6;
    this.popupReject.devolverComunicaciones();
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach( s => s.unsubscribe());
  }

  ngOnChanges(changes: SimpleChanges): void {

    if(!isNullOrUndefined(changes.tipoComunicacion)){
      if(this.tipoComunicacion == COMUNICACION_INTERNA_ENVIADA){
        this.procesosSuggestions =  this.procesosSuggestions.filter(item => item.id!= 2);
      }
    }
  }



}

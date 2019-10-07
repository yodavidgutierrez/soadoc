import {OnInit} from "@angular/core";
import {TaskTypes} from "../type-cheking-clasess/class-types";
import {DependenciaDTO} from "../../domain/dependenciaDTO";
import {Observable} from "rxjs/Observable";
import {RadicadoDTO} from "../../domain/radicadoDTO";
import {Subscription} from "rxjs/Subscription";
import {TareaDTO} from "../../domain/tareaDTO";
import {ComunicacionOficialDTO} from "../../domain/comunicacionOficialDTO";
import {Sandbox as TaskSandBox} from "../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {isNullOrUndefined} from "util";


declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

export  abstract class RadicacionBaseComponent implements  OnInit{

  abstract ngOnInit();

  type = TaskTypes.TASK_FORM;

  tabIndex = 0;
  editable = true;
  printStyle: string = printStyles;

  radicadoPadre:RadicadoDTO;

  task: TareaDTO;
  taskFilter?:string;
  radicacion: ComunicacionOficialDTO;
  barCodeVisible = false;

  formsTabOrder: Array<any> = [];
  activeTaskUnsubscriber: Subscription;
  dependencySubscription:Subscription;
  reqDigitInmediataUnsubscriber:Subscription;
  dependencySelected?:DependenciaDTO;
  printButtonEnabled:boolean = false;

  mediosRecepcionDefaultSelection$: Observable<any>;

  constructor(protected _taskSandbox:TaskSandBox){}


  hideTicketRadicado() {
    this.barCodeVisible = false;
  }

  showTicketRadicado() {
    this.barCodeVisible = true;
  }



abstract  openNext() ;
abstract  openPrev() ;

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

  abort() {
    console.log('ABORT...');
    this._taskSandbox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  abstract  radicacionButtonIsShown():boolean;


  asociarRadicado(radicado:any){

    this.radicadoPadre = radicado;

  }

  abstract save():Observable<any>;

}

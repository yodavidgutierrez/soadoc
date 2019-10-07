import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {RadicarSalidaComponent} from "../radicacion-salida/radicar-salida.component";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {RadicacionSalidaService} from "../../../infrastructure/api/radicacion-salida.service";
import {DomToImageService} from "../../../infrastructure/api/dom-to-image";
import {Sandbox as TaskSandBox} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {
  COMUNICACION_INTERNA_ENVIADA,
  RADICACION_INTERNA,
  RADICACION_SALIDA
} from "../../../shared/bussiness-properties/radicacion-properties";
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {ConfirmationService} from "primeng/primeng";

@Component({
  selector: 'app-correspondencia-interna',
  templateUrl: './correspondencia-interna.component.html',
  styleUrls: ['./correspondencia-interna.component.css']
})
export class CorrespondenciaInternaComponent  extends  RadicarSalidaComponent {

  readonly tipoRadicacion = RADICACION_INTERNA;

  protected filterTipoComunicacion = (tipoComunicacion:ConstanteDTO) => tipoComunicacion.codigo == COMUNICACION_INTERNA_ENVIADA;

  protected filterTipoDoc = (tipologia:ConstanteDTO) => { const tipologiasInt = ['TL-DOCA','TL-DOCC','TL-DOCM']; return tipologiasInt.some( t => tipologia.codigo == t)}

  constructor( protected _store: Store<RootState>
    ,protected _changeDetectorRef: ChangeDetectorRef
    ,protected _sandbox:RadicacionSalidaService
    ,protected _taskSandbox:TaskSandBox
    ,protected  _domToImage:DomToImageService
    ,protected confirmationService:ConfirmationService
  ) {

    super(_store,_changeDetectorRef,_sandbox,_taskSandbox,_domToImage,confirmationService);
  }

  ngOnInit() {
  }

}

import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {
  CompleteTaskAction
} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import 'rxjs/add/operator/switchMap';
import {FAIL_ADJUNTAR_PRINCIPAL, SUCCESS_ADJUNTAR_DOCUMENTO, FAIL_ADJUNTAR_ANEXOS} from '../../../shared/lang/es';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {isArray, isNullOrUndefined} from 'util';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import * as codigos from '../../../shared/bussiness-properties/radicacion-properties';
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {ROL_ASIGNADOR} from "../../../shared/bussiness-properties/radicacion-properties";
import {MIME_TYPES} from "../../../shared/bussiness-properties/mime-types";
import {TIPO_REMITENTE_INTERNO} from "../../../shared/bussiness-properties/radicacion-properties";
import {TareaDTO} from "../../../domain/tareaDTO";
import {combineLatest} from "rxjs/observable/combineLatest";
import {getAuthenticatedFuncionario} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {utils} from "protractor";
import {Utils} from "../../../shared/helpers";

enum UploadStatus {
  CLEAN = 0,
  LOADED = 1,
  UPLOADING = 2,
  UPLOADED = 3,
}

@Component({
  selector: 'app-digitalizar-documento',
  templateUrl: './digitalizar-documento.component.html',
  styleUrls: ['./digitalizar-documento.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DigitalizarDocumentoComponent implements OnInit, OnDestroy {

  uploadFiles: any[] = [];
  task: TareaDTO;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  uploadUrl: string;
  uploadTemplate: string;
  uploadDisabled = false;
  principalFile: string;
  principalFileId = null;
  correspondencia: CorrespondenciaDTO;
  comunicacion: ComunicacionOficialDTO = {};

    tipoSoporteElectronico = false;

  subscriptions: Subscription[] = [];

  @ViewChild('uploader') uploader;
  @ViewChild('viewer') viewer;

  constructor(private changeDetection: ChangeDetectorRef,
              private _api: ApiBase,
              private _asignacionSandBox: AsignacionSandbox,
              private _store: Store<RootState>) {
  }

  ngOnInit() {
    this.uploadUrl = environment.digitalizar_doc_upload_endpoint;
    this.uploadTemplate = environment.upload_template;
    this.subscriptions.push(this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task && this.task.variables && this.task.variables.numeroRadicado) {
        this._asignacionSandBox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado).subscribe((comunicacion) => {
          this.comunicacion = comunicacion;
          this.correspondencia = comunicacion.correspondencia;
          console.log("COMUNICACION!!:  ",this.comunicacion);
        });

        // if(this.task.variables.detalleAnexo == 1){
        //   const payload: LoadNextTaskPayload = {
        //     idProceso: this.task.idProceso,
        //     idInstanciaProceso: this.task.idInstanciaProceso,
        //     idDespliegue: this.task.idDespliegue
        //   };
        //   this._store.dispatch(new ScheduleNextTaskAction(payload));
        // }

        if(  isNullOrUndefined( this.task.variables && this.task.variables.detalleAnexo) ||  this.task.variables.detalleAnexo == 2){
          switch (this.task.idProceso) {
            case 'proceso.correspondencia-entrada' :
              ViewFilterHook.addFilter(`adjuntar-${this.task.idProceso}`,(parameters) => {

                return Object.assign(parameters,{
                  notifyToRol: ROL_ASIGNADOR,
                  notifyToDependenciaList:  this.comunicacion.agenteList.filter( agente => agente.codTipoRemite == TIPO_REMITENTE_INTERNO).map( agente => agente.codDependencia),
                  remitente: { ideFunci:this.correspondencia.codFuncRadica},
                  nroRadicado:this.correspondencia.nroRadicado,
                  nombreTarea : "Asignar Comunicaciones"
                })
              });
              break;
            case 'proceso.correspondencia-salida' :
              ViewFilterHook.addFilter(`adjuntar-${this.task.idProceso}`,(parameters) => {

                const radicadoParts = this.correspondencia.nroRadicado.split("--");
                const niceRadicado =  radicadoParts.length == 2 ? radicadoParts[1] : this.correspondencia.nroRadicado;
                const ideFunci = this.comunicacion.agenteList.find( agente => agente.codTipoRemite == TIPO_REMITENTE_INTERNO).ideFunci;

                return Object.assign(parameters,{
                  notifiable: true,
                  destinatario: {ideFunci: ideFunci} ,
                  remitente: { ideFunci:this.correspondencia.codFuncRadica},
                  nroRadicado:this.correspondencia.nroRadicado,
                  nombreTarea : `Archivar documento ${niceRadicado}`
                })
              });
              break;
          }
        }

      }
    }))

  }

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }

  customUploader(event) {

    if(isNullOrUndefined(this.principalFile) ){

      this._store.dispatch(new PushNotificationAction({severity:'error',summary:'Debe seleccionar un documento principal'}));
      return;
    }


    const formData = new FormData();
    for (const file of event.files) {
      formData.append('files', file, encodeURI(file.name));
    }

    this.comunicacion.anexoList.forEach(value => {
      if (value.codTipoSoporte === 'TP-SOPE') {
        this.tipoSoporteElectronico = true;
      }
    });

    if (isNullOrUndefined(this.principalFile)) {

      this._store.dispatch(new PushNotificationAction({
        severity: 'warn',
        summary: FAIL_ADJUNTAR_PRINCIPAL
      }));

    } else if (!this.tipoSoporteElectronico && event.files.length > 1) {

      this._store.dispatch(new PushNotificationAction({
        severity: 'warn',
        summary: FAIL_ADJUNTAR_ANEXOS
      }));

    } else {
      let _dependencia;
      this.subscriptions.push( combineLatest(
        this._store.select(getAuthenticatedFuncionario),
        this._asignacionSandBox.obtnerDependenciasPorCodigos(this.correspondencia.codDependencia)
        )
        .switchMap(([funcionario,result]) => {
          _dependencia = result.dependencias[0];
          let _agente = this.comunicacion.agenteList.find(a => a.codTipAgent === codigos.TIPO_AGENTE_REMITENTE);
          formData.append('tipoComunicacion', this.correspondencia.codTipoCmc);
          formData.append('nroRadicado', this.correspondencia.nroRadicado);
          if(!isNullOrUndefined(this.ideEcm))
            formData.append('ideEcmPrincipal',this.ideEcm);
          else
            formData.append('principalFileName', encodeURI(this.principalFile));
          if(_dependencia) {
            formData.append('sede', _dependencia.nomSede);
            formData.append('codigoSede', _dependencia.codSede);
            formData.append('dependencia', _dependencia.nombre);
            formData.append('codigoDependencia', _dependencia.codigo);
            formData.append('autor',Utils.funcionarioFunllName(funcionario));
          }
          if(_agente)
            formData.append('nombreRemitente', _agente.nombre);
          return this._api.sendFile(
            this.uploadUrl, formData, []);


        })
        .subscribe( response=> {
          const data = response;

          let ideEcm;
          console.log(response);
          if (isArray(data) ) {
            if (data.length === 0) {
              this._store.dispatch(new PushNotificationAction({
                severity: 'error', summary: 'NO ADJUNTO, NO PUEDE ADJUNTAR EL DOCUMENTO'
              }));

              return;
            }

            ideEcm = data[0];
          }
          if(!isNullOrUndefined(data.codMensaje)&& data.codMensaje != '0000'){
            switch (data.codMensaje) {
              case '1111':
                this._store.dispatch(new PushNotificationAction({
                  severity: 'error', summary: 'DOCUMENTO DUPLICADO, NO PUEDE ADJUNTAR EL DOCUMENTO'
                }));
                // (<FileUpload>(this.uploader)).disabled = true;
                // this.uploadDisabled = true;
                break;
              case '3333':
                this._store.dispatch(new PushNotificationAction({
                  severity: 'error', summary: 'ACCESO DENEGADO, NO PUEDE SUBIR EL DOCUMENTO'
                }));
                break;
              case '4444':
                this._store.dispatch(new PushNotificationAction({
                  severity: 'error',
                  summary: 'LA SEDE ' + _dependencia.nomSede + ' NO SE ENCUENTRA EN EL REPOSITORIO DOCUEMENTAL'
                }));
                break;
              case '4445':
                this._store.dispatch(new PushNotificationAction({
                  severity: 'error',
                  summary: 'LA DEPENDENCIA ' + _dependencia.nombre + ' NO SE ENCUENTRA EN EL REPOSITORIO DOCUEMENTAL'
                }));
                break;
              default:
                this._store.dispatch(new PushNotificationAction({
                  severity: 'error', summary: data.mensaje
                }));
            }
            return;
          }

          if(isNullOrUndefined(ideEcm))
            ideEcm = isNullOrUndefined(this.ideEcm) ? data.documentoDTOList[0].idDocumento : this.ideEcm;

          this._store.dispatch(new CompleteTaskAction({
            idProceso: this.task.idProceso, idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea, parametros: ViewFilterHook.applyFilter(`adjuntar-${this.task.idProceso}`,{ideEcm: ideEcm})
          }));
          ViewFilterHook.removeFilter(`adjuntar-${this.task.idProceso}`);
          this._store.dispatch(new PushNotificationAction({
            severity: 'success', summary: SUCCESS_ADJUNTAR_DOCUMENTO
          }));
          this.uploadDisabled = true;
          this.principalFileId = data[0];
          this.changeDetection.detectChanges();

        }));
    }

  }



  preview(file) {

    this.url = URL.createObjectURL(file);
    this.previewWasRefreshed = true;
    this.changeDetection.detectChanges();
  }

  onUpload(event) {
    // this.uploadFiles = event.files;
    this.status = UploadStatus.UPLOADED;
  }

  hideDocument(){
    this.previewWasRefreshed = false;
  }

  onClear(event) {

    this.principalFile = undefined;

    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
    this.uploadDisabled = false;
    console.log('DOCUMENTO PRINCIPAL ELIMINADO...');
/*    if (null !== this.principalFileId) {
      const deleteUrl = environment.digitalizar_doc_upload_endpoint + '/eliminarprincipal/' + this.principalFileId;
      this._api.post(deleteUrl, {}).subscribe(data => {
        if (data.ok) {
          this.principalFileId = null;
          this._store.dispatch(new PushNotificationAction({
            severity: 'success', summary: 'DOCUMENTOS ELIMINADOS CORRECTAMENTE'
          }));
        }
      });
    }*/
  }

  onSelect(event) {

    if(isNullOrUndefined(this.principalFile) && this.uploader.files.length > 0){

      const file = this.uploader.files.find( f => f.type == MIME_TYPES.pdf);

      if(!isNullOrUndefined(file))
        this.principalFile = file.name;
    }

    this.previewWasRefreshed = false;
    for (const file of event.files) {
      this.uploadFiles.push(file);
    }
    this.changeDetection.detectChanges();
    this.status = UploadStatus.LOADED;
  }

  ngOnDestroy() {
    this.subscriptions.forEach( s => s.unsubscribe());
  }

  isPdf(file){

   return file.type ==  MIME_TYPES.pdf;
  }

  get ideEcm(){

    return (!isNullOrUndefined(this.task) && !isNullOrUndefined(this.task.variables)) ?
        this.task.variables.ideEcm : null;


  }

}

import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {CompleteTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';
import {FAIL_ADJUNTAR_PRINCIPAL, SUCCESS_ADJUNTAR_DOCUMENTO, FAIL_ADJUNTAR_ANEXOS} from '../../../shared/lang/es';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {isArray, isNullOrUndefined} from 'util';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {empty} from 'rxjs/Observer';
import * as codigos from '../../../shared/bussiness-properties/radicacion-properties';
import {FileUpload} from "primeng/primeng";
import {DigitalizarEpehsoftApi} from '../../../infrastructure/api/digitalizar-epehsoft.api';
import {ReplaySubject} from "rxjs";


enum UploadStatus {
  CLEAN = 0,
  LOADED = 1,
  UPLOADING = 2,
  UPLOADED = 3,
}

@Component({
  selector: 'app-digitalizacion-ephehsoft',
  templateUrl: './digitalizacion-ephehsoft.component.html',
  styleUrls: ['./digitalizacion-ephehsoft.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DigitalizacionEphehsoftComponent  implements OnInit, OnDestroy {

  uploadFiles: any[] = [];
  files: any[] = [];
  task: any;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  uploadUrl: string;
  uploadDisabled = false;
  principalFile: string;
  correspondencia: CorrespondenciaDTO;
  comunicacion: ComunicacionOficialDTO = {};

  continuar = false;
  tipoDocumento: string;
  base_64: string;

  activeTaskUnsubscriber: Subscription;

  @ViewChild('uploader') uploader;
  @ViewChild('viewer') viewer;

  constructor(private changeDetection: ChangeDetectorRef,
              private _api: ApiBase,
              private _asignacionSandBox: AsignacionSandbox,
              private _store: Store<RootState>,
              private _digitalizarDocumento: DigitalizarEpehsoftApi) {
  }

  ngOnInit() {
    this.uploadUrl = environment.digitalizacion_epehsoft_endponint;
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task) {
        this._asignacionSandBox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado).subscribe((comunicacion) => {
          this.comunicacion = comunicacion;
          this.correspondencia = comunicacion.correspondencia;
                });
      }
    });

   }

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }


  /**
   * Metodo encargado envio archivo ha digitalizacion y completar tarea.
   * @param event
   */
  customUploader(event) {

    const digitalizarDTO = { arcBase64: this.base_64, tipoDocumental: this.tipoDocumento};

      if (digitalizarDTO.arcBase64) {
          console.log('payload')
        console.log(digitalizarDTO)
       /* this._digitalizarDocumento.digitalizarDocumento(digitalizarDTO).subscribe(response  => {
          console.log(response)*/

          this._store.dispatch(new CompleteTaskAction({
            idProceso: this.task.idProceso, idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea
          }));

      /*  });*/

        this.uploadFiles = [];
        this.uploadDisabled = false;

         this._store.dispatch(new PushNotificationAction({
          severity: 'success', summary: 'Resultado proceso digitalizacion'
        }));

      } else {
        this._store.dispatch(new PushNotificationAction({
          severity: 'error', summary: 'ERROR LECTURA DE ARCHIVO.'
        }));
      }


    this.base_64 = null;
    this.changeDetection.detectChanges();
  }

  onUpload(event) {
    // this.uploadFiles = event.files;
    this.status = UploadStatus.UPLOADED;
  }


  /**
   * Metodo que borrar la lista de archivos visualizados en pantalla.
   * @param event
   */
  onClear(event) {

    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
    this.uploadDisabled = false;

  }

  /**
   * Metodo que inicializa el documento ha cargar.
   * @param event
   */
  onSelect(event) {

    if (isNullOrUndefined(this.principalFile) && this.uploader.files.length > 0 ) {

      this.principalFile = this.uploader.files[0].name;
    }

    this.previewWasRefreshed = false;
    for (const file of event.files) {
      this.uploadFiles.push(file);
      this.getBase64File(file);
    }
    this.changeDetection.detectChanges();
    this.status = UploadStatus.LOADED;
    this.tipoDocumento = 'TIPOLOGIA DOCUMENTAL';
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

  /**
   * Metodo que obtiene en Base64 string un archivo adjunto.
   * @param archivo file
   */
  getBase64File(archivo: any) {

      const reader = new FileReader();
      const fileUpload =  archivo ;

      reader.onload =  () => {
        this.base_64 = (reader.result as string).split(',')[1];
      }
      reader.onerror = () => {
        this._store.dispatch(new PushNotificationAction({
          severity: 'error', summary: 'ERROR LECTURA DE ARCHIVO.'
        }));
      };

      reader.readAsDataURL(fileUpload);

  }

}

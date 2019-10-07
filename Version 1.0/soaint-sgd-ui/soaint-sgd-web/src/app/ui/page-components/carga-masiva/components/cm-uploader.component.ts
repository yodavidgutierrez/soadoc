import {Component, ChangeDetectorRef, ViewChild, Output, EventEmitter, Input} from '@angular/core';

import {CargaMasivaService} from '../providers/carga-masiva.service';
import {ResultUploadDTO} from '../domain/ResultUploadDTO';
import {Observable} from 'rxjs/Observable';
import {State as RootState, State} from 'app/infrastructure/redux-store/redux-reducers';
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from '../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {Store} from '@ngrx/store';
import {DependenciaDTO} from '../../../../domain/dependenciaDTO';
import {Subscription} from 'rxjs/Subscription';
import {FuncionarioDTO} from '../../../../domain/funcionarioDTO';
import {environment} from "../../../../../environments/environment";

enum UploadStatus {
  CLEAN = 0,
  LOADED = 1,
  UPLOADING = 2,
  UPLOADED = 3,
}

@Component({
  selector: 'cm-uploader',
  templateUrl: './cm-uploader.component.html',
  styleUrls: ['../carga-masiva.component.css'],
  providers: [CargaMasivaService]
})

export class CargaMasivaUploaderComponent {

  @Input() taskName = '';
  @Input() processName = '';

  public readonly baseUrl = environment.base_url;

  uploadFile: any;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  resultUpload: ResultUploadDTO;
  dependenciaSelected$: Observable<DependenciaDTO>;
  dependenciaSelected: DependenciaDTO;

  globalDependencySubcription: Subscription;

  funcionarioLog: FuncionarioDTO;

  funcionarioSubcription: Subscription;

  @Output()
  docUploaded = new EventEmitter<ResultUploadDTO>();

  @ViewChild('uploader') uploader;

  constructor(private _store: Store<RootState>, private changeDetection: ChangeDetectorRef, private cmService: CargaMasivaService) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
  }

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }

  uploadFileAction(event): void {
    this.cmService.uploadFile(event.files, {codigoSede: this.dependenciaSelected.codSede, codigoDependencia: this.dependenciaSelected.codigo, funcRadica: this.funcionarioLog.id})
      .subscribe(result => {
        this.resultUpload = result;
        console.log('READY!!!');
        this.docUploaded.emit(result);
        this.changeDetection.detectChanges();
      });
  }

  onUpload(event) {
    this.uploadFile = event.files[0];
    this.status = UploadStatus.UPLOADED;
  }

  onClear(event) {
    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
  }

  onSelect(event) {

    this.previewWasRefreshed = false;
    this.uploadFile = event.files[0];

    if (this.uploader.files.length === 2) {
      this.uploader.remove(0);
    } else if (this.uploader.files.length > 2) {
      let index = 0;
      while (this.uploader.files.length !== 1) {
        if (this.uploader.files[index] !== this.uploadFile) {
          this.uploader.remove(index);
          index--;
        } else if (this.uploader.files[index].lastModified !== this.uploadFile.lastModified) {
          this.uploader.remove(index);
          index--;
        } else {
          index++;
          if (index === this.uploader.files.length) {
            break;
          }
        }
      }
    }
    this.changeDetection.detectChanges();
    this.status = UploadStatus.LOADED;
  }

  ngOnInit() {
    this.globalDependencySubcription = this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
  }

  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
    this.globalDependencySubcription.unsubscribe();
  }

 get today(){
    return new Date();
  }

}

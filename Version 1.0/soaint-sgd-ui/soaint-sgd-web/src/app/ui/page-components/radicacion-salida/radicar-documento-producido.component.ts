import {RadicarSalidaComponent} from "./radicar-salida.component";
import {ChangeDetectorRef, Component} from "@angular/core";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {RadicacionSalidaService} from "../../../infrastructure/api/radicacion-salida.service";
import {Sandbox as TaskSandBox} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {Subject} from "rxjs/Subject";
import {isNullOrUndefined} from "util";
import {DomToImageService} from "../../../infrastructure/api/dom-to-image";
import {
  AGENTE_INTERNO, COMUNICACION_EXTERNA_ENVIADA,
  COMUNICACION_INTERNA_ENVIADA, RADICACION_DOC_PRODUCIDO,
  TIPOLOGIA_MEMORANDO
} from "../../../shared/bussiness-properties/radicacion-properties";
import {ConfirmationService} from "primeng/primeng";
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {FuncionarioDTO} from "../../../domain/funcionarioDTO";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Observable} from "rxjs/Observable";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {InstrumentoApi} from "../../../infrastructure/api/instrumento.api";
import {Utils} from "../../../shared/helpers";
import {APROBADOR, ASIGNADOR} from "../../../app.roles";
import {environment} from "../../../../environments/environment";
import {DomSanitizer} from "@angular/platform-browser";
import {UnidadDocumentalApiService} from "../../../infrastructure/api/unidad-documental.api";

declare var PDFObject:any;

@Component({
  selector: 'app-radicar-documento-producido',
  templateUrl: './radicar-documento-producido.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarDocumentoProducidoComponent extends  RadicarSalidaComponent {


  datosGenerales$: Subject<any> = new Subject;
  datosContacto$: Subject<any> = new Subject;
  ocultar: boolean = true;
  docSrc;
  previewVisible = false;
  hiddenR = true;


  fieldDatosGeneralesDisabled = {
    tipoComunicacion:true,
    tipologiaDocumental:true,
    reqDistFisica:true,
    reqDistElect: true,
    reqDigit: true,
    adjuntarDocumento:true,
    detallarDescripcion:true,
  };

  prevalores;

  protected filterTipoComunicacion = (tipoComunicacion:ConstanteDTO) => tipoComunicacion.codigo[0] == 'S';

  sectionRemitenteVisibility = {'interno':true,'externo':true};

  protected tipoRadicacion = RADICACION_DOC_PRODUCIDO;

  funcionarioAuthenticated:FuncionarioDTO;
  funcionarioToNotify:FuncionarioDTO;

  aprobadores: FuncionarioDTO[];

  ideEcm;

  constructor(
    protected _store: Store<RootState>
    , protected _changeDetectorRef: ChangeDetectorRef
    , protected _sandbox: RadicacionSalidaService
    , protected _taskSandbox: TaskSandBox
    ,protected  _domtoImageService:DomToImageService
    , protected confirmService:ConfirmationService
    ,private instrumentoApi:InstrumentoApi,
    private sanitizer:DomSanitizer
  ) {

    super(_store, _changeDetectorRef, _sandbox, _taskSandbox,_domtoImageService,confirmService);
  }

  ngOnInit() {

    super.ngOnInit();

    ViewFilterHook.addFilter("app-datos-direccion-show-block-dist-dig", () => false);

    this.subscriptions.push(this._store.select(getAuthenticatedFuncionario).subscribe( func => this.funcionarioAuthenticated = func));
  }

  restore() {
    console.log('RESTORE...');
    if (this.task) {
      console.log("TASK...");
      this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {
        console.log("Servicio: " + response.payload);
        const results = response.payload;

        if (!results) {
          console.log("RESULTS");
          this._taskSandbox.getTareaPersisted(this.task.variables.idInstancia, '0000')
            .map(r =>  JSON.parse(r.payload ))
            .subscribe(resp => {

              console.log("Respuesta: ", resp);

              const listaProyectores = resp.gestionarProduccion.listaProyectores;

              this.aprobadores = listaProyectores.filter(proyector => listaProyectores.length == 1 || proyector.rol.nombre == APROBADOR)
                .map(aprobador => aprobador.funcionario);

              resp.datosGenerales.reqDigit = false;
              if (!isNullOrUndefined(this.task.variables.numeroRadicado))
                resp.datosGenerales.radicadosReferidos = [{nombre: this.task.variables.numeroRadicado}];
              resp.datosGenerales.tipoComunicacion = resp.datosGenerales.tipoComunicacion.codigo;
              resp.datosGenerales.reqDistFisica = resp.datosContacto.distFisica;
              resp.datosGenerales.reqDistElect = resp.datosContacto.distElectronica || resp.datosGenerales.tipoComunicacion == COMUNICACION_INTERNA_ENVIADA;
              resp.datosGenerales.tipologiaDocumental = resp.datosGenerales.tipoPlantilla.codigo;

              this.prevalores = {
                reqDistFisica: resp.datosContacto.distFisica,
                reqDistElect: resp.datosContacto.distElectronica || resp.datosGenerales.tipoComunicacion == COMUNICACION_INTERNA_ENVIADA,
                tipoComunicacion: resp.datosGenerales.tipoComunicacion,
                tipologiaDocumental: resp.datosGenerales.tipologiaDocumental
              };

              this.sectionRemitenteVisibility.externo = resp.datosGenerales.tipoComunicacion == COMUNICACION_EXTERNA_ENVIADA;
              this.sectionRemitenteVisibility.interno = resp.datosGenerales.tipoComunicacion == COMUNICACION_INTERNA_ENVIADA;

              this.funcionarioToNotify = resp.gestionarProduccion.listaProyectores[0].funcionario;


              this.datosGenerales$.next(resp.datosGenerales);
              this.ideEcm = resp.datosGenerales.listaVersionesDocumento[0].id;

              this.datosContacto$.next(resp.datosContacto);
            });

          return;
        }else{

          const payload = JSON.parse(results);

          this.prevalores = {
            reqDistFisica: payload.generales.reqDistFisica,
            reqDistElect: payload.generales.reqDistElectronica || payload.generales.tipoComunicacion == COMUNICACION_INTERNA_ENVIADA,
            tipoComunicacion: payload.generales.tipoComunicacion,
            tipologiaDocumental: payload.generales.tipologiaDocumental
          };

          this.funcionarioToNotify = payload.funcionarioToNotify;

          this._taskSandbox.getTareaPersisted(this.task.variables.idInstancia, '0000')
            .map(r =>  JSON.parse(r.payload ))
            .subscribe(resp => {

              const listaProyectores = resp.gestionarProduccion.listaProyectores;

              this.aprobadores = listaProyectores.filter(proyector => listaProyectores.length == 1 || proyector.rol.nombre == APROBADOR)
                .map(aprobador => aprobador.funcionario);
            });
        }

       this.restoreByPayload(JSON.parse(results));

        });
    }
  }

  ngAfterViewInit(){}

 protected buildTaskDataForSave(){

    const payload = super.buildTaskDataForSave();

    payload.funcionarioToNotify = this.funcionarioToNotify;

    return payload;

  }


  ngOnDestroy() {

    super.ngOnDestroy();

    ViewFilterHook.removeFilter("app-datos-direccion-show-block-dist-dig");

  }

  protected buildTaskCompleteParameters(generales:any,noRadicado:any):any{

   const parameters:any = {
      codDependencia:this.dependencySelected.codigo,
      numeroRadicado:noRadicado,
      requiereDistribucion: 2,
      requiereDistribucionDemanda:this.prevalores.reqDistElect ? 2 : 1,
      nroRadicado:Utils.niceRadicado(noRadicado),
      remitente : {ideFunci: this.funcionarioAuthenticated.id,codTipAgent:AGENTE_INTERNO},
    };

    const  niceRadicado =Utils.niceRadicado(noRadicado);

    const remitente =  {ideFunci: this.funcionarioAuthenticated.id,codTipAgent:AGENTE_INTERNO};

    if(this.prevalores.reqDistElect && this.prevalores.tipoComunicacion == COMUNICACION_INTERNA_ENVIADA ){
      parameters.notifications = [
        {
          nroRadicado:niceRadicado,
          nombreTarea: `Archivar Documento ${niceRadicado}`,
          destinatario: {ideFunci: this.funcionarioToNotify.id,codTipAgent:AGENTE_INTERNO},
          remitente:remitente
        },
        {
          nroRadicado:niceRadicado,
          nombreTarea: 'Asignar Comunicación',
          remitente:remitente,
          notifyToDependenciaList:this.datosContacto.listaDestinatariosInternos.map(item => item.dependencia.codigo),
          notifyToRol:ASIGNADOR
        },
      ]
    }
    else {

      parameters.nroRadicado = niceRadicado;
      parameters.nombreTarea = `Archivar Documento ${niceRadicado}`;
      parameters.destinatario = {ideFunci: this.funcionarioToNotify.id,codTipAgent:AGENTE_INTERNO};
      parameters.notifiable = true;
    }
    return parameters;
  }

  protected buildPayload():any{

    const payload = super.buildPayload();

    payload.generales = Object.assign(this.prevalores,this.datosGenerales.form.value);

    payload.generales.ideEcm = this.ideEcm;

    return payload;
  }

  protected processFormData(formData: FormData){

    formData.append('firmas',JSON.stringify(this.aprobadores.map(aprobador => aprobador.loginName)));
  }

  radicacionButtonIsShown():boolean {

    const conditions: boolean[] = [
      this.datosGenerales.form.valid,
      this.datosRemitente.form.valid,
      !this.datosGenerales.form.get("reqDistFisica").value || this.datosGenerales.form.get("tipoComunicacion").value != COMUNICACION_EXTERNA_ENVIADA || ( this.datosEnvio !== undefined && this.datosEnvio.form.valid),
      this.datosContacto.listaDestinatariosExternos.length + this.datosContacto.listaDestinatariosInternos.length > 0,
      !isNullOrUndefined(this.task)
    ];

    return conditions.every(condition => condition);
  }

  protected mustSendImage(general:any):boolean{
    return true;
  }

  openNext() {
    this.tabIndex =  (this.tabIndex + 1)%5;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex +4) % 5;
  }

  protected callRadicarSalida(radicacion):Observable<any>{

    return this._sandbox.radicarDocProducido(this.radicacion)
  }

  protected  proccessImage(response){

    if(!isNullOrUndefined(response.response)){

      const documentBase64 = btoa(response.response.documentoDto.documento);

      this.docSrc = `data:application/pdf;base64,${response.response.documentoDto.documento}`;
    }


  }

  get urlPdf(){


    return  this.sanitizer.bypassSecurityTrustResourceUrl(this.docSrc);
  }

  preview(){
    this.previewVisible = true;

    PDFObject.embed(this.docSrc,'#pdf-embeded');
  }


}

import {ApiBase} from './api/api-base';
import {DatosGeneralesApiService} from './api/datos-generales.api';
import {PlanillasApiService} from './api/planillas.api';
import {DroolsRedireccionarCorrespondenciaApi} from './api/drools-redireccionar-correspondencia.api';
import {ProduccionDocumentalApiService} from './api/produccion-documental.api';
import {PdMessageService} from '../ui/page-components/produccion-documental/providers/PdMessageService';
import {MessagingService} from '../shared/providers/MessagingService';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { SerieSubserieApiService } from 'app/infrastructure/api/serie-subserie.api';
import {SerieService} from './api/serie.service';
import { ConstanteApiService } from './api/constantes.api';
import { CorrespondenciaApiService } from './api/correspondencia.api';
import { DependenciaApiService } from './api/dependencia.api';
import { LocalizacionApiService } from './api/localizacion.api';
import {TipologiaDocumentaService} from "./api/tipologia-documenta.service";
import {domToImageProvider} from "./api/dom-to-image";
import {TaskManagerService} from "./api/task-manager.service";
import {RadicadosApi} from "./api/radicados.api";
import {DatosContactoApi} from "./api/datos-contacto.api";
import {AnexoApi} from "./api/anexo.api";
import {InstrumentoApi} from "./api/instrumento.api";
import {PdObservacionApi} from "./api/pd-observacion.api";
import {TransformVersionDocumentoToDocumentoEcm} from "../shared/data-transformers/transform-version-documento-to-documento-ecm";
import {AgenteApi} from "./api/agente.api";
import {DigitalizarEpehsoftApi} from './api/digitalizar-epehsoft.api';
import {MegafServiceApi} from "./api/megaf-service.api";
import {ApiConsultaDocumentos} from "./api/api-consulta-documentos";

export const API_SERVICES = [
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService,
  MessagingService,
  PdMessageService,
  UnidadDocumentalApiService,
  SerieSubserieApiService,
  SerieService,
  ConstanteApiService,
  CorrespondenciaApiService,
  DependenciaApiService,
  LocalizacionApiService,
  TipologiaDocumentaService,
  domToImageProvider,
  TaskManagerService,
  RadicadosApi,
  DatosContactoApi,
  AnexoApi,
  InstrumentoApi,
  PdObservacionApi,
  TransformVersionDocumentoToDocumentoEcm,
  AgenteApi,
  DigitalizarEpehsoftApi,
  MegafServiceApi,
];

export {
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService,
  MessagingService,
  PdMessageService,

};

import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {Observable} from "rxjs/Observable";
import {RadicadoDTO} from "../../domain/radicadoDTO";
import {PERSONA_JURIDICA, PERSONA_NATURAL} from "../../shared/bussiness-properties/radicacion-properties";
import {environment} from "../../../environments/environment";

@Injectable()
export class RadicadosApi {

  constructor(private  _api:ApiBase){}

  getRadicados(payload?:any):Observable<any>{


     return this._api.list(environment.radicados_padre,payload);

     // return Observable.of([{
     //   tipoDocumento: 'TP-DOCT',
     //  numeroIdentificacion:'8888',
     //  nombre: 'Pepe',
     //  numeroRadicado: '88888',
     //  asunto:'Probando ',
     //  ideReferido:'448',
     //   codTipoPers:PERSONA_NATURAL,
     //   consecutivo:'000584-5'
     // },
     //   {
     //     tipoDocumento: 'TP-DOCT',
     //     nombre: 'la guarapera',
     //     numeroRadicado: '88899988',
     //     NIT:'235232332',
     //     asunto:'Probando',
     //     ideReferido:'448',
     //     razonSocial:"La guaraperia",
     //     codTipoPers:PERSONA_JURIDICA,
     //     consecutivo:'000584-6',
     //     codigoCalidad:'TP-ACCA'
     //   }
     // ]);
  }
}

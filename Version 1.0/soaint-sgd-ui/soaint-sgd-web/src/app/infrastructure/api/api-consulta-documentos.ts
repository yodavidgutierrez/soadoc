import {ApiBase} from "./api-base";
import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {isNullOrUndefined} from "util";
import {Observable} from "rxjs";
import {Store} from "@ngrx/store";
import {State as RootState} from "../redux-store/redux-reducers";
import {PushNotificationAction} from "../state-management/notifications-state/notifications-actions";

@Injectable()
export class ApiConsultaDocumentos {

  constructor(private _api:ApiBase,private _store:Store<RootState>){}

    getConsultaExpedientes(payload):Observable<any>{
      return this._api.list(environment.consultarExpediente, payload)
        .switchMap( res =>{
          if( isNullOrUndefined(res))
            return Observable.of([]);
          if(res.codMensaje != '0000'){
            this._store.dispatch(new PushNotificationAction({severity:'error',summary:res.mensaje}));
            return Observable.empty();
          }
          if(isNullOrUndefined(res.response))
            return Observable.of([]);
          return Observable.of(res.response['consultar-expedientes']);
        })
    }

    getDocumentoPorExpediente(ecmObjld): Observable<any>{
        return this._api.list(`${environment.consultarDocumentosPorExpediente}/${ecmObjld}`,)
          .switchMap( res =>{
            if( isNullOrUndefined(res))
              return Observable.of([]);
            if(res.codMensaje != '0000'){
              this._store.dispatch(new PushNotificationAction({severity:'error',summary:res.mensaje}));
              return Observable.empty();
            }
            if(isNullOrUndefined(res.response))
              return Observable.of([]);
            return Observable.of(res.response['consultar-documentos']);
          })
    }

    getConsultaDocumentos(payload):Observable<any>{
    return this._api.list(environment.consultarDocumentos, payload)
      .switchMap( res =>{
        if( isNullOrUndefined(res))
          return Observable.of([]);
        if(res.codMensaje != '0000'){
          this._store.dispatch(new PushNotificationAction({severity:'error',summary:res.mensaje}));
          return Observable.empty();
        }
        if(isNullOrUndefined(res.response))
          return Observable.of([]);
        return Observable.of(res.response['consultar-documentos']);
      })
    }

  getDetalleDocumento(idDocumento): Observable<any>{
    return this._api.list(`${environment.detalleDcoumento}/${idDocumento}`,)
      .switchMap( res =>{
        if( isNullOrUndefined(res))
          return Observable.of([]);
        if(res.codMensaje != '0000'){
          this._store.dispatch(new PushNotificationAction({severity:'error',summary:res.mensaje}));
          return Observable.empty();
        }
        if(isNullOrUndefined(res.response))
          return Observable.of([]);
        return Observable.of(res.response['consultar-documento']);
      })
  }

}

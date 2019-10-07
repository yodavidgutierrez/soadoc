import {Injectable} from "@angular/core";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../infrastructure/redux-store/redux-reducers";
import {VersionDocumentoDTO} from "../../ui/page-components/produccion-documental/models/DocumentoDTO";
import {
   getSelectedDependencyGroupFuncionario
} from "../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Observable} from "rxjs/Observable";
import {DocumentoEcmDTO} from "../../domain/documentoEcmDTO";

@Injectable()
export class TransformVersionDocumentoToDocumentoEcm{

  constructor(private _store:Store<RootState>){}

  convertToDcoumentoEcmDto(documento:VersionDocumentoDTO):Observable<DocumentoEcmDTO>{

 const reader = new FileReader();

 let dataAsBinaryString = '';

 reader.addEventListener("load", evt => {

   dataAsBinaryString =reader.result as string;
 });

    return  this._store.select(getSelectedDependencyGroupFuncionario)
                .map( dependencia => {

      return {
        idDocumento: documento.id,
        sede: dependencia.nomSede,
        dependencia: dependencia.nombre,
        nombreDocumento: documento.nombre,
        tipoDocumento: documento.tipo,
        tamano: documento.size.toString(),
        versionLabel: documento.version,
        documento:dataAsBinaryString
      };
    })
  }
}

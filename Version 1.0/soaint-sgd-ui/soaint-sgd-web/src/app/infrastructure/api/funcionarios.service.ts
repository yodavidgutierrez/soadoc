import { Injectable } from '@angular/core';
import {ApiBase} from "./api-base";
import {Observable} from "rxjs/Observable";
import {environment} from "../../../environments/environment";
import {isNullOrUndefined} from "util";
import {FuncionarioDTO} from "../../domain/funcionarioDTO";

@Injectable()
export class FuncionariosService {

  constructor(private _api:ApiBase) { }

  getFuncionarioById(id):Observable<any>{

    return this._api.post(environment.buscarFuncionarios_endpoint,{id:id}).map(data => data.funcionarios.find( f => f.id == id));
  }

  getFuncionarioBySpecification(specification?:(funcionario:any)=>boolean){

    return this._api.post(environment.buscarFuncionarios_endpoint)
      .map(data => data.funcionarios.filter( f => isNullOrUndefined(specification) || specification(f)));
  }

  getAllFuncionarios():Observable<any>{

    return this._api.post(environment.buscarFuncionarios_endpoint).map(data => data.funcionarios);
  }

  updateRoles(funcionario:FuncionarioDTO):Observable<any>{

    return this._api.put(environment.updateFuncionarios_roles_endpoint,funcionario);
  }

  update(funcionario:FuncionarioDTO):Observable<any>{

    return this._api.put(environment.updateFuncionarios_endpoint,funcionario);
  }

}

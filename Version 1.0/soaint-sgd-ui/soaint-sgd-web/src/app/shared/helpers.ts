import {isNullOrUndefined} from "util";

export class Utils {

  constructor(){}

 public static niceRadicado(nroRadicado){

    const radicadoParts = nroRadicado.split('--');

    return radicadoParts.length == 2 ? radicadoParts[1] : nroRadicado;
  }

  static funcionarioFunllName( f){
    return `${f.nombre} ${!isNullOrUndefined(f.valApellido1) ? f.valApellido1: ''} ${!isNullOrUndefined(f.valApellido2) ? f.valApellido2 : ''}`;
  }

}

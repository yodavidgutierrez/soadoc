import { Pipe, PipeTransform } from '@angular/core';
import {isNullOrUndefined} from "util";

@Pipe({
  name: 'funcionarioNombreCompleto'
})
export class FuncionarioNombreCompletoPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (value) {

      return (<string>(typeof value))  == 'array' ? value.map(f => {

        const fullName = `${f.nombre} ${!isNullOrUndefined(f.valApellido1) ? f.valApellido1: ''} ${!isNullOrUndefined(f.valApellido2) ? f.valApellido2 : ''}`;

        return {label: fullName, value: f.loginName};
      }) : `${value.nombre} ${!isNullOrUndefined(value.valApellido1) ? value.valApellido1: ''} ${!isNullOrUndefined(value.valApellido2) ? value.valApellido2 : ''}`


    }
  }

}

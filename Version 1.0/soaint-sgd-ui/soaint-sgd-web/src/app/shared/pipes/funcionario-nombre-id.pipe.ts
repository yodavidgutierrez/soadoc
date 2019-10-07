import { Pipe, PipeTransform } from '@angular/core';
import {isNullOrUndefined} from "util";

@Pipe({
  name: 'funcionarioNombreId'
})
export class FuncionarioNombreIdPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (value) {

      return value.map(f => {

        const fullName = `${f.nombre} ${!isNullOrUndefined(f.valApellido1) ? f.valApellido1: ''} ${!isNullOrUndefined(f.valApellido2) ? f.valApellido2 : ''}`;

        return {label: fullName, value: f.id};
      })


    }
  }

}

import { Pipe, PipeTransform } from '@angular/core';
import {isNullOrUndefined} from "util";

@Pipe({
  name: 'constantName'
})
export class ConstantNamePipe implements PipeTransform {

  transform(value: any, args?: any): any {

    if(isNullOrUndefined(value))
       return '';

      const ct = value.find(c => c.codigo == args);

      return !isNullOrUndefined(ct) ? ct.nombre : '';
  }

}

import { Pipe, PipeTransform } from '@angular/core';
import {isNullOrUndefined} from "util";
import {Utils} from "../helpers";

@Pipe({
  name: 'niceNoRadicado'
})
export class NiceNoRadicadoPipe implements PipeTransform {

  transform(value: any, args?: any): any {

    if(isNullOrUndefined(value))
        return '';

   return Utils.niceRadicado(value);
  }

}

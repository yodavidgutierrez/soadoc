import {Pipe, PipeTransform} from "@angular/core";
import {isNullOrUndefined} from "util";

@Pipe({name:"seriename"})
export class SerieNamePipe implements  PipeTransform{

  transform(value: any, ...args: any[]): any {

    const codigoSerie = args[0];

    if(isNullOrUndefined(value))
       return '';

    if(args.length > 1 && args[1] == 'subserie'){
      const subs = (<any[]>value).find( serie => serie.codSubserie == codigoSerie);

      return isNullOrUndefined(subs) ? '': `/${subs.nomSubserie}`;
    }

    const ser = (<any[]>value).find( serie => serie.codigoSerie == codigoSerie);

    return isNullOrUndefined(ser) ? '': ser.nombreSerie;
  }

}

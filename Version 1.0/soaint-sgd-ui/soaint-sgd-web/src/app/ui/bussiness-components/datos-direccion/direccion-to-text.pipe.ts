import { Pipe, PipeTransform } from '@angular/core';
import { isNullOrUndefined } from 'util';

@Pipe({
  name: 'direccionToText'
})
export class DireccionToTextPipe implements PipeTransform {

  transform(value: any): any {
    let direccionText = [];

    try {
      let direccion = JSON.parse(value);

      Object.keys(direccion).forEach( key => {

        if(direccion[key].nombre){
          direccionText.push(direccion[key].nombre);
          return;
        }

        direccionText.push(direccion[key]) ;

      });
    } catch (error) {
        return value;
    }

    return direccionText.join(' ');
  }

}

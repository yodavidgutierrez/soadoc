import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'estadoAsignacion'})
export class EstadoAsignacion implements PipeTransform{

  transform(value, args?) {
    return(value === 'SA') ? 'SIN ASIGNAR' : 'ASIGNADA';
  }

}

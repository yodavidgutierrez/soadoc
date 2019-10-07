import {Pipe, PipeTransform} from "@angular/core";
import {DependenciaDTO} from "../../domain/dependenciaDTO";
import {isNullOrUndefined} from "util";

@Pipe({ name:"dependenciaNombre"})
export class DependenciaNombrePipe implements PipeTransform{
  transform(value: DependenciaDTO[], ...args: any[]): any {

     if(isNullOrUndefined(value) || args.length == 0 || isNullOrUndefined(args[0]))
       return ;

     const dependencia = value.find( dep => dep.codigo == args[0]);

    return (isNullOrUndefined(dependencia)) ? '' : dependencia.nombre;

  }

}

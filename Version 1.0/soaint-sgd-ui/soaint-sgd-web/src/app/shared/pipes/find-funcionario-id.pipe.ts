import {Pipe, PipeTransform} from "@angular/core";
import {isNullOrUndefined} from "util";
import {FuncionarioDTO} from "../../domain/funcionarioDTO";

@Pipe(
  {
    name: 'findFuncionarioById'
  }
)
export class FindFuncionarioIdPipe implements PipeTransform{
  transform(value:any, id: any): any {

     if(isNullOrUndefined(value) || isNullOrUndefined(id))
        return '';

   const f =   value.find( funci => funci.id == id);

   if(isNullOrUndefined(f))
      return '';

    return `${f.nombre} ${!isNullOrUndefined(f.valApellido1) ? f.valApellido1: ''} ${!isNullOrUndefined(f.valApellido2) ? f.valApellido2 : ''}`;


  }

}

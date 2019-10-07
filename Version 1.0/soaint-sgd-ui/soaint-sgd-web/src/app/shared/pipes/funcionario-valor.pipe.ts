import {Pipe, PipeTransform} from "@angular/core";
import {isNullOrUndefined} from "util";
import {Utils} from "../helpers";

@Pipe({
  name:"funcionarioValor"
})
export class FuncionarioValorPipe implements PipeTransform{
  transform(funcionarios: any[]): any {
      return funcionarios.map(f => {return {label:Utils.funcionarioFunllName(f),value:f}});
  }


}

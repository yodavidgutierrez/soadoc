import {Pipe, PipeTransform} from "@angular/core";
import {isNullOrUndefined} from "util";

@Pipe({
  name:"megafStructure"
})
export class MegafStructurePipe implements PipeTransform{
  transform(value: any[]): any {

    if(isNullOrUndefined(value))
      return [];

    return value.map( v => { return {label:v.nombre,value: v.ide}});
  }

}

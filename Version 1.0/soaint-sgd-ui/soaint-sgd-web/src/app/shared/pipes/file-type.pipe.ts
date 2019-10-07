import {Pipe, PipeTransform} from "@angular/core";
import {MIME_TYPES} from "../bussiness-properties/mime-types";
import {isNullOrUndefined} from "util";

@Pipe({
  name:'fileType'
})
export class FileTypePipe implements PipeTransform{
  transform(value: string): any {

  let  type =    Object.keys(MIME_TYPES).find( key => {

    if(typeof  MIME_TYPES[key] == 'string')
      return MIME_TYPES[key] == value;

    if( MIME_TYPES[key] instanceof Array)
      return (<any[]> MIME_TYPES[key]).some( v => v == value);

    if(MIME_TYPES[key] instanceof RegExp)
        return  (<RegExp>MIME_TYPES[key]).test(value);

     return false;
    });

  return (isNullOrUndefined(type)) ? value : type;

  }

}

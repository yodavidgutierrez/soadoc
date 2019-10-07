import {Pipe, PipeTransform} from "@angular/core";
import {Utf8Helper} from "../utf8";

@Pipe({
  name:"utf8"
})
export class Utf8EncodePipe implements PipeTransform{
  transform(value: string): any {
    return Utf8Helper.encode(value);
  }

}

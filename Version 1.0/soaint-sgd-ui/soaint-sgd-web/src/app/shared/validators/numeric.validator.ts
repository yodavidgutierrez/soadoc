import {AbstractControl} from '@angular/forms';
import {isNullOrUndefined} from "util";


export class NumericValidator {

  public static validate(c: AbstractControl) {

    if(isNullOrUndefined(c.value))
       return true;
    const NUMERIC_REGEXP = /^[0-9]*$/;

    return NUMERIC_REGEXP.test(c.value) ? null : {
      numeric: {
        valid: false
      }
    };
  }
}

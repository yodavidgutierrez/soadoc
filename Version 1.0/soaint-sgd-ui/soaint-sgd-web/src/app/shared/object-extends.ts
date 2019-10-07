import {isNullOrUndefined} from "util";

export class  ObjectHelper {

  static  similar( obj1:Object,obj2:Object): boolean{

    if(isNullOrUndefined(obj1))
       return isNullOrUndefined(obj2);

    if(isNullOrUndefined(obj2))
      return isNullOrUndefined(obj1);

    let a = Object.keys(obj1).length >= Object.keys(obj2).length ? obj1 : obj2;
    let b = Object.keys(obj1).length < Object.keys(obj2).length ? obj1 : obj2;

   return Object.keys(a).every( key => {

      if(isNullOrUndefined(a[key])){

         return isNullOrUndefined(b[key] );
      }

     if(isNullOrUndefined(b[key]))
       return false;

     if(typeof a[key] === 'object'){

       if(typeof b[key] !== 'object')
         return false;

       return  ObjectHelper.similar(a[key],b[key]);

     }

      if( typeof a[key] !== 'function') {

        return a[key].toString().trim() === b[key].toString().trim();
      }

       return typeof  b[key] !== 'function';
    });

  }
}

export class FileHelper {


  static sToUintArray(str:string){

    const bytes = atob(str);

     const binary = new Uint8Array(new ArrayBuffer(bytes.length));

     for( let i=0; i< bytes.length; i++)
       binary[i] = bytes.charCodeAt(i);

     return binary;
  }

}

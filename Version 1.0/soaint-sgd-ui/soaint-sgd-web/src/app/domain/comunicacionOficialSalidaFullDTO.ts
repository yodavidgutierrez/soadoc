export interface ComunicacionOficialSalidaFullDTO {
   nroRadicado:string;
   fechaRadicacion:Date;
   dependencia:string;
    nombre:string;
   tipoDocumento:string;
   nroIdentificacion:string;
    pais:string;
    departamento:string;
    municipio:string;
    ciudad:string;
   direccion:string;
   folios:number;
   anexos:number;
   peso?:string;
   nroGuia?:string;
   valorEnvio?:string;
   idAgente:number;
   idDocumento:number;


}

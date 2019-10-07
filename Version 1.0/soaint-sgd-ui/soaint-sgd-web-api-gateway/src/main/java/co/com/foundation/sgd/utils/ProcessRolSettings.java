package co.com.foundation.sgd.utils;

import co.com.foundation.sgd.dto.ProcessRol;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;

import java.util.Arrays;
import java.util.List;

public class ProcessRolSettings {

    private static List<ProcessRol>  roles = Arrays.asList(
         new ProcessRol("proceso.correspondencia-entrada", "Radicador",true,"Correspondencia de Entrada"),
         new ProcessRol("proceso.correspondencia-salida", "Radicador",true,"Correspondencia de Salida"),
         new ProcessRol("process.archivar-documento", "Solicitante",true,"Organización y Archivo"),
         new ProcessRol("proceso.produccion-multiples-documentos", "Proyector",true,"Producción de múltiples documentos"),
         new ProcessRol("proceso.transferencia-documentales", "Aprobador_dependencia",true,"Transferencias documentales"),
         new ProcessRol("proceso.gestion-unidades-documentales","Archivista",true,"Gestión de unidades documentales"),
         new ProcessRol("proceso.produccion-documental",null,false,"Producción Documental"),
         new ProcessRol("proceso.recibir-gestionar-doc",null,false,"Recibir y gestionar documentos"),
         new ProcessRol("proceso.gestion-planillas",null,false,"Gestion de Panillas de Entrada"),
         new ProcessRol("proceso.gestion-planillas-salida",null,false,"Gestion de Panillas de Salida"),
         new ProcessRol("proceso.gestor-devoluciones",null,false,"Gestionar Devoluciones"),
         new ProcessRol("proceso.gestion-planillas-interna",null,false,"Gestion de Panillas Interna"),
         new ProcessRol("proceso.Gestion_de_Constitucionalidad","Sala_plenapoc",true,"Gestión de Constitucionalidad"),
         new ProcessRol("proceso.Gestion_de_Nulidad","Consejos_de_estadopoc",true,"Gestión de Nulidad")


    );

    public  static List<ProcessRol> GetProcesoPorDemanda(){
        return roles;
    }
}

package co.com.foundation.sgd.utils;

import co.com.soaint.foundation.canonical.correspondencia.RolDTO;

import java.util.Arrays;
import java.util.List;

public class RolesList {

    private static List<RolDTO> listRoles = Arrays.asList(
            new RolDTO("Administrador"),
            new RolDTO("Proyector"),
            new RolDTO("Archivista"),
            new RolDTO("Archivista_AC"),
            new RolDTO("Revisor"),
            new RolDTO("Aprobador"),
            new RolDTO("Aprobador_dependencia"),
            new RolDTO("Auxiliar_correspondencia"),
            new RolDTO("Solicitante"),
            new RolDTO("Gestor_devoluciones"),
            new RolDTO("Radicador"),
            new RolDTO("Radicador_contingencia"),
            new RolDTO("Asignador"),
            new RolDTO("Digitalizador"),
            new RolDTO("Proyector_radicador"),
            new RolDTO("Receptor"),
            new RolDTO("Archivista-AG"),
            new RolDTO("Archivista-AC"),
            new RolDTO("Kpi_Administrador"),
            new RolDTO("Archivista-tramitadorAC"),
            new RolDTO("Archivista-tramitadorAG"),
            new RolDTO("Aprobador-solicitudAG"),
            new RolDTO("Aprobador-solicitudAC"),
            new RolDTO("Consulta público"),
            new RolDTO("Consulta reservado"),
            new RolDTO("Consulta clasificado"),
            new RolDTO("Consulta_KPI")
  /*        new RolDTO("Consejos_de_estadopoc"),
            new RolDTO("Demandantepoc"),
            new RolDTO("Demandadopoc"),
            new RolDTO("Secretaria_Generalpoc"),
            new RolDTO("Sala_plenapoc"),
            new RolDTO("Magistrados_Sustanciadorespoc"),
            new RolDTO("Ciudadanopoc")*/

    );

    private RolesList(){}

    public static  List<RolDTO> getListaRoles(){

        return  listRoles;
    }
}

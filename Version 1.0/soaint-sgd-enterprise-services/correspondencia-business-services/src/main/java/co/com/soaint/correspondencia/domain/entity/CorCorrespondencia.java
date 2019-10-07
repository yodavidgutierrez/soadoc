/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "COR_CORRESPONDENCIA")
@NamedQueries({
        @NamedQuery(name = "CorCorrespondencia.findAll", query = "SELECT c FROM CorCorrespondencia c"),
        @NamedQuery(name = "CorCorrespondencia.findByNroRadicadoObj", query = "SELECT cc FROM CorCorrespondencia cc inner join CorRadicado cr on cr.id = cc.corRadicado.id " +
                "WHERE cc.corRadicado.nroRadicado Like :NRO_RADICADO"),
        @NamedQuery(name = "CorCorrespondencia.findByNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado, c.adjuntarDocumento) " +
                "FROM CorCorrespondencia c WHERE TRIM(c.corRadicado.nroRadicado) like TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findIfDevolucionByNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstanciaDevolucion, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c WHERE TRIM(c.corRadicado.nroRadicado) like TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado, c.codClaseEnvio, c.codModalidadEnvio) " +
                "FROM CorCorrespondencia c WHERE c.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "CorCorrespondencia.getIdeInstanciaPorRadicado", query = "SELECT c.ideInstancia " +
                "FROM CorCorrespondencia c WHERE c.corRadicado.nroRadicado = :NRO_RADICADO"),
        @NamedQuery(name = "CorCorrespondencia.getIdeInstanciaDevolucionPorRadicado", query = "SELECT c.ideInstanciaDevolucion " +
                "FROM CorCorrespondencia c WHERE c.corRadicado.nroRadicado = :NRO_RADICADO"),
        @NamedQuery(name = "CorCorrespondencia.findByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList a " +
                "WHERE a.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicadoAndIdFunci", query = "SELECT distinct NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "WHERE c.corRadicado.fechaRadicacion BETWEEN :FECHA_INI AND :FECHA_FIN and ca.codTipAgent = 'TP-AGEI' " +
                "AND (ca.codEstado = :COD_ESTADO or ca.codEstado = 'DV') and  c.codEstado = :COD_ESTADO " +
                "AND ca.codDependencia = :COD_DEPENDENCIA AND ca.codTipoRemite = :COD_TIPO_REMITENTE " +
                "AND (c.corRadicado.nroRadicado LIKE :NRO_RADICADO) and ca.ideFunci =:ID_FUNCI and (c.codTipoCmc = 'EE' or (c.codTipoCmc = 'SI' and c.reqDistElectronica = '1')) "),
        @NamedQuery(name = "CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstadoAndNroRadicado", query = "SELECT distinct NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "WHERE c.corRadicado.fechaRadicacion BETWEEN :FECHA_INI AND :FECHA_FIN and ca.codTipAgent = 'TP-AGEI' " +
                "AND (ca.codEstado = :COD_ESTADO or ca.codEstado = 'DV') and  c.codEstado <> :REGISTRADO " +
                "AND ca.codDependencia = :COD_DEPENDENCIA AND ca.codTipoRemite = :COD_TIPO_REMITENTE " +
                "AND (c.corRadicado.nroRadicado LIKE :NRO_RADICADO) and (c.codTipoCmc = 'EE' or (c.codTipoCmc = 'SI' and c.reqDistElectronica = '1')) "),
        @NamedQuery(name = "CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodEstado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                //"INNER JOIN CorAgente ca on ca.corCorrespondencia.ideDocumento = c.ideDocumento " +
                "INNER JOIN c.corAgenteList ca " +
                "WHERE c.corRadicado.fechaRadicacion BETWEEN :FECHA_INI AND :FECHA_FIN " +
                "AND c.codEstado = :COD_ESTADO " +
                "AND c.codDependencia= :COD_DEPENDENCIA AND ca.codTipoRemite = :COD_TIPO_REMITENTE "),
        @NamedQuery(name = "CorCorrespondencia.findByPeriodoAndCodDependenciaAndCodTipoDocAndNroRadicado", query = "SELECT distinct NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.ppdDocumentoList d " +
                "INNER JOIN c.corAgenteList ca " +
                "WHERE c.corRadicado.fechaRadicacion BETWEEN :FECHA_INI AND :FECHA_FIN " +
                "AND c.reqDistFisica = :REQ_DIST_FISICA AND ca.codDependencia = :COD_DEPENDENCIA AND ca.codTipAgent = :COD_TIP_AGENT " +
                "AND ca.estadoDistribucion = :ESTADO_DISTRIBUCION and c.codTipoCmc = 'EE' " +
                "AND (:COD_TIPO_DOC IS NULL OR d.codTipoDoc = :COD_TIPO_DOC) AND (:NRO_RADICADO IS NULL OR c.corRadicado.nroRadicado LIKE :NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisicaNroPlantillaNoAsociado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(c.ideDocumento, c.descripcion, c.tiempoRespuesta, c.codUnidadTiempo, c.codMedioRecepcion, c.corRadicado.fechaRadicacion, " +
                "c.corRadicado.nroRadicado, c.codTipoCmc, c.reqDistFisica, c.ideInstancia, c.codFuncRadica, " +
                "c.codSede, c.codDependencia, c.reqDigita, c.nroGuia, c.codEmpMsj, c.fecVenGestion, c.codEstado, c.codClaseEnvio, c.codModalidadEnvio) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "INNER JOIN c.ppdDocumentoList d " +
                "WHERE c.reqDistFisica = :REQ_DIST_FISICA AND ((:TIPO_COM1 IS NULL OR c.codTipoCmc = :TIPO_COM1) OR (:TIPO_COM2 IS NULL OR c.codTipoCmc = :TIPO_COM2)) " +
                "AND ((:FECHA_INI IS NULL OR c.corRadicado.fechaRadicacion >= :FECHA_INI) AND (:FECHA_FIN IS NULL OR c.corRadicado.fechaRadicacion < :FECHA_FIN)) " +
                "AND c.codClaseEnvio = :CLASE_ENVIO AND c.codModalidadEnvio = :MOD_ENVIO " +
                "AND (:COD_DEPENDENCIA IS NULL OR ca.codDependencia = :COD_DEPENDENCIA) " +
                "AND (ca.estadoDistribucion IS NULL OR  ca.estadoDistribucion = :ESTADO_DISTRIBUCION)" +
                " AND ca.codTipAgent = :TIPO_AGENTE " +
                "AND (:NRO_RADICADO IS NULL OR c.corRadicado.nroRadicado LIKE :NRO_RADICADO) " +
                "ORDER BY c.codDependencia, c.codTipoCmc, c.corRadicado.nroRadicado"),
        @NamedQuery(name = "CorCorrespondencia.findIdeDocumentoByNroRadicado", query = "SELECT c.ideDocumento " +
                "FROM CorCorrespondencia c " +
                "WHERE TRIM(c.corRadicado.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.findCorrespondenciasContingencia", query = "SELECT distinct NEW co.com.soaint.foundation.canonical.correspondencia.CorrespondenciaDTO " +
                "(cor.nroRadicado, ca.codSede, ca.codDependencia) " +
                "FROM CorCorrespondencia c inner join CorRadicado cor on c.corRadicado.id = cor.id " +
                "inner join c.corAgenteList ca " +
                "WHERE c.contingencia = true and ca.indOriginal = 'TP-DESP' "),
        @NamedQuery(name = "CorCorrespondencia.countByNroRadicado", query = "SELECT COUNT(c) " +
                "FROM CorCorrespondencia c " +
                "WHERE TRIM(c.corRadicado.nroRadicado) like TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.maxNroRadicadoByCodSedeAndCodTipoCMC", query = "SELECT MAX(c.corRadicado.nroRadicado) " +
                "FROM CorCorrespondencia c " +
                "WHERE TRIM(c.codSede) = TRIM(:COD_SEDE) AND TRIM(c.codTipoCmc) = TRIM(:COD_TIPO_CMC) " +
                "AND NOT c.corRadicado.nroRadicado BETWEEN :RESERVADO_INI AND :RESERVADO_FIN "),
        @NamedQuery(name = "CorCorrespondencia.findFechaVenGestionByIdeDocumento", query = "SELECT c.fecVenGestion " +
                "FROM CorCorrespondencia c " +
                "WHERE c.ideDocumento = :IDE_DOCUMENTO "),
        @NamedQuery(name = "CorCorrespondencia.findRadicadoFull", query = "SELECT concat( c.corRadicado.nroRadicado, concat(',',c.codDependencia) ) " +
                "FROM CorCorrespondencia c " +
                "WHERE c.corRadicado.nroRadicado like :NRO_RADICADO "),
        @NamedQuery(name = "CorCorrespondencia.updateEstado", query = "UPDATE CorCorrespondencia c " +
                "SET c.codEstado = :COD_ESTADO " +
                "WHERE TRIM(c.corRadicado.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.updateRadicacionSalida", query = "UPDATE CorCorrespondencia c " +
                "SET c.fecGuia =:FECHA_GUIA, c.proveedor =:PROVEEDOR, c.peso = :PESO, c.valorEnvio = :VALOR_ENVIO " +
                "WHERE TRIM(c.corRadicado.nroRadicado) = TRIM(:NRO_RADICADO)"),

        @NamedQuery(name = "CorCorrespondencia.consultaDocumentos", query = "SELECT DISTINCT NEW co.com.soaint.foundation.canonical.correspondencia.ModeloConsultaDocumentoDTO " +
                "(cor.ideDocumento, case when ppd.codTipoDoc is not null then (select con.nombre from TvsConstantes con where lower(con.codigo) = lower(ppd.codTipoDoc)) else '' end, " +
                "rad.nroRadicado, rad.fechaRadicacion, cor.nroGuia, cor.codEstado, cor.fecVenGestion, cor.estadoEntrega, cor.codTipoCmc, cor.codDependencia) " +
                "FROM CorCorrespondencia cor " +
                "INNER JOIN cor.corAgenteList ca INNER JOIN cor.ppdDocumentoList ppd " +
                "INNER JOIN CorRadicado rad on rad.id = cor.corRadicado.id " +
                "WHERE (:TIPO_COM is null or cor.codTipoCmc = :TIPO_COM) " +
                "and (:NRO_RADICADO is null or rad.nroRadicado like :NRO_RADICADO) " +
                "and (:NRO_IDENT is null or ca.nroDocuIdentidad like :NRO_IDENT) " +
                "and (:DEP_ORIGEN is null or cor.codDependencia = :DEP_ORIGEN) " +
                "and (:DEP_DEST is null or ca.codDependencia = :DEP_DEST) " +
                "and (:NRO_GUIA is null or cor.nroGuia = :NRO_GUIA) " +
                "and (:SOLICITANTE is null or lower(ca.nombre) like :SOLICITANTE) " +
                "and (:ASUNTO is null or lower(ppd.asunto) like :ASUNTO) " +
                "and (:TIPO_DOC is null or ca.codTipDocIdent = :TIPO_DOC) " +
                "and (:TIPOLOGIA is null or ppd.codTipoDoc = :TIPOLOGIA) "),
        //esta es la query de los que han sido aceptados para salida externa
        @NamedQuery(name = "CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisica", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialSalidaDTO " +
                "(ca.ideAgente, c.ideDocumento,ca.codDependencia,c.corRadicado.nroRadicado, c.corRadicado.fechaRadicacion, ca.nombre, ppd.codTipoDoc, ca.nroDocuIdentidad, " +
                "  dc.codPais, dc.codDepartamento, dc.codMunicipio, dc.ciudad, dc.direccion, ppd.nroFolios, ppd.nroAnexos, ppd.asunto, c.codTipoCmc) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "INNER JOIN c.ppdDocumentoList ppd " +
                "LEFT JOIN TvsDatosContacto dc ON ca.ideAgente = dc.corAgente.ideAgente " +
                "WHERE c.reqDistFisica = '1' AND c.codTipoCmc = :TIPO_COM AND (c.codModalidadEnvio=:COD_MOD_ENVIO or c.codModalidadEnvio is null ) AND (c.codClaseEnvio =:COD_CLASE_ENVIO or c.codClaseEnvio is null )  and c.aceptado = 1 " +
                "AND ca.codDependencia like :COD_DEPENDENCIA and ca.estadoDistribucion = 'SD' and ca.codTipoRemite ='INT' and ca.indOriginal is null AND ppd.codTipoDoc LIKE :TIPO_DOC AND c.corRadicado.nroRadicado LIKE :NRO_RADICADO " +
                "AND c.corRadicado.fechaRadicacion BETWEEN :FECHA_INI and :FECHA_FIN "),
        //esta es la query de los que han sido aceptados para salida interna
        @NamedQuery(name = "CorCorrespondencia.findByComunicacionsSalidaInternaConDistribucionFisica", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialSalidaDTO " +
                "(ca.ideAgente, c.ideDocumento,(select cora.codDependencia from CorAgente cora inner join cora.corCorrespondenciaList corresp where cora.codTipAgent = 'TP-AGEE' and corresp.ideDocumento = c.ideDocumento)," +
                "c.corRadicado.nroRadicado, c.corRadicado.fechaRadicacion, ca.nombre, ppd.codTipoDoc, ca.nroDocuIdentidad, " +
                " dc.codPais, dc.codDepartamento, dc.codMunicipio, dc.ciudad, dc.direccion, ppd.nroFolios, ppd.nroAnexos, ppd.asunto, c.codTipoCmc) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "INNER JOIN c.ppdDocumentoList ppd " +
                "LEFT JOIN TvsDatosContacto dc ON ca.ideAgente = dc.corAgente.ideAgente " +
                "WHERE c.reqDistFisica = '1' AND c.codTipoCmc = :TIPO_COM  and c.aceptado = 1 and ca.codTipoRemite ='INT' " +
                "AND ca.codDependencia like :COD_DEPENDENCIA and ca.estadoDistribucion = 'SD' AND ppd.codTipoDoc LIKE :TIPO_DOC AND c.corRadicado.nroRadicado LIKE :NRO_RADICADO " +
                "AND c.corRadicado.fechaRadicacion BETWEEN :FECHA_INI and :FECHA_FIN and ca.codTipAgent = 'TP-AGEI' "),
        //esta es la query de los que no han sido aceptados
        @NamedQuery(name = "CorCorrespondencia.findByComunicacionsSalidaConDistribucionFisicaSinDistribucion", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.ComunicacionOficialSalidaDTO " +
                "(ca.ideAgente, c.ideDocumento,ca.codDependencia,c.corRadicado.nroRadicado, c.corRadicado.fechaRadicacion, ca.nombre, ppd.codTipoDoc, ca.nroDocuIdentidad, " +
                "  dc.codPais, dc.codDepartamento, dc.codMunicipio, dc.ciudad, dc.direccion, ppd.nroFolios, ppd.nroAnexos, ppd.asunto, c.codTipoCmc) " +
                "FROM CorCorrespondencia c " +
                "INNER JOIN c.corAgenteList ca " +
                "INNER JOIN c.ppdDocumentoList ppd " +
                "LEFT JOIN TvsDatosContacto dc ON ca.ideAgente = dc.corAgente.ideAgente " +
                "WHERE c.reqDistFisica = '1' AND (c.codTipoCmc ='SE' OR c.codTipoCmc ='SI') and (c.aceptado is null or c.aceptado <> 1) " +
                "AND ca.codDependencia like :COD_DEPENDENCIA and ca.codTipAgent = 'TP-AGEE' and ca.estadoDistribucion = 'SD' and ca.codTipoRemite ='INT' and ca.indOriginal is null " +
                "AND ppd.codTipoDoc LIKE :TIPO_DOC AND c.corRadicado.nroRadicado LIKE :NRO_RADICADO AND c.corRadicado.fechaRadicacion BETWEEN :FECHA_INI and :FECHA_FIN "),

        @NamedQuery(name = "CorCorrespondencia.updateIdeInstancia", query = "UPDATE CorCorrespondencia c " +
                "SET c.ideInstancia = :IDE_INSTANCIA " +
                "WHERE TRIM(c.corRadicado.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "CorCorrespondencia.updateIdeInstanciaDevolucion", query = "UPDATE CorCorrespondencia c " +
                "SET c.ideInstanciaDevolucion= :IDE_INSTANCIA " +
                "WHERE TRIM(c.corRadicado.nroRadicado) = TRIM(:NRO_RADICADO)")})
@javax.persistence.TableGenerator(name = "COR_CORRESPONDENCIA_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_CORRESPONDENCIA_SEQ", allocationSize = 1)
public class CorCorrespondencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_CORRESPONDENCIA_GENERATOR")
    @Column(name = "IDE_DOCUMENTO")
    private BigInteger ideDocumento;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "TIEMPO_RESPUESTA")
    private String tiempoRespuesta;
    @Column(name = "COD_UNIDAD_TIEMPO")
    private String codUnidadTiempo;
    @Column(name = "COD_MEDIO_RECEPCION")
    private String codMedioRecepcion;
    @Column(name = "CONTINGENCIA")
    private boolean contingencia;
    @Column(name = "ACEPTADO")
    private BigInteger aceptado;
    @Column(name = "COD_TIPO_CMC")
    private String codTipoCmc;
    @Column(name = "IDE_INSTANCIA")
    private String ideInstancia;
    @Column(name = "IDE_INSTANCIA_DEVOLUCION")
    private String ideInstanciaDevolucion;
    @Column(name = "REQ_DIST_FISICA")
    private String reqDistFisica;
    @Column(name = "REQ_DIST_ELECTRONICA")
    private String reqDistElectronica;
    @Column(name = "COD_FUNC_RADICA")
    private String codFuncRadica;
    @Column(name = "COD_SEDE")
    private String codSede;
    @Column(name = "COD_DEPENDENCIA")
    private String codDependencia;
    @Column(name = "REQ_DIGITA")
    private String reqDigita;
    @Column(name = "ADJ_DOC")
    private String adjuntarDocumento;
    @Column(name = "COD_EMP_MSJ")
    private String codEmpMsj;
    @Column(name = "NRO_GUIA")
    private String nroGuia;
    @Column(name = "FEC_VEN_GESTION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecVenGestion;
    @Column(name = "FEC_ENVIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEnvio;
    @Column(name = "COD_ESTADO")
    private String codEstado;
    @Column(name = "COD_CLASE_ENVIO")
    private String codClaseEnvio;
    @Column(name = "COD_MODALIDAD_ENVIO")
    private String codModalidadEnvio;
    @Column(name = "FEC_GUIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecGuia;
    @Column(name = "PROVEEDOR")
    private String proveedor;
    @Column(name = "PESO")
    private String peso;
    @Column(name = "VALOR_ENVIO")
    private String valorEnvio;
    @Column(name = "FEC_ENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecEntrega;
    @Column(name = "ESTADO_ENTREGA")
    private String estadoEntrega;
    @Column(name = "OBSERVACIONES_ENTREGA")
    private String observacionesEntrega;

    @JoinColumn(name = "ID", referencedColumnName = "ID")
    @OneToOne(cascade = CascadeType.ALL)
    private CorRadicado corRadicado;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "COR_CORRESPONDENCIA_AGENTE",
            joinColumns = {@JoinColumn(name = "IDE_DOCUMENTO")},
            inverseJoinColumns = {@JoinColumn(name = "IDE_AGENTE")}
    )
    private Set<CorAgente> corAgenteList = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<DctAsignacion> dctAsignacionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<CorPlanAgen> corPlanAgenList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<PpdDocumento> ppdDocumentoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<DctAsigUltimo> dctAsigUltimoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corCorrespondencia", orphanRemoval = true)
    private List<CorReferido> corReferidoList = new ArrayList<>();

    public void addCorAgente(CorAgente corAgente) {
        if (corAgente == null) {
            return;
        }
        if (null == corAgenteList) {
            corAgenteList = new HashSet<>();
        }
        corAgenteList.add(corAgente);
        if (null == corAgente.getCorCorrespondenciaList()) {
            corAgente.setCorCorrespondenciaList(new HashSet<>());
        }
        corAgente.getCorCorrespondenciaList().add(this);
    }

    public void addPpdDocumento(PpdDocumento ppdDocumento) {
        if (ppdDocumento == null) {
            return;
        }
        if (null == ppdDocumentoList) {
            ppdDocumentoList = new ArrayList<>();
        }
        ppdDocumentoList.add(ppdDocumento);
        ppdDocumento.setCorCorrespondencia(this);
    }

    public void addCorReferido(CorReferido corReferido) {
        if (corReferido == null) {
            return;
        }
        if (null == corReferidoList) {
            corReferidoList = new ArrayList<>();
        }
        corReferidoList.add(corReferido);
        corReferido.setCorCorrespondencia(this);
    }

    public void addDctAsignacion(DctAsignacion dctAsignacion) {
        if (dctAsignacion == null) {
            return;
        }
        if (null == dctAsignacionList) {
            dctAsignacionList = new ArrayList<>();
        }
        dctAsignacionList.add(dctAsignacion);
        dctAsignacion.setCorCorrespondencia(this);
    }

    public void addDctAsigUltimo(DctAsigUltimo dctAsigUltimo) {
        if (dctAsigUltimo == null) {
            return;
        }
        if (null == dctAsigUltimoList) {
            dctAsigUltimoList = new ArrayList<>();
        }
        dctAsigUltimoList.add(dctAsigUltimo);
        dctAsigUltimo.setCorCorrespondencia(this);
    }
}

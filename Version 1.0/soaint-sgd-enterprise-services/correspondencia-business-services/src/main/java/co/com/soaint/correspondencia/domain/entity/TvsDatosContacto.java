/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.soaint.correspondencia.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "TVS_DATOS_CONTACTO")
@NamedQueries({
        @NamedQuery(name = "TvsDatosContacto.findAll", query = "SELECT t FROM TvsDatosContacto t"),
        @NamedQuery(name = "TvsDatosContacto.findAllbyIdAgenteObj", query = "SELECT t FROM TvsDatosContacto t inner join CorAgente ca on t.corAgente.ideAgente = ca.ideAgente " +
                "where t.corAgente.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "TvsDatosContacto.deleteByAgentId", query = "DELETE FROM TvsDatosContacto t WHERE t.corAgente.ideAgente = :ID_AGENTE"),
        @NamedQuery(name = "TvsDatosContacto.findByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO " +
                "(t.ideContacto,  t.nroViaGeneradora, t.nroPlaca, t.codTipoVia, t.codPrefijoCuadrant, t.codPostal, t.direccion, t.celular, " +
                "t.telFijo, t.extension, t.corrElectronico, t.codPais, t.codDepartamento, t.codMunicipio, " +
                "t.provEstado, t.principal, t.ciudad, t.tipoContacto) " +
                "FROM TvsDatosContacto t INNER JOIN CorAgente ca on t.corAgente.ideAgente = ca.ideAgente " +
                "WHERE t.corAgente.ideAgente = :IDE_AGENTE"),
        @NamedQuery(name = "TvsDatosContacto.findPrincipalByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO " +
                "(t.ideContacto,  t.nroViaGeneradora, t.nroPlaca, t.codTipoVia, t.codPrefijoCuadrant, t.codPostal, t.direccion, t.celular, " +
                "t.telFijo, t.extension, t.corrElectronico, t.codPais, t.codDepartamento, t.codMunicipio, " +
                "t.provEstado, t.principal, t.ciudad, t.tipoContacto) " +
                "FROM TvsDatosContacto t INNER JOIN CorAgente ca on t.corAgente.ideAgente = ca.ideAgente " +
                "WHERE t.corAgente.ideAgente = :IDE_AGENTE and t.principal = 'P' "),
        @NamedQuery(name = "TvsDatosContacto.findByNroIdentidadAndTipAgent", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO " +
                "(t.ideContacto,  t.nroViaGeneradora, t.nroPlaca, t.codTipoVia, t.codPrefijoCuadrant, t.codPostal, t.direccion, t.celular, " +
                "t.telFijo, t.extension, t.corrElectronico, t.codPais, t.codDepartamento, t.codMunicipio, " +
                "t.provEstado, t.principal, t.ciudad, t.tipoContacto) " +
                "FROM TvsDatosContacto t INNER JOIN CorAgente ca ON t.corAgente.ideAgente = ca.ideAgente " +
                "WHERE ca.nroDocuIdentidad= :NRO_IDENTIDAD AND ca.codTipoPers = 'TP-PERPN' and ca.codTipDocIdent =:TIPO_DOC"),
        @NamedQuery(name = "TvsDatosContacto.findByNITAndTipAgent", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO " +
                "(t.ideContacto,  t.nroViaGeneradora, t.nroPlaca, t.codTipoVia, t.codPrefijoCuadrant, t.codPostal, t.direccion, t.celular, " +
                "t.telFijo, t.extension, t.corrElectronico, t.codPais, t.codDepartamento, t.codMunicipio, " +
                "t.provEstado, t.principal, t.ciudad, t.tipoContacto) " +
                "FROM TvsDatosContacto t INNER JOIN CorAgente ca on t.corAgente.ideAgente = ca.ideAgente " +
                "WHERE ca.nit = :NIT AND ca.codTipoPers = 'TP-PERPJ' and ca.codTipDocIdent =:TIPO_DOC"),
        @NamedQuery(name = "TvsDatosContacto.findByCorreoElectronico", query = "SELECT t FROM TvsDatosContacto t WHERE t.corrElectronico = :CORREO"),
        @NamedQuery(name = "TvsDatosContacto.findDatosContactoByIdeAgente", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.DatosContactoDTO " +
                "(t.ideContacto,  t.nroViaGeneradora, t.nroPlaca, t.codTipoVia, t.codPrefijoCuadrant, t.codPostal, t.direccion, t.celular, " +
                "t.telFijo, t.extension, t.corrElectronico, t.codPais, t.codDepartamento, t.codMunicipio, " +
                "t.provEstado, t.principal, t.ciudad, t.tipoContacto) " +
                "FROM TvsDatosContacto t INNER JOIN CorAgente ca on t.corAgente.ideAgente = ca.ideAgente " +
                "WHERE ca.ideAgente = :IDE_AGENTE")
})
@javax.persistence.TableGenerator(name = "TVS_DATOS_CONTACTO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "TVS_DATOS_CONTACTO_SEQ", allocationSize = 1)
public class TvsDatosContacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TVS_DATOS_CONTACTO_GENERATOR")
    @Column(name = "IDE_CONTACTO")
    private BigInteger ideContacto;
    @Column(name = "NRO_VIA_GENERADORA")
    private String nroViaGeneradora;
    @Column(name = "NRO_PLACA")
    private String nroPlaca;
    @Column(name = "COD_TIPO_VIA")
    private String codTipoVia;
    @Column(name = "COD_PREFIJO_CUADRANT")
    private String codPrefijoCuadrant;
    @Column(name = "COD_POSTAL")
    private String codPostal;
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "CELULAR")
    private String celular;
    @Column(name = "TEL_FIJO")
    private String telFijo;
    @Column(name = "EXTENSION")
    private String extension;
    @Column(name = "CORR_ELECTRONICO")
    private String corrElectronico;
    @Column(name = "COD_PAIS")
    private String codPais;
    @Column(name = "COD_DEPARTAMENTO")
    private String codDepartamento;
    @Column(name = "COD_MUNICIPIO")
    private String codMunicipio;
    @Column(name = "PROV_ESTADO")
    private String provEstado;
    @Column(name = "PRINCIPAL")
    private String principal;
    @Column(name = "CIUDAD")
    private String ciudad;
    @Column(name = "TIPO_CONTACTO")
    private String tipoContacto;
    @JoinColumn(name = "IDE_AGENTE", referencedColumnName = "IDE_AGENTE")
    @ManyToOne
    private CorAgente corAgente;

}

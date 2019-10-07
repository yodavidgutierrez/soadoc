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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PPD_DOCUMENTO")
@NamedQueries({
        @NamedQuery(name = "PpdDocumento.findAll", query = "SELECT p FROM PpdDocumento p"),
        @NamedQuery(name = "PpdDocumento.findByIdeDocumentoObj", query = "SELECT p FROM PpdDocumento p where p.corCorrespondencia.ideDocumento =:ID_DOC"),
        @NamedQuery(name = "PpdDocumento.findByIdeDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO " +
                "(p.idePpdDocumento, p.codTipoDoc, p.fecDocumento, p.asunto, p.nroFolios, p.nroAnexos, p.codEstDoc, p.ideEcm) " +
                "FROM PpdDocumento p " +
                "INNER JOIN p.corCorrespondencia cor " +
                "WHERE cor.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "PpdDocumento.findIdePpdDocumentoByIdeDocumento", query = "SELECT p.idePpdDocumento " +
                "FROM PpdDocumento p " +
                "INNER JOIN p.corCorrespondencia co " +
                "WHERE co.ideDocumento = :IDE_DOCUMENTO"),
        @NamedQuery(name = "PpdDocumento.findIdePpdDocumentoByNroRadicado", query = "SELECT p.idePpdDocumento " +
                "FROM PpdDocumento p WHERE p.corCorrespondencia.corRadicado.nroRadicado like :NRO_RADICADO"),
        @NamedQuery(name = "PpdDocumento.findPpdDocumentoByNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.PpdDocumentoDTO " +
                "(p.idePpdDocumento, p.codTipoDoc, p.fecDocumento, p.asunto, p.nroFolios, p.nroAnexos, p.codEstDoc, p.ideEcm) " +
                "FROM PpdDocumento p INNER JOIN CorCorrespondencia cc ON p.corCorrespondencia.ideDocumento = cc.ideDocumento " +
                "WHERE TRIM(cc.corRadicado.nroRadicado) = TRIM(:NRO_RADICADO)"),
        @NamedQuery(name = "PpdDocumento.updateIdEcm", query = "UPDATE PpdDocumento p " +
                "SET p.ideEcm = :IDE_ECM " +
                "WHERE p.idePpdDocumento = :IDE_PPDDOCUMENTO")})
@javax.persistence.TableGenerator(name = "PPD_DOCUMENTO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "PPD_DOCUMENTO_SEQ", allocationSize = 1)
public class PpdDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PPD_DOCUMENTO_GENERATOR")
    @Column(name = "IDE_PPD_DOCUMENTO")
    private BigInteger idePpdDocumento;
    @Column(name = "COD_TIPO_DOC")
    private String codTipoDoc;
    @Basic
    @Column(name = "FEC_DOCUMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecDocumento;
    @Column(name = "ASUNTO")
    private String asunto;
    @Column(name = "NRO_FOLIOS")
    private BigInteger nroFolios;
    @Column(name = "NRO_ANEXOS")
    private BigInteger nroAnexos;
    @Column(name = "COD_EST_DOC")
    private String codEstDoc;
    @Basic
    @Column(name = "FEC_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCreacion;
    @Column(name = "IDE_ECM")
    private String ideEcm;

    @JoinColumn(name = "IDE_DOCUMENTO", referencedColumnName = "IDE_DOCUMENTO")
    @ManyToOne
    private CorCorrespondencia corCorrespondencia;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ppdDocumento", orphanRemoval = true)
    private List<PpdTrazDocumento> ppdTrazDocumentoList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ppdDocumento", orphanRemoval = true)
    private List<CorAnexo> corAnexoList = new ArrayList<>();

    public void addCorAnexo(CorAnexo corAnexo) {
        if (null == corAnexo)
            return;
        corAnexoList = null == corAnexoList ? new ArrayList<>() : corAnexoList;
        corAnexoList.add(corAnexoList.size(), corAnexo);
        corAnexo.setPpdDocumento(this);
    }
}

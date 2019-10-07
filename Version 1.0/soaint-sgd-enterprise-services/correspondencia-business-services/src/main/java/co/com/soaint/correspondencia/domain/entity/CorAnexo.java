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


/**
 * @author jrodriguez
 */
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COR_ANEXO")
@NamedQueries({
        @NamedQuery(name = "CorAnexo.findAll", query = "SELECT c FROM CorAnexo c"),
        @NamedQuery(name = "CorAnexo.findAll1", query = "SELECT c FROM CorAnexo c"),
        @NamedQuery(name = "CorAnexo.findByIdePpdDocumento", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AnexoDTO " +
                "(c.ideAnexo, c.codAnexo, c.descripcion, c.codTipoSoporte) " +
                "FROM CorAnexo c " +
                "INNER JOIN c.ppdDocumento pp " +
                "WHERE pp.idePpdDocumento = :IDE_PPD_DOCUMENTO"),
        @NamedQuery(name = "CorAnexo.findAnexosByNroRadicado", query = "SELECT NEW co.com.soaint.foundation.canonical.correspondencia.AnexoFullDTO " +
                "(a.ideAnexo, a.codAnexo, tc.nombre, a.descripcion, a.codTipoSoporte, tsc.nombre) " +
                "FROM CorAnexo a, TvsConstantes tc, TvsConstantes tsc " +
                "INNER JOIN a.ppdDocumento pp " +
                "INNER JOIN pp.corCorrespondencia cor " +
                "WHERE cor.corRadicado.nroRadicado = :NRO_RADICADO AND a.codAnexo = tc.codigo AND a.codTipoSoporte = tsc.codigo"),
        @NamedQuery(name = "CorAnexo.updateAnexo", query = "Update CorAnexo c set c.descripcion=:DESCRIPCION where c.ideAnexo=:ID_ANEXO")
})
@javax.persistence.TableGenerator(name = "COR_ANEXO_GENERATOR", table = "TABLE_GENERATOR", pkColumnName = "SEQ_NAME",
        valueColumnName = "SEQ_VALUE", pkColumnValue = "COR_ANEXO_SEQ", allocationSize = 1)
public class CorAnexo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COR_ANEXO_GENERATOR")
    @Column(name = "IDE_ANEXO")
    private BigInteger ideAnexo;

    @Column(name = "COD_ANEXO")
    private String codAnexo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "COD_TIPO_SOP")
    private String codTipoSoporte;

    @JoinColumn(name = "IDE_PPD_DOCUMENTO", referencedColumnName = "IDE_PPD_DOCUMENTO")
    @ManyToOne
    private PpdDocumento ppdDocumento;
}

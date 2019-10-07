package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.AuditColumns;
import co.com.soaint.correspondencia.domain.entity.Funcionarios;
import co.com.soaint.correspondencia.domain.entity.TvsOrgaAdminXFunciPk;
import co.com.soaint.foundation.canonical.correspondencia.TvsOrgaAdminXFunciPkDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 28-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class TvsOrgaAdminXFunciPkControl extends GenericControl<TvsOrgaAdminXFunciPk> {

    // [fields] -----------------------------------

    public TvsOrgaAdminXFunciPkControl() {
        super(TvsOrgaAdminXFunciPk.class);
    }

    // ----------------------

    @Transactional
    public boolean insertarFuncionarioConCodigoDependencia(TvsOrgaAdminXFunciPkDTO tvsOrga) throws SystemException {

        if (ObjectUtils.isEmpty(tvsOrga)) {
            throw new SystemException("No hay valores para procesar (TvsOrgaAdminXFunciPkDTO = null)");
        }
        final String idFunciString = tvsOrga.getIdFuncionario();
        try {
            final BigInteger idFuncInteger = new BigInteger(idFunciString);
            final Funcionarios funcionarios = em.find(Funcionarios.class, idFuncInteger);
            if (ObjectUtils.isEmpty(funcionarios)) {
                throw new SystemException("Ningun resultado coincide con el id proporcionado ID = '" + idFunciString + "'");
            }
            final List<String> codigos = ObjectUtils.isEmpty(tvsOrga.getCodigos()) ? new ArrayList<>() : tvsOrga.getCodigos();
            final int executeUpdate = em.createNamedQuery("TvsOrgaAdminXFunciPk.deleteAll")
                    .setParameter("ID_FUNCIONARIO", idFuncInteger)
                    .executeUpdate();
            log.info("Rows deleted {} from TvsOrgaAdminXFunciPk.", executeUpdate);
            for (String code : codigos) {
                final AuditColumns auditColumns = new AuditColumns();
                auditColumns.setEstado("A");
                auditColumns.setFecCreacion(new Date());

                final TvsOrgaAdminXFunciPk tvsOrgaAdminXFunciPk = TvsOrgaAdminXFunciPk.newInstance()
                        .codOrgaAdmi(code)
                        .funcionarios(funcionarios)
                        .auditColumns(auditColumns)
                        .build();
                 /*  Query nativeQuery = em.createNativeQuery("INSERT into tvs_orga_admin_x_funci_pk (cod_orga_admi,estado,fec_creacion,ide_funci) " +
                            " VALUES(?,?,?,?)");
                    nativeQuery.setParameter(1, tvsOrgaAdminXFunciPk.getTvsOrgaAdminXFunciPkPk().getCodOrgaAdmi());
                    nativeQuery.setParameter(2, "A");
                    nativeQuery.setParameter(3, new Date());
                    nativeQuery.setParameter(4, funcionarios.getIdeFunci());
                    nativeQuery.executeUpdate();*/
                em.persist(tvsOrgaAdminXFunciPk);

            }
            return true;
        } catch (Exception ex) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage(ex.getMessage())
                    .withRootException(ex)
                    .buildSystemException();
        }
    }
}

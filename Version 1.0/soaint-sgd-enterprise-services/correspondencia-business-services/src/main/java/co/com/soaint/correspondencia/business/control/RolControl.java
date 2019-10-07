package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorRol;
import co.com.soaint.foundation.canonical.correspondencia.RolDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 13-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class RolControl extends GenericControl<CorRol> {

    @Autowired
    private ConstantesControl constantesControl;

    public RolControl() {
        super(CorRol.class);
    }


    public List<RolDTO> listarRoles(){

        List<CorRol> resultQuery = em.createNamedQuery("CorRol.findAll", CorRol.class).getResultList();
        if(ObjectUtils.isEmpty(resultQuery)){
           return new ArrayList<>();
        }
        List<RolDTO> roles = new ArrayList<>();
        for (CorRol rol: resultQuery) {
            RolDTO corRol = RolDTO.newInstance()
                    .rol(rol.getNombre())
                    .build();
            roles.add(corRol);
        }
        return roles;
    }

    public List<RolDTO> listarRolesByIdFuncionario(BigInteger idFunci){

        List<CorRol> resultQuery = em.createNamedQuery("CorRol.findAllByIdFunci", CorRol.class)
                .setParameter("ID_FUNCI", idFunci)
                .getResultList();
        if(ObjectUtils.isEmpty(resultQuery)){
            return new ArrayList<>();
        }
        List<RolDTO> roles = new ArrayList<>();
        for (CorRol rol: resultQuery) {
            RolDTO corRol = RolDTO.newInstance()
                    .rol(rol.getNombre())
                    .build();
            roles.add(corRol);
        }
        return roles;
    }

    public List<RolDTO> listarRolesByLoginName(String loginName){

        List<CorRol> resultQuery = em.createNamedQuery("CorRol.findAllByLoginName", CorRol.class)
                .setParameter("LOGIN_NAME", loginName)
                .getResultList();
        if(ObjectUtils.isEmpty(resultQuery)){
            return new ArrayList<>();
        }
        List<RolDTO> roles = new ArrayList<>();
        for (CorRol rol: resultQuery) {
            RolDTO corRol = RolDTO.newInstance()
                    .rol(rol.getNombre())
                    .build();
            roles.add(corRol);
        }
        log.info("roles----------------------------------------{}",roles);
        return roles;
    }
}

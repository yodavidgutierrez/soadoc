/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.business.massiveloader.interfaces;

import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.CmCargaMasiva;
import co.com.foundation.soaint.documentmanager.persistence.entity.CmRegistroCargaMasiva;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface MassiveLoaderManagerProxy {
    
    List<CmCargaMasiva> listarCargasMasivas() throws SystemException, BusinessException;
    
    List<CmRegistroCargaMasiva> listarRegistrosDeCargaMasiva(Long id) throws SystemException, BusinessException ;
    
    
}

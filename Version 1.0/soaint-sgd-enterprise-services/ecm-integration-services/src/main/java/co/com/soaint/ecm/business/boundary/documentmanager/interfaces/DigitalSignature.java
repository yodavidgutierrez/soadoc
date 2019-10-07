package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.canonical.ecm.FirmaDigitalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;

import java.io.Serializable;

public interface DigitalSignature extends Serializable {

    byte[] signPDF(FirmaDigitalDTO firmaDigitalDTO) throws SystemException;


}

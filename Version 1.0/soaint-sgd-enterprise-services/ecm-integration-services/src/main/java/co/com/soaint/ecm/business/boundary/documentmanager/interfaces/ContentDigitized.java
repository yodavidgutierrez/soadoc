package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

public interface ContentDigitized extends Serializable {

    void processDigitalizedDocuments() throws SystemException;
}

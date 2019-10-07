package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.Session;

import java.io.Serializable;

public interface Connection extends Serializable {

    Session getSession() throws SystemException;
}

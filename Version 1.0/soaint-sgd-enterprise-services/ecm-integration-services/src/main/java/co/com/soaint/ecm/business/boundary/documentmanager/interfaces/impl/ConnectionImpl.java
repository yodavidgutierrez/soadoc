package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Configuracion;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.Connection;
import co.com.soaint.ecm.util.SystemParameters;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Getter
@Log4j2
@Service("connection")
public class ConnectionImpl implements Connection {

    @Autowired
    @Qualifier("configuracion")
    private Configuracion configuracion;

    /**
     * Metodo que obtiene el objeto de conexion que produce Alfresco en conexion
     *
     * @return Objeto de tipo Conexion en este caso devuevle un objeto Session
     */
    @Override
    public Session getSession() throws SystemException {
        log.info("*** obtenerConexion ***");
        try {
            Map<String, String> parameter = new HashMap<>();

            // Credenciales del usuario
            parameter.put(SessionParameter.USER, SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_USER));
            parameter.put(SessionParameter.PASSWORD, SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_PASS));

            // configuracion de conexion
            parameter.put(SessionParameter.ATOMPUB_URL, SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT_ECM));
            parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            parameter.put(SessionParameter.REPOSITORY_ID, configuracion.getPropiedad("REPOSITORY_ID"));

            // Object factory de Alfresco
            parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

            // Crear Sesion
            SessionFactory factory = SessionFactoryImpl.newInstance();
            return factory.getRepositories(parameter).get(0).createSession();

        } catch (Exception e) {
            log.error("Error al crear la conexion con el repositorio de contenidos alfresco {}", e.getMessage());
            throw new SystemException("Error al crear la conexion con el repositorio de contenidos alfresco " + e.getMessage());
        }
    }
}

package co.com.soaint.bpm.services.util;

import co.com.soaint.foundation.canonical.bpm.EntradaProcesoDTO;
import lombok.extern.log4j.Log4j2;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.services.client.api.RemoteRuntimeEngineFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arce on 8/16/2017.
 */
@Service
@PropertySource(value = "classpath:sgd-service.properties")
@Log4j2
public class EngineConexion {
    private static EngineConexion SINGLETON;


    public EngineConexion() {
        //
    }

    public static EngineConexion getInstance() {
        if (SINGLETON == null)
            SINGLETON = new EngineConexion();
        return SINGLETON;
    }

    /**
     * Permite crear la conexion con el servidor JPBM
     *
     * @param entrada Objeto que contiene los parametros de entrada para un proceso
     * @return Objeto conexion JBPM
     * @throws MalformedURLException
     */
    public RuntimeEngine obtenerEngine(EntradaProcesoDTO entrada) throws MalformedURLException {

        return RemoteRuntimeEngineFactory.newRestBuilder()
                .addDeploymentId(entrada.getIdDespliegue()).disableTaskSecurity()
                .addUserName(entrada.getUsuario())
                .addPassword(entrada.getPass())
                .addUrl(new URL("http://".concat(SystemParameters.getParameter(SystemParameters.BUSINESS_PLATFORM_ENDPOINT)).concat("/jbpm-console")))
                .build();
    }

}

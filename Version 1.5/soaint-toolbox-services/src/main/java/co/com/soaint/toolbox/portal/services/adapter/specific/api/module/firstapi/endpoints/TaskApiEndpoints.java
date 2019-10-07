package co.com.soaint.toolbox.portal.services.adapter.specific.api.module.firstapi.endpoints;

import org.springframework.beans.factory.annotation.Value;

public class TaskApiEndpoints {

    @Value("${endpoint.prueba.servicio.buscar}")
    public String ENDPOINT_PRUEBA;
}

package co.com.soaint.toolbox.portal.services.adapter.manager.impl;



import co.com.soaint.toolbox.portal.services.adapter.specific.cmis.util.ClientUtil;
import co.com.soaint.toolbox.portal.services.adapter.specific.infrastructure.EndpointConfig;
import co.com.soaint.toolbox.portal.services.adapter.manager.EndpointManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public abstract class EndpointManagerAbstract implements EndpointManager {

    private EndpointConfig endpointConfig;

    @Autowired
    public EndpointManagerAbstract(EndpointConfig endpointConfig) {
        this.endpointConfig = endpointConfig;
    }


    public ResponseEntity endpointConsumerClient(final String pathEndpoint,
                                                 final Class<?> typeResponse,
                                                 final HttpMethod method,
                                                 final Object body) {

        RestTemplate clientConsumer = new RestTemplate();
        HttpHeaders httpHeadersConsumer = endpointConfig.createAuthenticationHeaders();
        return clientConsumer.exchange(pathEndpoint, method, new HttpEntity<>(body, httpHeadersConsumer), typeResponse);
    }

    public ResponseEntity endpointConsumerClient(final String pathEndpoint,
                                                 final Class<?> typeResponse,
                                                 final HttpMethod method,
                                                 final HashMap<String, String> headers) {

        RestTemplate clientConsumer = new RestTemplate();
        HttpHeaders httpHeadersConsumer = ClientUtil.addHeaders(headers);
        return clientConsumer.exchange(pathEndpoint, method, new HttpEntity<>(httpHeadersConsumer), typeResponse);
    }

    public ResponseEntity endpointConsumerClient(final String pathEndpoint,
                                                 final Class<?> typeResponse,
                                                 final HttpMethod method) {

        RestTemplate clientConsumer = new RestTemplate();
        HttpHeaders httpHeadersConsumer = endpointConfig.createAuthenticationHeaders();
        return clientConsumer.exchange(pathEndpoint, method, new HttpEntity<>(httpHeadersConsumer), typeResponse);
    }

    public ResponseEntity endpointConsumerClient(final String pathEndpoint,
                                                 final Class<?> typeResponse,
                                                 final HttpMethod method,
                                                 final Object body,
                                                 final HashMap<String, String> headers) {

        RestTemplate clientConsumer = new RestTemplate();
        HttpHeaders httpHeadersConsumer = ClientUtil.addHeaders(headers);
        return clientConsumer.exchange(pathEndpoint, method, new HttpEntity<>(body, httpHeadersConsumer), typeResponse);
    }

}

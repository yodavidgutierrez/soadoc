package co.com.soaint.toolbox.portal.services.adapter.manager;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public interface EndpointManager {

    ResponseEntity<?> endpointConsumerClient(final String pathEndpoint,
                                             final Class<?> typeResponse,
                                             final HttpMethod method,
                                             final HashMap<String, String> headers);
}

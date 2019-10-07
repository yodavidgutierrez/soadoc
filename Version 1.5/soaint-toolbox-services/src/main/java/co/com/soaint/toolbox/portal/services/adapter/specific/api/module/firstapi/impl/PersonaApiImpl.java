package co.com.soaint.toolbox.portal.services.adapter.specific.api.module.firstapi.impl;

import co.com.soaint.toolbox.portal.services.adapter.specific.infrastructure.EndpointConfig;
import co.com.soaint.toolbox.portal.services.commons.converter.PersonaConverter;
import co.com.soaint.toolbox.portal.services.commons.domains.generic.PersonaDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.soaint.toolbox.portal.services.adapter.manager.impl.EndpointManagerAbstract;
import co.com.soaint.toolbox.portal.services.adapter.specific.api.module.firstapi.PersonaApiCliente;
import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import co.com.soaint.toolbox.portal.services.commons.domains.response.BaseResponse;
import co.com.soaint.toolbox.portal.services.commons.exception.business.ResponseNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Component
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class PersonaApiImpl extends EndpointManagerAbstract implements PersonaApiCliente {

    @Autowired
    private ObjectMapper mapper;

    public PersonaApiImpl(EndpointConfig endpointConfig) {
        super(endpointConfig);
    }

    @Value("${endpoint.prueba.servicio.buscar}")
    public String ENDPOINT_PRUEBA;


    @Override
    public PersonaDTO create(PersonaDTORequest persona) {
        ResponseEntity<BaseResponse> response = endpointConsumerClient(ENDPOINT_PRUEBA, BaseResponse.class, HttpMethod.POST, persona);
        if (response.getStatusCode() == HttpStatus.CREATED)
            return mapper.convertValue(response.getBody().getBody(), PersonaDTO.class);
        throw new ResponseNotFoundException();
    }

    @Override
    public List<PersonaDTO> personas() {
        ResponseEntity<BaseResponse> response = endpointConsumerClient(ENDPOINT_PRUEBA, BaseResponse.class, HttpMethod.GET);
        return response.getStatusCode() == HttpStatus.OK ?
                (List<PersonaDTO>) response.getBody().getBody() : new ArrayList<>();
    }

    @Override
    public PersonaDTO findPersonaById(Long id) {
        try {
            ResponseEntity<BaseResponse> response = endpointConsumerClient(ENDPOINT_PRUEBA.concat(id.toString()), BaseResponse.class, HttpMethod.GET);
            return response.getStatusCode() == HttpStatus.OK ?
                    PersonaConverter.clientToPersonaDto(response) : new PersonaDTO();
        } catch (HttpClientErrorException e) {
            throw new ResponseNotFoundException();
        }
    }

    @Override
    public PersonaDTO updatePersonaById(Long id, PersonaDTORequest persona) {
        ResponseEntity<BaseResponse> response = endpointConsumerClient(ENDPOINT_PRUEBA.concat(id.toString()), BaseResponse.class, HttpMethod.PUT, persona);
        return response.getStatusCode() == HttpStatus.OK ?
                PersonaConverter.clientToPersonaDto(response) : new PersonaDTO();
    }

    @Override
    public String deletePersonabyId(Long id) {
        ResponseEntity<BaseResponse> reponse = endpointConsumerClient(ENDPOINT_PRUEBA.concat(id.toString()), BaseResponse.class, HttpMethod.DELETE);
        return reponse.getStatusCode() == HttpStatus.OK ?
                (String) reponse.getBody().getBody() : "Fallo al invocar el servicio";
    }

}

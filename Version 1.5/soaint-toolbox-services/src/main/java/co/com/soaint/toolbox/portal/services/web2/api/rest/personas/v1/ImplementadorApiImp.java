package co.com.soaint.toolbox.portal.services.web2.api.rest.personas.v1;


import co.com.soaint.toolbox.portal.services.adapter.specific.api.module.firstapi.PersonaApiCliente;
import co.com.soaint.toolbox.portal.services.commons.constants.api.persona.EndpointPersonaApi;
import co.com.soaint.toolbox.portal.services.commons.domains.generic.PersonaDTO;
import co.com.soaint.toolbox.portal.services.web2.api.rest.ImplementadorApi;
import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import co.com.soaint.toolbox.portal.services.commons.domains.response.builder.ResponseBuilder;
import co.com.soaint.toolbox.portal.services.commons.enums.TransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = EndpointPersonaApi.PERSONA_API_V1)
@CrossOrigin(origins = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class ImplementadorApiImp implements ImplementadorApi {


    final private PersonaApiCliente personaApi;

    @Autowired
    public ImplementadorApiImp(PersonaApiCliente personaApi) {
        this.personaApi = personaApi;
    }


    @PostMapping(value = EndpointPersonaApi.CREATE_PERSONA)
    public ResponseEntity createPersona(@RequestBody PersonaDTORequest persona, HttpServletRequest request) {
        PersonaDTO personaDTO = personaApi.create(persona);
        return ResponseBuilder.newBuilder()
                .withResponse(personaDTO)
                .withStatus(HttpStatus.CREATED)
                .withMessage("Yeah!")
                .withPath(request.getRequestURI())
                .withTransactionState(TransactionState.OK)
                .buildResponse();
    }

    @GetMapping(EndpointPersonaApi.FIND_PERSONAS)
    public ResponseEntity findPersonas(HttpServletRequest request) {
        List<PersonaDTO> personas = personaApi.personas();
        return ResponseBuilder.newBuilder()
                .withResponse(personas)
                .withStatus(HttpStatus.OK)
                .withMessage("Yeah!")
                .withPath(request.getRequestURI())
                .withTransactionState(TransactionState.OK)
                .buildResponse();
    }

    @GetMapping(EndpointPersonaApi.FIND_PERSONAS_BY_ID)
    public ResponseEntity findPersonasById(@PathVariable final Long id, HttpServletRequest request) {
        PersonaDTO persona = personaApi.findPersonaById(id);

        return ResponseBuilder.newBuilder()
                .withResponse(persona)
                .withStatus(HttpStatus.OK)
                .withMessage("Yeah!")
                .withPath(request.getRequestURI())
                .withTransactionState(TransactionState.OK)
                .buildResponse();

    }

    @PutMapping(EndpointPersonaApi.UPDATE_PERSONAS_BY_ID)
    public ResponseEntity updatePersonasById(@PathVariable final Long id, @RequestBody final PersonaDTORequest persona) {
        PersonaDTO personaUpd = personaApi.updatePersonaById(id, persona);

        return ResponseBuilder.newBuilder()
                .withResponse(personaUpd)
                .withStatus(HttpStatus.OK)
                .withMessage("Yeah!")
                .withTransactionState(TransactionState.OK)
                .buildResponse();
    }

    @DeleteMapping(EndpointPersonaApi.DELETE_PERSONA_BY_ID)
    public ResponseEntity deletePersonaById(@PathVariable final Long id, HttpServletRequest request) {
        return ResponseBuilder.newBuilder()
                .withResponse(personaApi.deletePersonabyId(id))
                .withTransactionState(TransactionState.OK)
                .withMessage("Yeah!")
                .withPath(request.getRequestURI())
                .withStatus(HttpStatus.OK)
                .buildResponse();
    }

}

package co.com.soaint.toolbox.portal.services.web.api.rest.personas.v1;


import co.com.soaint.toolbox.portal.services.commons.constants.api.persona.EndpointPersonaApi;
import co.com.soaint.toolbox.portal.services.commons.domains.generic.PersonaDTO;
import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import co.com.soaint.toolbox.portal.services.commons.domains.response.builder.ResponseBuilder;
import co.com.soaint.toolbox.portal.services.commons.enums.TransactionState;
import co.com.soaint.toolbox.portal.services.model.entities.Persona;
import co.com.soaint.toolbox.portal.services.service.implementador.IGestionPersona;
import co.com.soaint.toolbox.portal.services.web.api.rest.ImplementadorApi;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = EndpointPersonaApi.PERSONA_API_V1)
@CrossOrigin(origins = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@Log4j2
public class ImplementadorApiImp implements ImplementadorApi {

    private final IGestionPersona gestionPersona;


    @Autowired
    public ImplementadorApiImp(IGestionPersona gestionPersona) {
        this.gestionPersona = gestionPersona;
    }

    @PostMapping(EndpointPersonaApi.CREATE_PERSONA)
    public ResponseEntity create(@RequestBody PersonaDTORequest persona) {
        log.info("ENTRO AL SERVICIO DE CREAR");
        Optional<PersonaDTO> personaCreated = gestionPersona.registerPersona(persona);

        return ResponseBuilder.newBuilder()
                .withStatus(personaCreated.isPresent() ? HttpStatus.CREATED : HttpStatus.OK)
                .withResponse(personaCreated.isPresent() ? personaCreated : new PersonaDTO())
                .buildResponse();
    }


    @GetMapping(EndpointPersonaApi.FIND_PERSONAS)
    public ResponseEntity findPersonas() {
        log.info("ENTRO AL SERVICIO DE BUSCAR");
        return ResponseBuilder.newBuilder()
                .withStatus(HttpStatus.OK)
                .withResponse(gestionPersona.findPersonas())
                .buildResponse();
    }

    @GetMapping(EndpointPersonaApi.FIND_PERSONAS_BY_ID)
    public ResponseEntity findPersonById(@PathVariable final Long id) {
        log.info("ENTRO AL SERVICIO DE BUSCAR POR ID");
        PersonaDTO persona = gestionPersona.getPersonaById(id);

        if (persona.getId() == null) {
            return ResponseBuilder.newBuilder()
                    .withResponse(Persona.builder().build())
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("El Documento " + id + " no Existe ")
                    .buildResponse();
        }

        return ResponseBuilder.newBuilder()
                .withStatus(HttpStatus.OK)
                .withResponse(persona)
                .withMessage("El Documento " + id + " Existe ")
                .buildResponse();
    }


    @PutMapping(EndpointPersonaApi.UPDATE_PERSONAS_BY_ID)
    public ResponseEntity updatePersonaById(@RequestBody final PersonaDTORequest persona, @PathVariable final Long id) {
        log.info("ENTRO AL SERVICIO DE ACTUALIZAR");
        Optional<PersonaDTO> response = gestionPersona.updatePersona(persona, id);

        if (response.get().getId() == null) {

            return ResponseBuilder.newBuilder()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withResponse(response)
                    .withMessage("El Documento " + id + " no Existe ")
                    .buildResponse();
        }

        return ResponseBuilder.newBuilder()
                .withStatus(HttpStatus.OK)
                .withResponse(response)
                .buildResponse();
    }

    @DeleteMapping(EndpointPersonaApi.DELETE_PERSONA)
    public ResponseEntity deletePersonaById(@PathVariable final Long id) {
        log.info("ENTRO AL SERVICIO DE ELIMINAR");
        return ResponseBuilder.newBuilder()
                .withResponse(gestionPersona.detelePersona(id))
                .withMessage("Funciona!")
                .withStatus(HttpStatus.OK)
                .withTransactionState(TransactionState.OK)
                .buildResponse();
    }


}

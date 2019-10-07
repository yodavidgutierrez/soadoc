package co.com.soaint.toolbox.portal.services.web.api.rest;


import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import org.springframework.http.ResponseEntity;

public interface ImplementadorApi {

    ResponseEntity create(final PersonaDTORequest persona);

    ResponseEntity findPersonas();

    ResponseEntity findPersonById(final Long id);

    ResponseEntity updatePersonaById(final PersonaDTORequest persona, Long id);

    ResponseEntity deletePersonaById(final Long id);




}

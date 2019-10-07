package co.com.soaint.toolbox.portal.services.web2.api.rest;


import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface ImplementadorApi {

    ResponseEntity createPersona(PersonaDTORequest persona, HttpServletRequest request);

    ResponseEntity findPersonas(HttpServletRequest request);

    ResponseEntity findPersonasById(Long id, HttpServletRequest request);

    ResponseEntity updatePersonasById(Long id, PersonaDTORequest persona);

    ResponseEntity deletePersonaById(Long id, HttpServletRequest request);

}

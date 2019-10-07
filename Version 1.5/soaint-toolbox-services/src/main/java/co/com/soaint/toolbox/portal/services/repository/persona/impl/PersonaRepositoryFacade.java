package co.com.soaint.toolbox.portal.services.repository.persona.impl;



import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import co.com.soaint.toolbox.portal.services.model.entities.Persona;

import java.util.Collection;
import java.util.Optional;

public interface PersonaRepositoryFacade {

    Optional<Persona> registerPersona(final PersonaDTORequest persona);

    Optional<Collection<Persona>>findPersonas();

    Optional<Persona> updatePersona(final PersonaDTORequest persona, Long id);

    Optional<Persona> getPersonaById(final Long id);

    Optional<String> deletePersona(final Long id);



}

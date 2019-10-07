package co.com.soaint.toolbox.portal.services.service.implementador;



import co.com.soaint.toolbox.portal.services.commons.domains.generic.PersonaDTO;
import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;

import java.util.Collection;
import java.util.Optional;

public interface IGestionPersona {

    Optional<PersonaDTO> registerPersona(final PersonaDTORequest persona);

    Optional<Collection<PersonaDTO>> findPersonas();

    PersonaDTO getPersonaById(final Long id);

    Optional<PersonaDTO> updatePersona(final PersonaDTORequest persona, final Long id);

    Optional<String> detelePersona(final Long id);



}

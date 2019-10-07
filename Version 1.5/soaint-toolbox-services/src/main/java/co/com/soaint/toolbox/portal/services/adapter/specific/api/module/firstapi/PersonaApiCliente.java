package co.com.soaint.toolbox.portal.services.adapter.specific.api.module.firstapi;



import co.com.soaint.toolbox.portal.services.commons.domains.generic.PersonaDTO;
import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;

import java.util.List;

public interface PersonaApiCliente {

    PersonaDTO create(PersonaDTORequest persona);

    List<PersonaDTO> personas();

    PersonaDTO findPersonaById(Long id);

    PersonaDTO updatePersonaById(Long id, PersonaDTORequest persona);

    String deletePersonabyId(Long id);

}

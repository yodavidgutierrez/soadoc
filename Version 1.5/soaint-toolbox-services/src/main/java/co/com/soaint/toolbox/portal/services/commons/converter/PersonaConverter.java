package co.com.soaint.toolbox.portal.services.commons.converter;


import co.com.soaint.toolbox.portal.services.commons.domains.generic.PersonaDTO;
import co.com.soaint.toolbox.portal.services.commons.domains.response.BaseResponse;
import co.com.soaint.toolbox.portal.services.commons.util.JSONUtil;
import co.com.soaint.toolbox.portal.services.model.entities.Persona;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class PersonaConverter {


    public static PersonaDTO entityToDtoPersona(Optional<Persona> persona) {
        PersonaDTO personaDTO = new PersonaDTO();
        if (persona.isPresent()) {
            personaDTO.setId(persona.get().getId());
            personaDTO.setNombres(persona.get().getNombres());
            personaDTO.setApellidos(persona.get().getApellidos());
            personaDTO.setNumeroIdentificacion(persona.get().getNumeroIdentificacion());
            personaDTO.setTipoIdentificacion(persona.get().getTipoIdentificacion());
            personaDTO.setCreateDate(persona.get().getCreateDate());
        }
        return personaDTO;
    }

    /**
     * Metodo para convertir un ResponseEntity en un objeto de tipo ClientPersonaDTO
     *
     * @param responseEntity respuesta de un cliente
     * @return objeto de tipo ClientPersonaDTO
     */
    public static PersonaDTO clientToPersonaDto(ResponseEntity<BaseResponse> responseEntity) {
        String body = JSONUtil.marshal(responseEntity.getBody().getBody());
        return JSONUtil.unmarshal(body, PersonaDTO.class);
    }


}

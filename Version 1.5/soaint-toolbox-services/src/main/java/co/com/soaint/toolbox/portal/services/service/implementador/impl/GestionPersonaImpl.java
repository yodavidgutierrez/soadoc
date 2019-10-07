package co.com.soaint.toolbox.portal.services.service.implementador.impl;


import co.com.soaint.toolbox.portal.services.commons.converter.PersonaConverter;
import co.com.soaint.toolbox.portal.services.commons.domains.generic.PersonaDTO;
import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import co.com.soaint.toolbox.portal.services.model.entities.Persona;
import co.com.soaint.toolbox.portal.services.repository.persona.impl.PersonaRepositoryFacade;
import co.com.soaint.toolbox.portal.services.service.implementador.IGestionPersona;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GestionPersonaImpl implements IGestionPersona {

    @Autowired
    private ModelMapper modelMapper;

    private final PersonaRepositoryFacade repository;

    @Autowired
    public GestionPersonaImpl(PersonaRepositoryFacade repository) {
        this.repository = repository;
    }


    @Override
    public Optional<PersonaDTO> registerPersona(PersonaDTORequest persona) {
        return Optional.ofNullable(PersonaConverter.entityToDtoPersona(repository.registerPersona(persona)));
    }

    @Override
    public Optional<Collection<PersonaDTO>> findPersonas() {

        return Optional.ofNullable(repository.findPersonas().get().stream()
                .map(persona -> modelMapper.map(persona, PersonaDTO.class))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<PersonaDTO> updatePersona(PersonaDTORequest persona, Long id) {
        return Optional.ofNullable(PersonaConverter.entityToDtoPersona(repository.updatePersona(persona, id)));
    }

    @Override
    public Optional<String> detelePersona(Long id) {
        Optional<String> ms = repository.deletePersona(id);
        return Optional.ofNullable(ms.get());
    }

    @Override
    public PersonaDTO getPersonaById(Long id) {
        Optional<Persona> persona = repository.getPersonaById(id);
        return persona.isPresent() ? modelMapper.map(persona.get(), PersonaDTO.class) : new PersonaDTO();
    }
}

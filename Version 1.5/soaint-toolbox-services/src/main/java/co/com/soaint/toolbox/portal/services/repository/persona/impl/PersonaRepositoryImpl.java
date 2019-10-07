package co.com.soaint.toolbox.portal.services.repository.persona.impl;


import co.com.soaint.toolbox.portal.services.repository.persona.IPersonaRepository;
import co.com.soaint.toolbox.portal.services.commons.domains.request.PersonaDTORequest;
import co.com.soaint.toolbox.portal.services.model.entities.Persona;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Component
@Log4j2
public class PersonaRepositoryImpl implements PersonaRepositoryFacade {

    private final IPersonaRepository repository;

    @Autowired
    public PersonaRepositoryImpl(IPersonaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Persona> registerPersona(PersonaDTORequest persona) {

        log.info("PersonaRepositoryImpl. creando registro de persona : " + persona.toString());
        Persona save = null;
        try {

            save = repository.save(Persona.builder()
                    .tipoIdentificacion(persona.getTipoIdentificacion())
                    .numeroIdentificacion(persona.getNumeroIdentificacion())
                    .nombres(persona.getNombres())
                    .apellidos(persona.getApellidos())
                    .createDate(new Date())
                    .build());



            return Optional.of(save);

        } catch (DataAccessException e) {

            return Optional.of(save);
        }


    }

    @Override
    public Optional<Collection<Persona>> findPersonas() {
        log.info("PersonaRepositoryImpl. consultando personas.");

        Collection<Persona> personas = new ArrayList<>(repository.findAll());
        return Optional.of(personas);
    }

    @Override
    public Optional<Persona> updatePersona(PersonaDTORequest persona, Long id) {

        log.info("PersonaRepositoryImpl. actualizando persona por el id : " + id);

        Persona upPersona = repository.findPersonaByIdPersona(id);

        if (upPersona == null) {
            log.info("PersonaRepositoryImpl. No se encontro la persona por el id : " + id);
            return Optional.of(Persona.builder().build());
        }

        upPersona.setNombres(persona.getNombres());
        upPersona.setApellidos(persona.getApellidos());
        upPersona.setTipoIdentificacion(persona.getTipoIdentificacion());
        upPersona.setNumeroIdentificacion(persona.getNumeroIdentificacion());

        return Optional.of(repository.save(upPersona));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Persona> getPersonaById(Long id) {

        log.info("PersonaRepositoryImpl. consultando persona por el id : " + id);

        return Optional.ofNullable(repository.findById(id).orElse(null));
    }

    @Override
    public Optional<String> deletePersona(Long id) {

        Optional<Persona> persona = getPersonaById(id);

        if (persona.isPresent()) {
            repository.deleteById(id);
            return Optional.ofNullable("Eliminado satisfactoriamente");
        } else {
            return Optional.ofNullable("Registro no encontrado");
        }

    }


}

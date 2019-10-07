package co.com.soaint.toolbox.portal.services.repository.persona;


import co.com.soaint.toolbox.portal.services.model.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonaRepository extends JpaRepository<Persona, Long> {

    @Query("select p from Persona p where p.id = :id")
    Persona findPersonaByIdPersona(@Param("id") final Long id);




}

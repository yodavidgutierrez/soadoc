package co.com.foundation.soaint.documentmanager.business.configuracion;

import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.ConfiguracionInstrumentosMangerProxy;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.documentmanager.persistence.entity.constants.EstadoInstrumentoEnum;
import co.com.foundation.soaint.documentmanager.domain.ItemVO;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmConfigInstrumento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jrodriguez on 27/10/2016.
 */

@BusinessBoundary
public class ConfiguracionInstrumentosManger implements ConfiguracionInstrumentosMangerProxy{

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(DisposicionFinalManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    public ConfiguracionInstrumentosManger(){}

    @Override
    public ItemVO consultarEstadoInstrumento(String instrumento) throws BusinessException, SystemException {

        AdmConfigInstrumento admConfigInstrumento= em.createNamedQuery("AdmConfigInstrumento.findByStatus", AdmConfigInstrumento.class)
                 .setParameter("INSTRUMENTO",instrumento)
                 .getSingleResult();

        if(admConfigInstrumento == null){
         admConfigInstrumento.setIdeInstrumento(instrumento);
         admConfigInstrumento.setEstInstrumento(EstadoInstrumentoEnum.SIN_ESTDO.getId());
         em.persist(admConfigInstrumento);
         em.flush();
        }

        ItemVO item = null;
            switch (admConfigInstrumento.getEstInstrumento()) {
                case 1: {
                    item = new ItemVO(EstadoInstrumentoEnum.CONFIGURACION.getName(), EstadoInstrumentoEnum.CONFIGURACION.getId());
                    break;
                }
                case 2: {
                    item = new ItemVO(EstadoInstrumentoEnum.PUBLICADO.getName(), EstadoInstrumentoEnum.PUBLICADO.getId());
                    break;
                }
                case 0: {
                    item = new ItemVO(EstadoInstrumentoEnum.SIN_ESTDO.getName(), EstadoInstrumentoEnum.SIN_ESTDO.getId());
                    break;
                }
            }
        return item;
    }


    public String consultarEstadosIntrumentos(ItemVO itemInstrumento)throws BusinessException, SystemException {

        String mensaje="ok";

        List<AdmConfigInstrumento> listInstrumentos = em.createNamedQuery("AdmConfigInstrumento.findAll", AdmConfigInstrumento.class)
                .setParameter("INSTRUMENTO", itemInstrumento.getLabel())
                .getResultList();

        for (AdmConfigInstrumento inst : listInstrumentos){
            if(itemInstrumento.getValue().equals(inst.getEstInstrumento())){
                mensaje = MessageUtil.getMessage("instrumentos.instrumentos.validate.estado").replaceAll("#STATUS#",inst.getIdeInstrumento());
            }
        }
        return  mensaje;
    }

    @Override
    public void setEstadoInstrumento(ItemVO item) throws BusinessException, SystemException {
        AdmConfigInstrumento inst =new AdmConfigInstrumento();
        inst.setIdeInstrumento(item.getLabel());
        inst.setEstInstrumento(Integer.parseInt(item.getValue().toString()));
        em.merge(inst);
        em.flush();
    }
}

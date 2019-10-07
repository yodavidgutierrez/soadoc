package co.com.soaint.ecm.component;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentDigitized;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl.RecordServices;
import co.com.soaint.ecm.domain.entity.AccionUsuario;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;

@Log4j2
@Component
@EnableScheduling
public class RecordTaskProcessor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Value("${scheduling.job.cron.enable}")
    private boolean cronFindDigitalizedDocEnable;

    @Autowired
    private RecordServices recordServices;

    @Autowired
    private ContentControl contentControl;

    @Autowired
    private ContentDigitized contentDigitized;

    @Scheduled(cron = "#{'${scheduling.job.cron.custom}' ne '' ? '${scheduling.job.cron.custom}' : configuracion.cronTypeUD.cron}")
    public void tasksExecuter() {
        log.info("Running tasks executer for a day {}", GregorianCalendar.getInstance().getTime());
        autoCloseExecutor();
    }

    @Scheduled(cron = "#{configuracion.cronTypeDD.cron}")
    public void processDigitalizedDocuments() {
        log.info("Running task processDigitalizedDocuments in ECM");
        if (cronFindDigitalizedDocEnable) {
            try {
                contentDigitized.processDigitalizedDocuments();
            } catch (SystemException e) {
                log.error("Ocurrio un error procesar documentos {}");
                log.error("### Mensaje de Error: " + e.getMessage());
            }
        }
    }

    public void autoCloseExecutor() {
        log.info("Buscando Unidades documentales con fecha de cierre para el dia de hoy");
        System.out.println("Buscando Unidades documentales con fecha de cierre para el dia de hoy");
        try {
            final Session session = contentControl.getSession();
            final String query = "SELECT a." + ConstantesECM.RMC_X_AUTO_CIERRE + " AUTO_CLOSE," +
                    " a." + ConstantesECM.RMC_X_IDENTIFICADOR + " UD_ID, a." + PropertyIds.NAME + " UD_NAME" +
                    " FROM rmc:rmarecordFolderCustomProperties a INNER JOIN rma:recordFolder b" +
                    " ON a." + PropertyIds.OBJECT_ID + " = " + "b." + PropertyIds.OBJECT_ID +
                    " WHERE b." + ConstantesECM.RMA_IS_CLOSED + " = false" +
                    " AND (a." + ConstantesECM.RMC_X_AUTO_CIERRE + " IS NOT NULL" +
                    " AND a." + ConstantesECM.RMC_X_AUTO_CIERRE + " NOT IN('', 'none'))";
            final ItemIterable<QueryResult> queryResults = session.query(query, false);

            for (QueryResult queryResult : queryResults) {
                new Thread(() -> {
                    try {
                        final String autoCloseDate = queryResult.getPropertyValueByQueryName("AUTO_CLOSE");
                        final LocalDateTime dateTimeEcm = Utilities.getDispDateTimeEcm(autoCloseDate);
                        final String ud_id = queryResult.getPropertyValueByQueryName("UD_ID");
                        final String ud_name = queryResult.getPropertyValueByQueryName("UD_NAME");

                        if (Utilities.isEqualOrAfterDate(dateTimeEcm)) {
                            log.info("Efectuando cierre para la UD '{}' con ID '{}'", ud_name, ud_id);
                            final UnidadDocumentalDTO dto = new UnidadDocumentalDTO();
                            dto.setAccion(AccionUsuario.CERRAR.getState());
                            dto.setId(ud_id);
                            final MensajeRespuesta respuesta = recordServices
                                    .gestionarUnidadDocumentalECM(dto);
                            log.info(respuesta);
                        }
                    } catch (Exception ex) {
                        log.error("### Mensaje de Error: " + ex.getMessage());
                    }
                }).start();
            }
        } catch (Exception e) {
            log.error("Ocurrio un error al cerrar la unidad documental {}", e);
            log.error("### Mensaje de Error: " + e.getMessage());
        }
    }
}

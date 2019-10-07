package co.com.soaint.ecm.business.boundary.documentmanager.interfaces;

import co.com.soaint.ecm.domain.entity.EcmRecordObjectType;
import co.com.soaint.foundation.canonical.ecm.DisposicionFinalDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.Folder;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by amartinez on 24/01/2018.
 */
public interface IRecordServices {

    /**
     * Permite crear la estructura en el record
     * @param structure objeto que contiene la estructura
     * @return respuesta satisfactoria para la creacion del estructura
     * @throws SystemException
     */
    MensajeRespuesta crearEstructuraEcm(List<EstructuraTrdDTO> structure) throws SystemException;

    /**
     * Metodo para cerrar una unidad documental
     *
     * @param unidadDocumentalDTO  Obj Unidad Documental
     * @return MensajeRespuesta
     */
    MensajeRespuesta gestionarUnidadDocumentalECM(final UnidadDocumentalDTO unidadDocumentalDTO) throws SystemException;

    /**
     * Metodo para abrir/cerrar una unidad documental
     *
     * @param unidadDocumentalDTOS Lista de Unidad Documental
     * @return MensajeRespuesta
     */
    MensajeRespuesta gestionarUnidadesDocumentalesECM(final List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException;

    /**
     * Metodo para Obtener un recordFolder
     *
     * @param idUnidadDocumental     Id Unidad Documental
     * @return Folder folder
     */
    Optional<Folder> getRecordFolderByUdId(String idUnidadDocumental) throws SystemException;

    /**
     * Eliminar carpeta record
     *
     * @param idUnidadDocumental Id de la Ud por el que se hara la busqueda en el ECM
     * @return Void
     */
    void eliminarRecordFolder(String idUnidadDocumental) throws SystemException;

    Response modifyRecord(String idObject, Map<String, Object> propertyMap, EcmRecordObjectType ecmRecordObjectType);

    //Disposicion Final
    MensajeRespuesta listarUdDisposicionFinal(DisposicionFinalDTO disposicionFinalDTO, String dependencyCode) throws SystemException;

    MensajeRespuesta aprobarRechazarDisposicionesFinales(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException;
}

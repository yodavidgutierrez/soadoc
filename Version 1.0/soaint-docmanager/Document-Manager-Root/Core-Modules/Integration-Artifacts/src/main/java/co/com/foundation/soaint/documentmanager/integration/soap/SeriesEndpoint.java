package co.com.foundation.soaint.documentmanager.integration.soap;


import co.com.foundation.soaint.documentmanager.business.series.interfaces.SeriesManagerProxy;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.documentmanager.integration.domain.SerieDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://co.com.foundation.soaint.documentmanager.ws/services")
public class SeriesEndpoint {

    @Autowired
    private SeriesManagerProxy boundary;

    public SeriesEndpoint() {
    }

    // -----------------------------

    @WebMethod(operationName = "validateExistByCodSerieAndCodSubserie", action = "validateExistByCodSerieAndCodSubserie")
    public boolean validateExistByCodSerieAndCodSubserie (@WebParam(name = "codSerie") String codSerie,@WebParam(name = "codSubserie")  String codSubserie)throws SystemException, BusinessException{
       return boundary.validateExistByCodSerieAndCodSubserie(codSerie, codSubserie);
    }


    @WebMethod(operationName = "createSeries",action = "createSeries")
    public void createSeries( @WebParam(name = "serieDTO") SerieDTO serieDTO) throws SystemException,BusinessException {

        //TODO: mover entity-builder a proyecto Business-Artifacts para su reuso en esta capa

        /***
        AdmMotCreacion motCreacion = EntityAdmMotCreacionBuilder.newBuilder()
                .withIdeMotCreacion(serie.getIdMotivo())
                .build();

        EntityAdmSerieBuilder builder = EntityAdmSerieBuilder.newInstance()
                .withActAdministrativo(serie.getActAdministrativo())
                .withCarAdministrativa(serie.getCarAdministrativa())
                .withCarLegal(serie.getCarLegal())
                .withCarTecnica(serie.getCarTecnica())
                .withCodSerie(serie.getCodSerie())
                .withEstSerie(Integer.valueOf(serie.getEstSerie()))
                .withFueBibliografica(serie.getFueBibliografica())
                .withNomSerie(serie.getNomSerie())
                .withNotAlcance(serie.getNotAlcance())
                .withIdeMotCreacion(motCreacion);
        **/
    }


}

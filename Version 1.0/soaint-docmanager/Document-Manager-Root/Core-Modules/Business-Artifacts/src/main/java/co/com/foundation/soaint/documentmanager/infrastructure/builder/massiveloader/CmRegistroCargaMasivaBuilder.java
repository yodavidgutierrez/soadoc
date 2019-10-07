package co.com.foundation.soaint.documentmanager.infrastructure.builder.massiveloader;

import co.com.foundation.soaint.documentmanager.persistence.entity.CmCargaMasiva;
import co.com.foundation.soaint.documentmanager.persistence.entity.CmRegistroCargaMasiva;
import co.com.foundation.soaint.documentmanager.persistence.entity.constants.RegistroCargaMasivaStatus;
import co.com.foundation.soaint.infrastructure.builder.Builder;

/**
 * Created by administrador_1 on 04/10/2016.
 */
public class CmRegistroCargaMasivaBuilder implements Builder<CmRegistroCargaMasiva> {
    
    private CmCargaMasiva cargaMasiva;
    private String contenido;
    private String mensajes;
    private RegistroCargaMasivaStatus estado;

    private CmRegistroCargaMasivaBuilder() {
    }
    
    public static CmRegistroCargaMasivaBuilder newBuilder(){
        return new CmRegistroCargaMasivaBuilder();
    }

    public CmRegistroCargaMasivaBuilder withCargaMasiva(CmCargaMasiva cargaMasiva) {
        this.cargaMasiva = cargaMasiva;
        return this;
    }

    public CmRegistroCargaMasivaBuilder withContenido(String contenido) {
        this.contenido = contenido;
        return this;
    }

    public CmRegistroCargaMasivaBuilder withMensajes(String mensajes) {
        this.mensajes = mensajes;
        return this;
    }

    public CmRegistroCargaMasivaBuilder withEstadoOk() {
        this.estado = RegistroCargaMasivaStatus.COMPLETADO_CORRECTAMENTE;
        return this;
    }

    public CmRegistroCargaMasivaBuilder withEstadoError() {
        this.estado = RegistroCargaMasivaStatus.COMPLETADO_CON_ERRORES;
        return this;
    }

    @Override
    public CmRegistroCargaMasiva build() {
        return new CmRegistroCargaMasiva(cargaMasiva,contenido,mensajes,estado);
    }
}

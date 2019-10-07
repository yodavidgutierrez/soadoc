package co.com.soaint.toolbox.portal.services.adapter.specific.api.module.firstapi.impl;


import co.com.soaint.toolbox.portal.services.commons.domains.response.builder.PersonaDTOResponse;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponsePersonaDTO implements Serializable {

    private PersonaDTOResponse body;
    private String status;
    private String timeResponse;
    private String message;
    private String path;
    private String transactionState;


}

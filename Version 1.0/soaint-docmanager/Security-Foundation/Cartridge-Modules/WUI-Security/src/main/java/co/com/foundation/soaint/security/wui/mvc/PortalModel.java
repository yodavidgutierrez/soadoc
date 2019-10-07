package co.com.foundation.soaint.security.wui.mvc;

import co.com.foundation.soaint.infrastructure.annotations.WebModel;
import co.com.foundation.soaint.security.wui.domain.LoginContext;

import java.io.Serializable;

@WebModel
public class PortalModel implements Serializable {

    private LoginContext context = new LoginContext();

    public PortalModel() {
    }

    public LoginContext getContext() {
        return context;
    }

    public void setContext(LoginContext context) {
        this.context = context;
    }

}

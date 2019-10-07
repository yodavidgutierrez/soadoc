package co.com.foundation.soaint.security.wrapper;

import co.com.foundation.soaint.security.domain.AuthenticationResponseContext;
import co.com.foundation.soaint.security.interfaces.SecurityAuthenticator;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

public class SecurityWrapper implements SecurityAuthenticator{

    private final SecurityAuthenticator authenticator;

    public SecurityWrapper(SecurityAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public AuthenticationResponseContext login(String login, String password) throws SystemException {
        return authenticator.login(login,password);
    }

}

package co.com.foundation.soaint.security.interfaces;

import co.com.foundation.soaint.security.domain.AuthenticationResponseContext;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;

public interface SecurityAuthenticator {

    AuthenticationResponseContext login(String login, String password) throws SystemException;
}

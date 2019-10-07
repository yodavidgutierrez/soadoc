package co.com.foundation.soaint.security.api;


import co.com.foundation.soaint.security.domain.AuthenticationResponseContext;
import co.com.foundation.soaint.security.interfaces.SecurityAuthenticator;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


@WebService(targetNamespace = "http://www.soaint.com/services/security-cartridge/1.0.0")
public class SecurityAPI {

    @Autowired
    @Qualifier("security_wrapper")
    private SecurityAuthenticator authenticator;

    public SecurityAPI() {
    }

    // --------------------------

    @WebMethod(operationName = "verifyCredentials",action = "verifyCredentials")
    public AuthenticationResponseContext verifyCredentials( @WebParam(name = "login") String login,
                                                @WebParam(name = "password") String password) throws SystemException{
        return authenticator.login(login,password);
    }

}

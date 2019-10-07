package co.com.foundation.soaint.documentmanager.adapter;

import co.com.foundation.soaint.documentmanager.integration.client.security.AuthenticationResponseContext;
import co.com.foundation.soaint.documentmanager.integration.client.security.PrincipalContext;
import co.com.foundation.soaint.documentmanager.integration.client.security.SecurityAPI;
import co.com.foundation.soaint.documentmanager.integration.client.security.SystemException_Exception;
import co.com.foundation.soaint.infrastructure.annotations.InfrastructureService;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.security.wui.domain.LoginContext;
import org.apache.cxf.common.util.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@InfrastructureService
public class SecurityClientAdapter {

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(SecurityClientAdapter.class.getName());

    @Autowired
    private SecurityAPI api;

    public SecurityClientAdapter() {
    }

    // ---------------------------

    public LoginContext login( String login, String password )throws SystemException{

        LoginContext loginContext = new LoginContext();

        try {

            LOGGER.info("calling security web service api for user {}",login);

            AuthenticationResponseContext responseContext = api.verifyCredentials(login,password);
            Optional<PrincipalContext> optional = Optional.ofNullable( responseContext.getPrincipalContext() );
            PrincipalContext pc = optional.orElse(new PrincipalContext());

            loginContext.setEmail( pc.getEmail() );
            loginContext.setFirstName( pc.getFirstName() );
            loginContext.setLastName( pc.getLastName() );
            loginContext.setLogged( responseContext.isSuccessful() );
            loginContext.setRoles( pc.getRoles() );
            loginContext.setUsername(pc.getUsername());
            loginContext.setPassword(pc.getPassword());

            if (CollectionUtils.isEmpty(loginContext.getRoles())){
                List<String> roles = new ArrayList<>();
                roles.add("GG-INT-GD-DOCMAGER-ADMIN");
                loginContext.setRoles(roles);
            }


        } catch (SystemException_Exception e)
        {
            LOGGER.error("error calling security api ",e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(e)
                    .buildSystemException();
        }catch (Exception e)
        {
            LOGGER.error("generic error calling security api ",e);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .buildSystemException();
        }

        return loginContext;
    }

}

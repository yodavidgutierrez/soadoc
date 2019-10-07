package co.com.foundation.soaint.security.ldap;

import co.com.foundation.soaint.security.domain.AuthenticationResponseContext;
import co.com.foundation.soaint.security.domain.PrincipalContext;
import co.com.foundation.soaint.security.interfaces.SecurityAuthenticator;
import co.com.foundation.soaint.infrastructure.annotations.InfrastructureService;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.security.infrastructure.builder.PrincipalContextBuilder;
import co.com.foundation.soaint.security.ldap.mapper.PrincipalMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

//https://www.visual-paradigm.com/support/documents/teamcollaborationguide/1337/1341/81289_managinggrou.html

@InfrastructureService
public class LDAPSecurityAuthenticator implements SecurityAuthenticator {

    @Resource(name = "configurationData")
    private Map<String,String> parameters;
    private final LdapTemplate template;

    // [fields] -----------------------------------
    private static final Logger LOGGER = LogManager.getLogger(LDAPSecurityAuthenticator.class.getName());

    @Autowired
    public LDAPSecurityAuthenticator(@Qualifier("ldap_source") LdapContextSource ldapSource) {
        ldapSource.afterPropertiesSet();
        template = new LdapTemplate(ldapSource);
    }

    //[] -------------------------

    @Override
    public AuthenticationResponseContext login(String login, String password) throws SystemException {

        boolean success = true;
        PrincipalContext context = null;

        try {
            String loginExpression = parameters.get("user.unique.identifier").concat("=").concat(login);
            boolean auth = template.authenticate(parameters.get("user.base.dn"), loginExpression, password);
            if (auth) {
                context = queryUserDetails(login, password);
            } else {
                success = false;
                context = PrincipalContextBuilder.newBuilder()
                        .build();
            }
        } catch (Exception e) {
            LOGGER.error("Security - an error has occurred during logging", e);
            success = false;
        }

        return new AuthenticationResponseContext(success, context);
    }

    //[] -------------------------

    private PrincipalContext queryUserDetails(String login, String password) throws NamingException{

        PrincipalContext context = null;
        String loginExpression = parameters.get("user.unique.identifier").concat("=").concat(login);


        PrincipalMapper mapper = PrincipalMapper.newInstance(parameters, password);
        List<PrincipalContext> result = template.search(parameters.get("user.base.dn"), loginExpression, mapper);

        if (!result.isEmpty()) {
            context = result.get(0);
            String roleFinder = mapper.getValue(parameters.get("mapper.roles.finder"));
            context.setRoles(queryUserRoles(roleFinder));
        }

        return context;
    }

    //[] -------------------------

    private List<String> queryUserRoles(String roleFinder) {

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(parameters.get("roles.group.criteria.name"), parameters.get("roles.group.criteria.value")));
        String memberExpression = parameters.get("mapper.roles.finder")
                .concat("=").concat(roleFinder)
                .concat(",").concat(parameters.get("user.base.dn"));

        filter.and(new EqualsFilter(parameters.get("roles.group.member.name"), memberExpression));

        List<String> roles = template.search(parameters.get("group.base.dn"),
                filter.encode(), (AttributesMapper<String>) (Attributes attrs) -> String.valueOf(attrs.get("cn").get()));

        return roles;
    }

}

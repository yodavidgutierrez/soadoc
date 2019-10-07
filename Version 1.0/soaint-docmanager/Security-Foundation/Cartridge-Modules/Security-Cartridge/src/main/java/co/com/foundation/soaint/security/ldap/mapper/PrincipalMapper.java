package co.com.foundation.soaint.security.ldap.mapper;

import co.com.foundation.soaint.security.domain.PrincipalContext;
import co.com.foundation.soaint.security.infrastructure.builder.PrincipalContextBuilder;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class PrincipalMapper  implements AttributesMapper<PrincipalContext> {

    private Map<String, String> parameters;
    private String password;
    private Attributes attributes;

    public static PrincipalMapper newInstance(final Map<String, String> parameters , String password) {
        return new PrincipalMapper(parameters, password);
    }

    private PrincipalMapper(final Map<String, String> parameters, String password) {
        this.parameters = parameters;
        this.password =password;
    }

    public PrincipalContext mapFromAttributes(Attributes attributes) throws NamingException {

        this.attributes = attributes;
        PrincipalContextBuilder builder = PrincipalContextBuilder.newBuilder();
        builder.withEmail(getValue(parameters.get("mapper.email")));
        builder.withFirstName(getValue("cn"));
        builder.withLastName(getValue("sn"));
        builder.withUsername(getValue(parameters.get("mapper.user")));
        builder.withPassword(password);

        return builder.build();
    }

    public String getValue(String name) throws NamingException {

        Attribute attribute = attributes.get(name);

        if (!Objects.isNull(attribute))
            return attribute.get().toString();

        return "";
    }
}

package co.com.foundation.soaint.security.infrastructure.builder;

import co.com.foundation.soaint.security.domain.PrincipalContext;
import co.com.foundation.soaint.infrastructure.builder.Builder;

import java.util.ArrayList;
import java.util.List;

public class PrincipalContextBuilder //implements Builder<PrincipalContext>
{

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;

    private PrincipalContextBuilder() {
        roles = new ArrayList<>();
    }

    public static PrincipalContextBuilder newBuilder() {
        return new PrincipalContextBuilder();
    }

    public PrincipalContextBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public PrincipalContextBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public PrincipalContextBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PrincipalContextBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PrincipalContextBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public PrincipalContextBuilder withNewRole( String role ){
        roles.add(role);
        return this;
    }

    public PrincipalContext build() {
        return new PrincipalContext(username,password,firstName,lastName,email,roles);
    }
}

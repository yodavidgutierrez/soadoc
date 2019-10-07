package co.com.foundation.soaint.security.domain;

public class AuthenticationResponseContext {

    private boolean successful;
    private PrincipalContext principalContext;

    public AuthenticationResponseContext() {
    }

    public AuthenticationResponseContext(boolean successful, PrincipalContext principalContext) {
        this.successful = successful;
        this.principalContext = principalContext;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public PrincipalContext getPrincipalContext() {
        return principalContext;
    }

    public void setPrincipalContext(PrincipalContext principalContext) {
        this.principalContext = principalContext;
    }
}

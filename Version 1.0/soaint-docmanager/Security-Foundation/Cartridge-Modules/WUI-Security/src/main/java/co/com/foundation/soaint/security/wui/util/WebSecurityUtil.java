package co.com.foundation.soaint.security.wui.util;

import co.com.foundation.soaint.security.wui.domain.LoginContext;
import co.com.foundation.soaint.security.wui.mvc.PortalModel;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class WebSecurityUtil {

    // --------------------------

    public static boolean userIsLoggedWithModel(PortalModel model) {

        if( Objects.isNull(model) ){
            return false;
        }

        LoginContext context = model.getContext();
        boolean logged = false;

        if (!Objects.isNull(context)) {
            logged = Optional.ofNullable(context
                    .isLogged())
                    .orElse(false);
        }

        return logged;
    }

    // --------------------------

    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    // --------------------------

    public static boolean userIsLogged() {
        PortalModel model = (PortalModel) getSession().getAttribute("portalModel");
        return userIsLoggedWithModel(model);
    }

    // --------------------------

    public static void redirectToLogin(HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) throws IOException {
        String loginUri = httpRequest.getServletContext().getContextPath().concat("/login");
        httpResponse.sendRedirect(loginUri);
    }


    // --------------------------

    public static boolean skipSecurityForUri(String uri, String urisToSkipSecurity) {

        String[] notSecuredUris = urisToSkipSecurity.split(",");
        boolean skip = false;

        for (String notSecuredUri : notSecuredUris) {
            if (uri.contains(notSecuredUri)) {
                skip = true;
                break;
            }
        }

        return skip;
    }


    // --------------------------

    public static boolean userHasRole(String roles) {
        LoginContext context = getLoginContext(getSession());

        boolean hasRole = false;
        for (String role : roles.split(",")) {
            if (context.getRoles().contains(role) || role.equals("PUBLIC")) {
                hasRole = true;
                break;
            }
        }
        return hasRole;
    }

    // --------------------------

    private static LoginContext getLoginContext(HttpSession session) {
        PortalModel model = (PortalModel) session.getAttribute("portalModel");
        Optional<LoginContext> value = Optional.ofNullable(model.getContext());
        return value.orElse(new LoginContext());
    }

    // --------------------------

    public static void terminateSession() {
        HttpSession session = getSession();
        session.invalidate();
    }

}

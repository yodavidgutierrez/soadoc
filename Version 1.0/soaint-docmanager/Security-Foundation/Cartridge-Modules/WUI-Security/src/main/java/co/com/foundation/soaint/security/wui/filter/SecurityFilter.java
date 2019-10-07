package co.com.foundation.soaint.security.wui.filter;

import co.com.foundation.soaint.security.wui.mvc.PortalModel;
import co.com.foundation.soaint.security.wui.util.WebSecurityUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class SecurityFilter implements Filter {

    // [fields] -----------------------------------
    private static final Logger LOGGER = LogManager.getLogger(SecurityFilter.class.getName());
    private String urisToSkipSecurity;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        urisToSkipSecurity = filterConfig.getInitParameter("URIS_TO_SKIP_SECURITY");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String ipAddress = servletRequest.getRemoteAddr();
        String uri = httpRequest.getRequestURI();

        LOGGER.info("Processing request from IP {}", ipAddress);
        LOGGER.info("Processing request for resource {}", uri);

        HttpSession session = httpRequest.getSession(true);

        if( Objects.isNull(session) ){
            httpRequest.getSession();
            WebSecurityUtil.redirectToLogin(httpRequest,httpResponse);
            return;
        }

        if ( !WebSecurityUtil.skipSecurityForUri(uri,urisToSkipSecurity) ) {

            PortalModel model = (PortalModel) session.getAttribute("portalModel");
            boolean logged = WebSecurityUtil.userIsLoggedWithModel(model);

            if (!logged) {
                WebSecurityUtil.redirectToLogin(httpRequest,httpResponse);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}


package co.com.foundation.sgd.apigateway.config;

import co.com.foundation.sgd.apigateway.security.filters.JWTTokenFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/apis")
public class RestConfig extends ResourceConfig {

	public RestConfig() {
		super();
		register(RolesAllowedDynamicFeature.class);
		register(JWTTokenFilter.class);
		super.packages(true,"co.com.foundation.sgd");
	}

}

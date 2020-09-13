package designer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@SuppressWarnings("unused")
@Component
@Configuration
public class AppConfig {
    /**
     * This is your auth0 domain (tenant you have created when registering with auth0 - account name)
     */
    @Value(value = "${com.auth0.domain}")
    private String domain;

    /**
     * This is the client id of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientId}")
    private String clientId;

    /**
     * This is the client secret of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret;

    //Filter registration for endpoints requiring user authentication
    @Bean
    public FilterRegistrationBean<Auth0Filter> privateFilterRegistration() {
        final FilterRegistrationBean<Auth0Filter> registration = new FilterRegistrationBean<Auth0Filter>();
        registration.setFilter(new Auth0Filter(domain, clientId, clientSecret));
        registration.addUrlPatterns(
                "/public/*",
            "/users/*",
        	"/views/*",
        	"/edit/*",
        	"/CRUD/*"
        );
        registration.setName(Auth0Filter.class.getSimpleName());
        return registration;
    }

    //Filter registration for all endpoints
    //This filter stores authorized database name to sessionUtils
    //in order to allow access to only the right database
    @Bean
    public FilterRegistrationBean<PublicFilter> publicFilterRegistration() {
        final FilterRegistrationBean<PublicFilter> registration = new FilterRegistrationBean<PublicFilter>();
        registration.setFilter(new PublicFilter());
        registration.addUrlPatterns(
//                "/public/*",
                "/users/*",
            	"/views/*",
            	"/edit/*",
            	"/CRUD/*"
        );
        registration.setName(PublicFilter.class.getSimpleName());
        return registration;
    }

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}

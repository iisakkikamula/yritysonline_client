package designer;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

//Implement spring security
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        .requiresChannel()
		        .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
		        .requiresSecure()
		        .and()
		    .csrf().disable()
		    .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ;
    }
}
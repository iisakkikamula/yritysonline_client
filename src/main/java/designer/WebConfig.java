package designer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport{

	//Declare resource handlers for webjars and static resources
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry
        .addResourceHandler("/**","/webjars/**")
		.addResourceLocations("classpath:/static/")
        .addResourceLocations("/webjars/").resourceChain(false)
        ;
	}

	//Declare templateengine
    @Bean
    public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(thymeleafTemplateResolver_string());
        return templateEngine;
    }
 
    //Declare templateresolver
    @Bean
    public StringTemplateResolver thymeleafTemplateResolver_string() {
   StringTemplateResolver  templateResolver = new StringTemplateResolver();
    templateResolver.setOrder(2);
        return templateResolver;
    }

}

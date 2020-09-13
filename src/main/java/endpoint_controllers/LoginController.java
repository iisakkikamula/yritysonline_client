package endpoint_controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import designer.AuthController;

import javax.servlet.http.HttpServletRequest;


//Login controller according to auth0 documentation
@SuppressWarnings("unused")
@Controller
public class LoginController {

    @Autowired
    private AuthController controller;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest request) {
        logger.debug("Performing login");
        String redirectUri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/callback";
        
        //if not on localhost, redirectUri is formed in a different way
        if(!request.getRequestURL().toString().contains("localhost")) {
        	redirectUri = request.getScheme() + "://" + request.getServerName() + "/callback";
        }
        
        String authorizeUrl = controller.buildAuthorizeUrl(request, redirectUri);
        return "redirect:" + authorizeUrl; 
    }

}

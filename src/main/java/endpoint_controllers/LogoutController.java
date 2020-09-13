package endpoint_controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.auth0.SessionUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import designer.AppConfig;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class LogoutController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String domain;
    private final String clientId;
    private final String clientSecret;

    public LogoutController(AppConfig config){
        domain = config.getDomain();
        clientId = config.getClientId();
        clientSecret = config.getClientSecret();
    }

    //Perform logout by invalidating session and revoking refreshtoken
    @RequestMapping(value = "/force_logout", method = RequestMethod.GET)
    protected String logout(final HttpServletRequest request) {
        logger.debug("Performing logout");
        
        //invalidate session

        if (request.getSession() != null) {
            request.getSession().invalidate();
        }
        
        try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				//Go through cookies and revoke refreshtoken
				for (Cookie cookie : cookies) {
					if (cookie.getName().contains("refreshToken")) {
						String refreshToken = cookie.getValue();
						
						HttpResponse<String> response = Unirest.post("https://" + domain + "/oauth/revoke")
								  .header("content-type", "application/json")
								  .body("{ \"client_id\": \"" + clientId + "\", \"client_secret\": \"" + clientSecret + "\", \"token\": \"" + refreshToken + "\" }")
								  .asString();
					}
				}
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Build return url and logout url
        String returnTo = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String logoutUrl = String.format("https://%s/v2/logout?client_id=%s&returnTo=%s", domain, clientId, returnTo);
        return "redirect:" + logoutUrl;
    }

}

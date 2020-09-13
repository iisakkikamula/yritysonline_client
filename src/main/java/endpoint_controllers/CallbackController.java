package endpoint_controllers;

import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.UserInfo;

import designer.AuthController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Controller
public class CallbackController {
	
  	//declare refreshtoken name, refreshtoken is saved to cookies with this key
	public static final String REFRESHTOKEN_DESIGNER = "designer_refreshToken";

    @Autowired
    private AuthController controller;
    private final String redirectOnFail;
    private final String redirectOnNoRights;

    public CallbackController() {
        this.redirectOnFail = "/login";
        this.redirectOnNoRights = "/force_logout";
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected void postCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    private void handle(HttpServletRequest request, HttpServletResponse res) throws IOException {
        String redirect_url = redirectOnNoRights;
        try {
        	//Handle callback according to auth0 documentation until we get userInfo
            Tokens tokens = controller.handle(request);
            SessionUtils.set(request, "accessToken", tokens.getAccessToken());
            SessionUtils.set(request, "idToken", tokens.getIdToken());
            
            log("tokens.getAccessToken(): " + tokens.getAccessToken());
            
            Cookie cookie = new Cookie(REFRESHTOKEN_DESIGNER, tokens.getRefreshToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(2592000); // 30 days
            res.addCookie(cookie);

			AuthAPI authAPI = new AuthAPI(
        			"user-check.eu.auth0.com", 
        			"U5RlbV9Tbtl4phuuN6WyDH-9W79STc7o", 
        			"Rc116MhY133N3YxL8sfXtXpdfDYiFQKxBK72Yhp_GtB8uQEVs0J0yQkyI_KkGIZd"
        	);

        	UserInfo userInfo = authAPI.userInfo((String)SessionUtils.get(request, "accessToken")).execute();

        	//Get email from user info
        	String email = (String) userInfo.getValues().get("email");
        	SessionUtils.set(request, "email", email);
        	//Get redirect_url based on email
        	redirect_url = "/";
        } catch (IdentityVerificationException e) {
            e.printStackTrace();
            redirect_url = "/authentication_failed";
        }
        res.sendRedirect(redirect_url);
    }
	
    private static void log(String message) {
    	Logger logger = Logger.getLogger(CallbackController.class.getName());
    	logger.log(Level.INFO, message);
    }
}

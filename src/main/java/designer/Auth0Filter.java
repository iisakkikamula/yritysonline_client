package designer;

import com.auth0.SessionUtils;
import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.UserInfo;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import endpoint_controllers.CallbackController;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//This class defines a filter which is called in every endpoint where user authentication is needed
public class Auth0Filter implements Filter {

	private String domain;

	private String clientId;

	private String clientSecret;

    public Auth0Filter(String domain, String clientId, String clientSecret) {
		this.domain = domain;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String url = req.getRequestURL().toString();
        
        //Get stored tokens from SessionUtils and if they don't fulfill not null
        //requirement proceed to get new tokens from auth0
        String accessToken = (String) SessionUtils.get(req, "accessToken");
        String idToken = (String) SessionUtils.get(req, "idToken");
        String email = (String) SessionUtils.get(req, "email");

//        log(accessToken + " - " + idToken);
        if (accessToken == null || idToken == null || email == null) {
        	log("tokens or email is null");
            log("accessToken: " + accessToken);
            log("idToken: " + idToken);
            log("email: " + email);
        	
        	LinkedHashMap<String, String> jsonobject = new LinkedHashMap<String, String>();
			try {
				//On localhost we don't use refresh token
				if(!url.contains("localhost")) {
					jsonobject = try_refresh_token(req, res);
				}
	            log("jsonobject: " + jsonobject.toString());
	            log("updated_accessToken: " + jsonobject.get("access_token"));
	            log("updated_idToken: " + jsonobject.get("id_token"));
	            
	            //Check refreshed tokens and proceed to set new values to sessionUtils
	            //if refreshed tokens are not null
	            //This is important because if tokens expire we can renew them 
	            //without a need for a user to log in -> UI experience is better
				if (
						jsonobject.get("access_token") != null
						&& 
						jsonobject.get("id_token") != null
				) {
//					log("updated_access_token: " + jsonobject.get("access_token"));
					SessionUtils.set(req, "accessToken", jsonobject.get("access_token"));
	
//					log("updated_id_token: " + jsonobject.get("id_token"));
					SessionUtils.set(req, "idToken", jsonobject.get("id_token"));

					//We get user email based on access token and set it's value to session utils as well
					AuthAPI authAPI = new AuthAPI(
		        			domain, 
		        			clientId, 
		        			clientSecret
		        	);
		        	UserInfo userInfo = authAPI.userInfo((String) SessionUtils.get(req, "accessToken")).execute();
		        	Object updated_email = userInfo.getValues().get("email");
		        	log("updated_email: " + updated_email);
					SessionUtils.set(req, "email", updated_email);
				}
				else {
		            res.sendRedirect("/login");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        next.doFilter(request, response);
    }
    
    //Returns access token and id-token based on a refresh token
	private LinkedHashMap<String, String> try_refresh_token(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LinkedHashMap<String, String> jsonobject= new LinkedHashMap<String, String>();
		log("prehandle");
        String accessToken = (String) SessionUtils.get(request, "accessToken");
        String idToken = (String) SessionUtils.get(request, "idToken");
	    if (accessToken == null || idToken == null) {
	    	log("accessToken: " + accessToken);
	    	log("idToken: " + idToken);
			Cookie[] cookies = request.getCookies();
			
			if (cookies != null) {
				log("cookies not null");
				//Go through cookies and find refresttoken cookie
				for (Cookie cookie : cookies) {
					log("cookie: " + cookie.getName());
					if (cookie.getName().equals(CallbackController.REFRESHTOKEN_DESIGNER)) {
	
						String refreshToken = cookie.getValue();
						log("cookie.getValue(): " + cookie.getValue());
						
						//Call Auth0 to acquire new tokens
						HttpResponse<String> httpresponse = Unirest.post("https://" + domain + "/oauth/token")
								.header("content-type", "application/x-www-form-urlencoded")
								.body("grant_type=refresh_token&client_id=" + clientId + "&client_secret=" + clientSecret
										+ "&refresh_token=" + refreshToken)
								.asString();
						log("httpresponse: " + httpresponse.toString());
						
						//Parse response into Java map
						JSONParser parser = new JSONParser(httpresponse.getBody());
						try {
							jsonobject = (LinkedHashMap<String, String>) parser.parse();
							return jsonobject;
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	
						break;
					}
				}
			}
	    }

		return jsonobject;
	}

	//Log function logs class name and message
    private static void log(String message) {
    	Logger logger = Logger.getLogger(Auth0Filter.class.getName());
    	logger.log(Level.INFO, message);
    }
}

package designer;

import com.auth0.SessionUtils;
import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.UserInfo;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PublicFilter implements Filter {

	//Filter request, parse uri, find databasename and save it into sessionutils
	//for later use in controllers
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI();
		String[] url_parts = url.split("/");
 //       log("url_parts" + url_parts);
        String authorized_database = url_parts[2];
 //       log("authorized_database: " + authorized_database);
		SessionUtils.set(req, "authorized_database", authorized_database);
        next.doFilter(request, response);
	}

    public PublicFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	//Log function logs class name and message
	private static void log(String message) {
    	Logger logger = Logger.getLogger(PublicFilter.class.getName());
    	logger.log(Level.INFO, message);
    }
}

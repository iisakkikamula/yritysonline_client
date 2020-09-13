package endpoint_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.auth0.SessionUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    //Find default page from model structure based on user's email
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(
    		HttpServletRequest request,
    		HttpServletResponse response
    	) {
    	return "home";
    }

	//Log function logs class name and message
    private static void log(String message) {
    	Logger logger = Logger.getLogger(HomeController.class.getName());
    	logger.log(Level.INFO, message);
    }
}

package endpoint_controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.RequestContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.context.webmvc.SpringWebMvcThymeleafRequestContext;

import com.google.gson.Gson;

@Controller
public class PublicController {

	@Autowired
	private SpringTemplateEngine templateEngine;

	// Serve HTML- page from dropbox
	@GetMapping({ "/public/{database_name}/{html_file_path}" })
	public String serve_public_html_page(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "html_file_path") String html_file_path,
			@PathVariable(value = "database_name") String database_name
	) {
		String backend_url = PublicController.get_backend_domain(request) + "/models/" + database_name + "/" + html_file_path;
		TemplateAsJson templateAsJson = make_rest_call_GET_template(backend_url);

		
	    StringWriter writer = new StringWriter();
	    for(String line: templateAsJson.getRows()) {
	    	writer.write(line);
	    	writer.write("\n");
	    }
	    templateEngine.process("", new Context(), writer);
	    return writer.toString();

	}

	public static String get_backend_domain(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(request.getRequestURI().toLowerCase().contains("localhost")) {
			return "http://localhost:8080";
		}
		return "https:/yritysonline-backend.herokuapp.com/";
	}

	public static String make_rest_call_POST_JSON(String backend_url, String body_json) {
		String output = null;
		try {
			URL url = new URL(backend_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			try {
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.write(body_json.getBytes("UTF8"));
				wr.flush();
				wr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String line = "";
			output = "";
			System.out.println("Output from Server .... \n");
			while ((line = br.readLine()) != null) {
//				System.out.println(output);
				output = output + line;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return output;
	}

	public TemplateAsJson make_rest_call_GET_template(String backend_url) {
		String output = null;
		try {
			URL url = new URL(backend_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String line = "";
			output = "";
			System.out.println("Output from Server .... \n");
			while ((line = br.readLine()) != null) {
//				System.out.println(output);
				output = output + line;
			}

			conn.disconnect();
			Gson gson = new Gson();
			return gson.fromJson(output, TemplateAsJson.class);
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return null;
	}

	public static String make_rest_call_GET(String backend_url) {
		String output = null;
		try {
			URL url = new URL(backend_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String line = "";
			output = "";
			System.out.println("Output from Server .... \n");
			while ((line = br.readLine()) != null) {
//				System.out.println(output);
				output = output + line;
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return output;
	}

	// Log function logs class name and message
	private static void log(String message) {
		Logger logger = Logger.getLogger(PublicController.class.getName());
		logger.log(Level.INFO, message);
	}
}
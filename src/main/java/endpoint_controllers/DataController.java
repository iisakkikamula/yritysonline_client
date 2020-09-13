package endpoint_controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

	//Return view with single record inside based on database name, view name, and record id
	@CrossOrigin(origins = "https://iisakkikamula.github.io")
	@GetMapping("/data/{database_name}/{view_name}/{record_id}")
	public String fetch_one_record(
			HttpServletRequest request,
			@PathVariable("database_name") String database_name,
			@PathVariable("view_name") String view_name,
			@PathVariable("record_id") int record_id
		) {

		String backend_url = PublicController.get_backend_domain(request) + "/data/" + database_name + "/" + view_name + "/" + record_id;
		return PublicController.make_rest_call_GET(backend_url);
	}
	
	@CrossOrigin(origins = "https://iisakkikamula.github.io")
	@GetMapping("/data/{database_name}/{view_name}")
	public String fetch_all_records(
			HttpServletRequest request,
			@PathVariable("database_name") String database_name,
			@PathVariable("view_name") String view_name
		) {
		String backend_url = PublicController.get_backend_domain(request) + "/data/" + database_name + "/" + view_name;
		return PublicController.make_rest_call_GET(backend_url);
	}

	@CrossOrigin(origins = "https://iisakkikamula.github.io")
	@GetMapping("/data/{database_name}/model_structure")
	public String fetch_model_structure(
			HttpServletRequest request,
			@PathVariable("database_name") String database_name
		) {
		String backend_url = PublicController.get_backend_domain(request) + "/data/" + database_name + "/model_structure";
		return PublicController.make_rest_call_GET(backend_url);
	}

	//Log function logs class name and message
	private static void log(String message) {
		Logger logger = Logger.getLogger(DataController.class.getName());
		logger.log(Level.INFO, message);
	}
}

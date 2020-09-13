package endpoint_controllers;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.SessionUtils;
import com.google.gson.Gson;

@RestController
public class CRUD_controller {

	//Save record function
	//Record delivered in request body as JSON
	@RequestMapping(
		    value = "/CRUD_REST/{database_name}/save_record", 
		    method = RequestMethod.POST,
	        consumes = MediaType.APPLICATION_JSON_VALUE)
	public String save_records(
			HttpServletRequest request,
			@PathVariable(value="database_name") String database_name,
			@RequestBody String json
		) {
		String backend_url = "http://localhost:8080" + "/CRUD_REST/" + database_name + "/save_record";
		return PublicController.make_rest_call_POST_JSON(backend_url, json);
	}

	//Create new record based on database name and tablename
	@RequestMapping(
		    value = "/CRUD_REST/{database_name}/{edit_table}/create_record_REST", 
		    method = RequestMethod.POST,
	        consumes = MediaType.APPLICATION_JSON_VALUE)
	public String create_record_REST(
			HttpServletRequest request,
			@PathVariable(value="database_name") String database_name,
			@PathVariable(value="edit_table") String edit_table,
			@RequestBody String json
	) {
		String backend_url = "http://localhost:8080" + "/CRUD_REST/" + database_name + "/" + edit_table + "/create_record_REST";
		return PublicController.make_rest_call_POST_JSON(backend_url, json);
	}

	//Delete record from database based on database name, table name and record id
	@RequestMapping(
		    value = "/CRUD_REST/{database_name}/{edit_table}/{record_id}/delete_record_REST", 
		    method = RequestMethod.POST,
	        consumes = MediaType.APPLICATION_JSON_VALUE)
	public String delete_record_REST(
			HttpServletRequest request,
			@PathVariable(value="database_name") String database_name,
			@PathVariable(value="edit_table") String edit_table,
			@PathVariable(value="record_id") int record_id,
			@RequestBody String json
	) {
		String backend_url = "http://localhost:8080" + "/CRUD_REST/" + database_name + "/" + edit_table + "/" + record_id + "/delete_record_REST";
		return PublicController.make_rest_call_POST_JSON(backend_url, json);
	}

	//Log function logs class name and message
	private static void log(String message) {
		Logger logger = Logger.getLogger(CRUD_controller.class.getName());
		logger.log(Level.INFO, message);
	}
}

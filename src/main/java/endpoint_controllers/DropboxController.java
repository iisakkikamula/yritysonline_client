package endpoint_controllers;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class DropboxController {

	@Autowired
	ServletContext context;

	// Upload file to dropbox based on database name, table name, and row id
	@PostMapping("/dropbox/uploadREST/{database_name}/{table_name}/{row_id}")
	@ResponseBody
	public String upload_file(@PathVariable("database_name") String database_name,
			@PathVariable("table_name") String table_name, @PathVariable("row_id") int row_id,
			HttpServletRequest request, HttpServletResponse response) {
		String backend_url = PublicController.get_backend_domain(request) + "/dropbox/uploadREST/" + database_name + "/" + table_name + "/" + row_id;
		try {
			List<Part> parts = request.getParts().stream().collect(Collectors.toList());
			for (Part file : parts) {
				
				HttpEntity responseEntity = PublicController.make_rest_call_POST_UPLOAD_DROPBOX_FILE(file, backend_url, file.getSubmittedFileName());
				log("responseEntity: " + responseEntity);
			}
			return "{}";
//			response.sendRedirect(backend_url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	// Download file based on fileid
	@GetMapping("/dropbox/{database_name}/download/{file_id:.+}")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("database_name") String database_name, @PathVariable("file_id") String file_id)
			throws Exception {

		String backend_url = PublicController.get_backend_domain(request) + "/dropbox/" + database_name + "/download";
		PublicController.make_rest_call_POST_DOWNLOAD_DROPBOX_FILE(response, backend_url,file_id);
	}

	// Delete file based on file id
	@PostMapping("/dropbox/deleteFileREST/{database_name}/{id}")
	public String deleteFileREST(HttpServletRequest request, @PathVariable("database_name") String database_name,
			@PathVariable("id") String id) {
		String backend_url = PublicController.get_backend_domain(request) + "/dropbox/deleteFileREST/" + database_name + "/" + id;
		PublicController.make_rest_call_POST_JSON(backend_url, "{}");
		return "{}";
	}

	// Log function logs class name and message
	private static void log(String message) {
		Logger logger = Logger.getLogger(DropboxController.class.getName());
		logger.log(Level.INFO, message);
	}
}

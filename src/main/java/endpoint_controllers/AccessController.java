package endpoint_controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {

	//If authentication fails, this endpoint is called and error text displayed to user
	@GetMapping({
		"/authentication_failed"
	})
	public String authentication_failed(){
		return "authentication_failed";
	}

}

package vn.edu.fpt.peery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class HomeController {
	@GetMapping(path = "peery/index")
	public String home(HttpSession session) {
		if (session.getAttribute("user") != null) {
			return "index";
		} else {
			return "index";
		}

	}
}

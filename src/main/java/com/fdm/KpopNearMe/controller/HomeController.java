package com.fdm.KpopNearMe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * Home controller is responsible for getting initial home page
 * @author Hailie Long
 *
 */
@Controller
public class HomeController {
	
	final String INDEX_JSP = "index";


	/**
	 * home controller returns index page when getting homepage request
	 * @return index page
	 */
	@GetMapping("/")
	public String getHomePage() {
		return INDEX_JSP;
	}
	
	
}

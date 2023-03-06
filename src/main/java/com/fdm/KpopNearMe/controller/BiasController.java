package com.fdm.KpopNearMe.controller;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;
import com.fdm.KpopNearMe.model.Bias;
import com.fdm.KpopNearMe.service.PlaceService;
import com.fdm.KpopNearMe.service.ReviewService;
import com.fdm.KpopNearMe.service.UserService;
import com.fdm.KpopNearMe.service.BiasService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * BiasController that is responsible for handling user requests regarding biases object
 * This involves: create a bias
 * 				  delete a bias
 * 				  search for places based on bias name
 * @author Hailie Long
 *
 */
@Controller
public class BiasController {
	
	
	final String ERROR_EMPTY_SEARCH_INPUT_MSG = "please enter your bias name for search";
	final String FOUND_SEARCH_PLACES_ATTR = "foundSearchPlaces";
	final String REDIRECT_ERROR_EMPTY_SEARCH_INPUT = "redirect:/errorEmptySearchInput";
	final String SEARCH_PLACE_JSP = "searchPlace";
	final String ERROR_PLACE_HAS_BIAS_MSG = "the place already has the bias, re-enter another one";
	final String ERROR_EMPTY_BIAS_NAME_MSG = "please input a bias name";
	final String ERROR_MESSAGE_ATTR = "errorMessage";
	final String REDIRECT_ERROR_PLACE_EXISTS_BIAS = "redirect:/errorPlaceExistsBias";
	final String REDIRECT_ERROR_EMPTY_BIAS_NAME_JSP = "redirect:/errorEmptyBiasName";
	final String CREATE_BIAS_JSP = "createBias";
	final String CURRENT_PLACE_ATTR = "currentPlace";
	final String BIND_BIAS_ATTR = "bindBias";
    final String REDIRECT_ERROR_NOT_LOGGED_IN_YET = "redirect:/errorNotLoggedInYet";
	final String CURRENT_USER_ATTR = "currentUser";
	private PlaceService placeService;
	private BiasService biasService;
	private Log log = LogFactory.getLog(BiasController.class);

	/**
	 * field constructor
	 * @param placeService PlaceService handling read/write/validate place objects from database
	 * @param biasService  BiasService handling read/write/validate bias objects from database
	 */
	@Autowired
	public BiasController(PlaceService placeService, BiasService biasService) {
		super();
		this.placeService = placeService;
		this.biasService = biasService;
	}
	
	
	/**
	 * create a bias and add it to a place
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param placeId id of the place to add a bias
	 * @return createBias jsp file
	 */
	@GetMapping("/createBias")
	public String getCreateBiasPage(Model model, HttpSession session, @RequestParam int placeId) {
		// a user cannot access the createbias page if not logged in yet
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		
		Place foundPlace = placeService.findPlaceById(placeId);
		Bias biasObject = new Bias();
		
		model.addAttribute(BIND_BIAS_ATTR,biasObject);
		session.setAttribute(CURRENT_PLACE_ATTR, foundPlace);
		return CREATE_BIAS_JSP;
	}
	
	
	/**
	 * process the bias created via the form on createBias jsp, validates it and add it to database if valid
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param returnedSpringBias bias created by user via spring form from createbias page
	 * @return error if fails, createBiasConfirm page if creation is successful
	 */
	@PostMapping("/processCreateBias")
	public String processCreateBias(Model model, HttpSession session, Bias returnedSpringBias) {
		Place currentPlace = (Place) session.getAttribute(CURRENT_PLACE_ATTR);
		
		// store all bias name in uppercase
		String inputBiasName = returnedSpringBias.getName().toUpperCase();
		returnedSpringBias.setName(inputBiasName);
		log.debug(returnedSpringBias.getName());
		
		// check validity of created bias via biasService
		int ifBiasValid = biasService.ifBiasValid(returnedSpringBias, currentPlace);
		
		// if no bias name fulfilled
		if(ifBiasValid == -1) {
			return REDIRECT_ERROR_EMPTY_BIAS_NAME_JSP;
			
		}
		// if bias already stored in the place
		else if (ifBiasValid == -2) {
			return REDIRECT_ERROR_PLACE_EXISTS_BIAS;
			
		}
		// if place does not have the bias, but there is already one bias with same name
		// add the existing bias to the place
		else if(ifBiasValid == 0) {
			Bias foundBias = biasService.findbyName(inputBiasName);
			foundBias.addAPlace(currentPlace);
			currentPlace.addABias(foundBias);
			
			placeService.save(currentPlace);
			biasService.save(foundBias);
			log.info("found existing bias in storage, added it to the place");
			
		}else {
			// if place does not have bias, and bias is new to database
			// add the bias to database, and attach it to place
			// update the database
			returnedSpringBias.addAPlace(currentPlace);
			currentPlace.addABias(returnedSpringBias);
			
			placeService.save(currentPlace);
			biasService.save(returnedSpringBias);
			log.info("bias created and stored"+returnedSpringBias.toString());
		}
		
		//update session and model attributes
		session.removeAttribute(CURRENT_PLACE_ATTR);
		session.setAttribute(CURRENT_PLACE_ATTR, currentPlace);
		
		Bias biasObject = new Bias();
		model.addAttribute(BIND_BIAS_ATTR,biasObject);
		return CREATE_BIAS_JSP;
		
		
	}
	
	/**
	 * if getting error saying empty bias name, show error and redirect to create bias page
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createBias page
	 */
	@GetMapping("/errorEmptyBiasName")
	public String getEmptyBiasName(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, ERROR_EMPTY_BIAS_NAME_MSG);
		Bias biasObject = new Bias();
		model.addAttribute(BIND_BIAS_ATTR,biasObject);
		return CREATE_BIAS_JSP;
	}
	
	/**
	 * if getting place already contains the bias as error, show error msg as attribute in model
	 * and redirect to createBias jsp
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createBias jsp
	 */
	@GetMapping("/errorPlaceExistsBias")
	public String getPlaceExistsBias(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, ERROR_PLACE_HAS_BIAS_MSG);
		Bias biasObject = new Bias();
		model.addAttribute(BIND_BIAS_ATTR,biasObject);
		return CREATE_BIAS_JSP;
	}
	
	
	/**
	 * when trying to delete a bias, update corresponding place object, and update entities in database,
	 * and redirect to createbias page if successful
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param biasId id of bias to be deleted
	 * @return createBias page
	 */
	@GetMapping("/deleteBias")
	public String deleteABias(Model model, HttpSession session, @RequestParam int biasId) {
		// guest do not have access to the page/request
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		// find corresponding bias and place
		Bias currBias = biasService.findById(biasId);
		Place currentPlace = (Place) session.getAttribute(CURRENT_PLACE_ATTR);
		
		// update entities in java model and database
		currBias.removeAPlace(currentPlace);
		currentPlace.removeABias(currBias);
		
		biasService.save(currBias);
		placeService.save(currentPlace);
		
		//update session and model
		session.removeAttribute(CURRENT_PLACE_ATTR);
		session.setAttribute(CURRENT_PLACE_ATTR, currentPlace);
		Bias biasObject = new Bias();
		model.addAttribute(BIND_BIAS_ATTR,biasObject);
		return CREATE_BIAS_JSP;
		
	}
	
	/**
	 * when user trying to reach search page to search for places based on bias name
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return search place jsp
	 */
	@GetMapping("/searchPlace")
	public String getSearchPlaceBasedOnBias(Model model, HttpSession session) {
		// guest have no access to page
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		return SEARCH_PLACE_JSP;
	}

	/**
	 * after receiving searchinput, controller treats the input as biasname,
	 * find bias based on the biasname, and find its stored places if there is any
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param biasName search input being treated as biasname
	 * @return searchPlace jsp with found places show on the page as search result
	 */
	@GetMapping("/processSearchPlace")
	public String processSearchPlace(Model model, HttpSession session, @RequestParam String biasName) {
		// guest have no access to search page
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		// search input cannot be empty
		if(biasName.isEmpty()) {
			return REDIRECT_ERROR_EMPTY_SEARCH_INPUT;
		}
		List<Place> foundPlaces = biasService.findPlacesByBias(biasName);
		
		session.setAttribute(FOUND_SEARCH_PLACES_ATTR, foundPlaces);
		return SEARCH_PLACE_JSP;
		
	}
	
	/**
	 * when getting error due to empty search input
	 * show error msg as model attribute, and redirect to seach place jsp
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return searchPlace jsp
	 */
	@GetMapping("/errorEmptySearchInput")
	public String getEmptySearchInput(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, ERROR_EMPTY_SEARCH_INPUT_MSG);
		return SEARCH_PLACE_JSP;
	}
	
	
	
	

}

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
import com.fdm.KpopNearMe.model.User;
import com.fdm.KpopNearMe.service.PlaceService;
import com.fdm.KpopNearMe.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 *  PlaceController is responsible for handling requests regarding place objects, and return corresponding pages to user
 * @author Hailie Long
 *
 */
@Controller
public class PlaceController {
	
    final String SEARCH_PLACE_JSP = "searchPlace";
	final String SHOW_FAVORITE_PLACES_JSP = "showFavoritePlaces";
	final String FAVORITE_PLACES_ATTR = "favoritePlaces";
	final String ERROR_PLACE_NAME_EXISTS_MSG = "place name already exists, please re-enter one";
	final String REDIRECT_ERROR_PLACE_NAME_EXIXTS_JSP = "redirect:/errorPlaceNameExixts";
	final String UPDATE_PLACE_JSP = "updatePlace";
	final String CURRENT_PLACE_ATTR = "currentPlace";
	final String SHOW_ALL_PLACES_JSP = "showAllPlaces";
	final String ALL_PLACES_ATTR = "allPlaces";
	final String SHOW_PLACES_BY_CREATOR_JSP = "showPlacesByCreator";
	final String CREATED_PLACES_ATTR = "createdPlaces";
	final String ERROR_EMPTY_FIELD_MSG = "please fill every field, thanks";
	final String ERROR_PLACE_EXISTS_MSG = "the place already exists in database, please re-enter one";
	final String ERROR_MESSAGE_ATTR = "errorMessage";
	final String CREATE_PLACE_CONFIRM_JSP = "createPlaceConfirm";
	final String REDIRECT_ERROR_PLACE_EMPTY_FIELD_JSP = "redirect:/errorPlaceEmptyField";
	final String REDIRECT_ERROR_PLACE_ALREADY_EXISTS_JSP = "redirect:/errorPlaceAlreadyExists";
	final String CREATE_PLACE_JSP = "createPlace";
	final String BIND_PLACE_ATTR = "bindPlace";
	final String REDIRECT_ERROR_NOT_LOGGED_IN_YET = "redirect:/errorNotLoggedInYet";
	final String CURRENT_USER_ATTR = "currentUser";
	private PlaceService placeService;
	private UserService userService;
	private Log log = LogFactory.getLog(PlaceController.class);
	
	/**
	 * constructor of controller with fields
	 * @param placeService handling place read/write/validation from/to database
	 * @param userService handling user read/write/validation from/to database
	 */
	@Autowired
	public PlaceController(PlaceService placeService, UserService userService) {
		super();
		this.placeService = placeService;
		this.userService = userService;
	}
	

	/**
	 * a logged in user can access createPlace page to create a new place
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createPlace jsp
	 */
	@GetMapping("/createPlace")
	public String getCreatePlacePage(Model model, HttpSession session) {
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		
		Place placeObject = new Place();
		model.addAttribute(BIND_PLACE_ATTR, placeObject);
		return CREATE_PLACE_JSP;
		
	}
	
	/**
	 * process created place object via springform from createPlace jsp, and store it into database if valid
	 * @param returnedSpringPlace
 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createPlaceConfirm jsp
	 */
	@PostMapping("/processCreatePlace")
	public String processPostCreatePlace(Place returnedSpringPlace, Model model, HttpSession session) {
		log.info("created place: "+returnedSpringPlace.toString());
		// validate place created via springform
		int ifPlaceValid = placeService.ifPlaceValid(returnedSpringPlace);
		// if place already exixts in database, show error
		if(ifPlaceValid == -1) {
			return REDIRECT_ERROR_PLACE_ALREADY_EXISTS_JSP;
		}
		// if fields of place is empty, show error
		else if(ifPlaceValid == -2) {
			return REDIRECT_ERROR_PLACE_EMPTY_FIELD_JSP;
		}
		// update database and session attributes
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		returnedSpringPlace.setCreator(currentuser);
		returnedSpringPlace.setSavedUsers(new ArrayList<User>());
		this.placeService.save(returnedSpringPlace);
		return CREATE_PLACE_CONFIRM_JSP;
	}
	
	/**
	 * when getting error place already exists in database, show error msg via model attribute
	 * and redirect to createplace jsp
     * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createPlace page
	 */
	@GetMapping("/errorPlaceAlreadyExists")
	public String getPlaceAlreadyExists(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, ERROR_PLACE_EXISTS_MSG);
		Place placeObject = new Place();
		model.addAttribute(BIND_PLACE_ATTR, placeObject);
		return CREATE_PLACE_JSP;
	}
	
	/**
	 * when getting error saying empty fields found in place, show error msg via model attribute
	 * and redirect to createplace jsp page
     * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createPlace jsp page
	 */
	@GetMapping("/errorPlaceEmptyField")
	public String getPlaceEmptyFields(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, ERROR_EMPTY_FIELD_MSG);
		Place placeObject = new Place();
		model.addAttribute(BIND_PLACE_ATTR, placeObject);
		return CREATE_PLACE_JSP;
	}
	
	/**
	 * show places created by current user in the page
	 * a guest cannot access the page
     * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return showPlacesByCreator jsp page
	 */
	@GetMapping("/showPlacesByCurrentUser")
	public String getPlacesByCurrentUser(Model model, HttpSession session) {
		
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		
		// get places created by user via placeService
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		List<Place> createdPlaces = placeService.findPlacesByCreator(currentuser);
		log.debug("createdPlaces size is" + createdPlaces.size());

		session.setAttribute(CREATED_PLACES_ATTR, createdPlaces);
		return SHOW_PLACES_BY_CREATOR_JSP;
	}
	
	/**
	 * show all places stored in database in a page
	 * a guest cannot access the page without login
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return showAllPlaces jsp page
	 */
	@GetMapping("/showAllPlaces")
	public String getAllPlaces(Model model, HttpSession session) {
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		// get all places via placeService
		List<Place> allPlaces = placeService.findAll();

		session.setAttribute(ALL_PLACES_ATTR, allPlaces);
		return SHOW_ALL_PLACES_JSP;
	}
	
	/**
	 * get updatePlace page for a particular place
	 * a guest cannot access the page without login
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param placeId id for the place to be updated
	 * @return updatePlace jsp showing updated information
	 */
	@GetMapping("/updatePlace")
	public String showUpdatePlacePage(Model model, HttpSession session, @RequestParam int placeId ) {
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		// find the place via placeId from placeService, 
		// and store it as session attribute to show on jsp page
		Place foundPlace = placeService.findPlaceById(placeId);
		session.setAttribute(CURRENT_PLACE_ATTR, foundPlace);
		model.addAttribute(BIND_PLACE_ATTR, foundPlace);
		return UPDATE_PLACE_JSP;
	}
	
	/**
	 * when receiving updated place via springform from updateplace jsp page
	 * validate the update, and udpate the database if valid
	 * @param springReturnedPlace updated place submitted by user via springform
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return showPlacesByCreator jsp Page if succeed, show error msg and stay in updatePlace if fails
	 */
	@PostMapping("/processUpdatePlace")
	public String postProcessUpdatePlace(Place springReturnedPlace, Model model, HttpSession session) {
		boolean ifPlaceExists = false;
		// check if place name get changed, if changed, check if there is place with same name
		String oldPlaceName = ((Place) session.getAttribute(CURRENT_PLACE_ATTR)).getName();
		if(springReturnedPlace.getName().equals(oldPlaceName)== false) {
			ifPlaceExists = placeService.ifPlaceExists(springReturnedPlace.getName());
		}
		if(ifPlaceExists) {
			return REDIRECT_ERROR_PLACE_NAME_EXIXTS_JSP;
		}
		
		// if no place with same name exists,
		// copy biases, reviews, savedUsers to springReturnedPlace
		Place currentPlace = (Place) session.getAttribute(CURRENT_PLACE_ATTR);
		springReturnedPlace.setBiases(currentPlace.getBiases());
		springReturnedPlace.setCreator(currentPlace.getCreator());
		springReturnedPlace.setReviews(currentPlace.getReviews());
		springReturnedPlace.setSavedUsers(currentPlace.getSavedUsers());
		// update database
		placeService.save(springReturnedPlace);
		log.info("place has been updated successfully: "+springReturnedPlace.toString());
		
		// update session and model attributes
		int currentuserId = ((User) session.getAttribute(CURRENT_USER_ATTR)).getId();
		User currentuser = userService.find(currentuserId);
		session.removeAttribute(CURRENT_USER_ATTR);
		session.setAttribute(CURRENT_USER_ATTR, currentuser);
		
		List<Place> createdPlaces = placeService.findPlacesByCreator(currentuser);
		session.setAttribute(CREATED_PLACES_ATTR, createdPlaces);
		
		return SHOW_PLACES_BY_CREATOR_JSP;
		
	}
	
	/**
	 * when getting error saying place name exists, show error via model and redirect to update place page
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return updatePlace jsp page
	 */
	@GetMapping("/errorPlaceNameExixts")
	public String getErrorPlaceNameExists(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR,ERROR_PLACE_NAME_EXISTS_MSG);
		Place foundPlace = (Place) session.getAttribute(CURRENT_PLACE_ATTR);
		model.addAttribute(BIND_PLACE_ATTR, foundPlace);
		return UPDATE_PLACE_JSP;
	}
	
	/**
	 * save a place into favoritePlces list if place is not in the fav places list yet
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param placeId place id to be saved
	 * @return show all places jsp
	 */
	@GetMapping("/savePlace")
	public String getSaveAPlaceToUserFavoriteList(Model model, HttpSession session, @RequestParam int placeId) {
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		
		Place foundPlace = placeService.findPlaceById(placeId);
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		int currUserId = currentuser.getId();
		
		log.debug(currentuser.toString());
		log.debug("before saving current user fav size is "+currentuser.getFavoritePlaces().size());
		// update java object model first, 
		// then update the user/place table in database
		currentuser.favoriteAPlace(foundPlace);
		userService.save(currentuser);
		
		log.debug("current user fav place size is "+currentuser.getFavoritePlaces().size());
		
		foundPlace.addUser(currentuser);
		log.debug("current place saveduser size is"+foundPlace.getSavedUsers().size());
		placeService.save(foundPlace);
		// updqte session attributes
		session.removeAttribute(CURRENT_USER_ATTR);
		currentuser = userService.find(currUserId);
		session.setAttribute(CURRENT_USER_ATTR, currentuser);
		log.info("save done");
		log.debug(currentuser.getFavoritePlaces().size());
		return SHOW_ALL_PLACES_JSP;
	}
	
	/**
	 * show favorite places by the user in a page
	 * a guest cannot access without login
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return showFavPlaces jsp page
	 */
	@GetMapping("/showFavoritePlaces")
	public String getFavoritePlaces(Model model, HttpSession session) {
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		List<Place> foundPlaces = currentuser.getFavoritePlaces();
		session.setAttribute(FAVORITE_PLACES_ATTR, foundPlaces);

		return SHOW_FAVORITE_PLACES_JSP;
		
	}
	
	/**
	 * delete a place from favoritePlaces list from the user
	 * update java object, and update the database
	 * finally, update the session attributes
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param placeId id of the place to be removed
	 * @return show fav_places jsp page
	 */
	@GetMapping("/deleteFavoritePlace")
	public String deleteFavoritePlace(Model model, HttpSession session, @RequestParam int placeId) {
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		// find current user, place via placeService or as current session attribute
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		Place currentPlace = placeService.findPlaceById(placeId);
		int currUserId = currentuser.getId();
		log.debug("enter deletion");
		// update java object and update database entries
		currentuser.removeAfavoritePlace(currentPlace);
		currentPlace.removeSaveUser(currentuser);
	
		placeService.save(currentPlace);
		userService.save(currentuser);
		
		// update session attributes
		session.removeAttribute(CURRENT_USER_ATTR);
		currentuser = userService.find(currUserId);
		session.setAttribute(CURRENT_USER_ATTR, currentuser);
		
		log.debug("after deletion");
		List<Place> foundPlaces = currentuser.getFavoritePlaces();
	
		session.removeAttribute(FAVORITE_PLACES_ATTR);
		session.setAttribute(FAVORITE_PLACES_ATTR, foundPlaces);
		
		log.info("deletion complete");
		return SHOW_FAVORITE_PLACES_JSP;
		
	}
	
	/**
	 * save a place to favoritePlaces list of a user from search result page
	 * the method is similar to savePlace method previously, 
	 * the difference is to redirect user to searchPlace jsp at last instead of showallplaces jsp page
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param placeId id of the place to be saved
	 * @return searchPlace jsp page
	 */
	@GetMapping("/saveSearchPlace")
	public String getSaveAPlaceToUserFavoriteListFromSearch(Model model, HttpSession session, @RequestParam int placeId) {
		if(session.getAttribute(CURRENT_USER_ATTR) == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		// update java models and database entries 
		Place foundPlace = placeService.findPlaceById(placeId);
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		int currUserId = currentuser.getId();
		log.debug(currentuser.toString());
		log.debug("before saving current user fav size is "+currentuser.getFavoritePlaces().size());
		
		currentuser.favoriteAPlace(foundPlace);
		userService.save(currentuser);
		
		foundPlace.addUser(currentuser);
		log.debug("current place saveduser size is"+foundPlace.getSavedUsers().size());
		placeService.save(foundPlace);
		
		// update session attributes
		session.removeAttribute(CURRENT_USER_ATTR);
		currentuser = userService.find(currUserId);
		session.setAttribute(CURRENT_USER_ATTR, currentuser);
		log.info("save done");
		log.debug(currentuser.getFavoritePlaces().size());
		return SEARCH_PLACE_JSP;
	}
	
	
	
	

}

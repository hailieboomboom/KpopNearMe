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
import com.fdm.KpopNearMe.service.PlaceService;
import com.fdm.KpopNearMe.service.ReviewService;
import com.fdm.KpopNearMe.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * reviewController is responsible for handling requests regarding Reviews, and redirect user to corresponding pages
 * a user can: create a review
 * 			   delete a review
 * @author Hailie Long
 *
 */
@Controller
public class ReviewController {
	 
	final String ERROR_REVIEW_RATING_OUTOFRANGE_MSG = "Please input review rating as a number between 0 and 5";
	final String ERROR_EMPTY_REVIEW_CONTENT_MSG = "Review content cannot be empty";
	final String ERROR_MESSAGE_ATTR = "errorMessage";
	final String ALL_PLACES_ATTR = "allPlaces";
	final String REDIRECT_ERROR_REVIEW_RATING_OUT_OF_RANGE_JSP = "redirect:/errorReviewRatingOutOfRange";
	final String REDIRECT_ERROR_NO_REVIEW_CONTENT_JSP = "redirect:/errorNoReviewContent";
	final String CREATE_REVIEW_JSP = "createReview";
	final String CURRENT_PLACE_ATTR = "currentPlace";
	final String BIND_REVIEW_ATTR = "bindReview";
	final String REDIRECT_ERROR_NOT_LOGGED_IN_YET = "redirect:/errorNotLoggedInYet";
	final String CURRENT_USER_ATTR = "currentUser";
	private PlaceService placeService;
	private UserService userService;
	private ReviewService reviewService;
	private Log log = LogFactory.getLog(ReviewController.class);

	

	@Autowired
	public ReviewController(PlaceService placeService, UserService userService, ReviewService reviewService) {
		super();
		this.placeService = placeService;
		this.userService = userService;
		this.reviewService = reviewService;
	}
	
	/**
	 * show createReview page when user is logged in.
	 * the page includes shows all current reviews for a place, 
	 * and a spring form for a user to add a new review to the place
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param placeId id of the place to add a review
	 * @return createReview jsp page 
	 */
	@GetMapping("/createReview")
	public String getCreateReviewPage(Model model, HttpSession session, @RequestParam int placeId) {
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		if(currentuser == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
		Place foundPlace = placeService.findPlaceById(placeId);
		Review reviewObject = new Review();
		
		model.addAttribute(BIND_REVIEW_ATTR,reviewObject);
		session.setAttribute(CURRENT_PLACE_ATTR, foundPlace);
		return CREATE_REVIEW_JSP;
	}
	
	/**
	 * when user submits a review object via spring form, if it is valid,
	 * it is added into database and the page will show the created review as a result
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param returnedSpringReview review submitted by user via springform on the page
	 * @return createReview jsp page with created review added if succeed
	 */
	@PostMapping("/processCreateReview")
	public String processCreateReview(Model model, HttpSession session, Review returnedSpringReview) {
		log.info("created review: "+returnedSpringReview.toString());
		
		// check if created review is valid via reviewService
		int ifReviewValid = reviewService.ifReviewValid(returnedSpringReview);
		// lead the user to different error msgs based on validation result
		if(ifReviewValid == -1) {
			return REDIRECT_ERROR_NO_REVIEW_CONTENT_JSP;
		}
		else if(ifReviewValid == -2) {
			return REDIRECT_ERROR_REVIEW_RATING_OUT_OF_RANGE_JSP;
		}
		// if created review is valid, update the java objects, entries in database and session attributes
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		Place currentPlace = (Place) session.getAttribute(CURRENT_PLACE_ATTR);
	
		returnedSpringReview.setUser(currentuser);
		returnedSpringReview.setPlace(currentPlace);
		currentuser.addAReview(returnedSpringReview);
		currentPlace.addAReview(returnedSpringReview);
		//update place overall rating
	    double rating = placeService.updateOverallRating(currentPlace);
		currentPlace.setOverallRating(rating);
		log.debug("after review create " + currentPlace.toString());
		placeService.save(currentPlace);
		userService.save(currentuser);
		reviewService.save(returnedSpringReview);
		
		
		// update session and model attributes
		List<Place> allPlaces = placeService.findAll();
		session.removeAttribute(ALL_PLACES_ATTR);
		session.setAttribute(ALL_PLACES_ATTR, allPlaces);
		session.removeAttribute(CURRENT_PLACE_ATTR);
		session.setAttribute(CURRENT_PLACE_ATTR, currentPlace);
		Review reviewObject = new Review();
		reviewObject.setPlace(currentPlace);
		reviewObject.setUser(currentuser);
		model.addAttribute(BIND_REVIEW_ATTR,reviewObject);
		
		log.info("review created successfully");
		return CREATE_REVIEW_JSP;
	}
	
	/**
	 * when getting error showing no review content submitted, show error msg via model attribute and redirect 
	 * user to createReview jsp page
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createReview jsp page
	 */
	@GetMapping("/errorNoReviewContent")
	public String getNoReviewContent(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, ERROR_EMPTY_REVIEW_CONTENT_MSG);
		Review reviewObject = new Review();
		
		model.addAttribute(BIND_REVIEW_ATTR,reviewObject);
		return CREATE_REVIEW_JSP;
	}
	
	/**
	 * when getting error showing rating is out of range,
	 * show error msg via model attributes and redirect to createReview jsp page
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @return createReview jsp page
	 */
	@GetMapping("/errorReviewRatingOutOfRange")
	public String getReviewRatingOutofRange(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, ERROR_REVIEW_RATING_OUTOFRANGE_MSG);
		Review reviewObject = new Review();
		
		model.addAttribute(BIND_REVIEW_ATTR,reviewObject);
		return CREATE_REVIEW_JSP;
	}
	 
	/**
	 * when deleting a review from place and database, 
	 * find review via review id from ReviewService
	 * remove review from lists stored in current user and place
	 * update database entries, 
	 * and update session attributes,
	 * and redirect user to createReview jsp with the review removed
	 * @param model store request scope attributes
	 * @param session store session scope attributes
	 * @param reviewId id of the review to be removed
	 * @return createReview jsp with review removed if successful
	 */
	@GetMapping("/deleteReview")
	public String deleteAReview(Model model, HttpSession session, @RequestParam int reviewId) {
		
		// find review, associated user and place to be updated
		Review currReview = reviewService.findById(reviewId);
		User currentuser = (User) session.getAttribute(CURRENT_USER_ATTR);
		Place currentPlace = (Place) session.getAttribute(CURRENT_PLACE_ATTR);
		log.debug(currReview.toString());
		// guest cannot access the page
		if(currentuser == null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET;
		}
	
		// update database entries
		currentuser.removeAReview(currReview);
		currentPlace.removeAReview(currReview);
	
		double rating = placeService.updateOverallRating(currentPlace);
		currentPlace.setOverallRating(rating);
		userService.save(currentuser);
		placeService.save(currentPlace);
		reviewService.removeReview(currReview);
		
		//update session and model attributes
		session.removeAttribute(CURRENT_PLACE_ATTR);
		session.setAttribute(CURRENT_PLACE_ATTR, currentPlace);
		session.removeAttribute(CURRENT_USER_ATTR);
		session.setAttribute(CURRENT_USER_ATTR, currentuser);
		Review reviewObject = new Review();
		reviewObject.setPlace(currentPlace);
		reviewObject.setUser(currentuser);
		model.addAttribute(BIND_REVIEW_ATTR,reviewObject);
		
		log.info("deletion review complete");
		return CREATE_REVIEW_JSP;
		
	}
	
	
	
	

}

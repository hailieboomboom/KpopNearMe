package com.fdm.KpopNearMe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.KpopNearMe.controller.UserController;
import com.fdm.KpopNearMe.dal.UserRepository;
import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;

/**
 * User service class is responsible for dealing with actions requested from user controller
 * related to communicating with user entities stored in database.
 * 
 * User service completes communication with user table in database via user repository.
 * The service is responsible for:
 * 		find a user by id
 * 		find a user by username
 * 		save a user to database
 * 		validate user credentials when logging in
 * 		check if a username already exists in database
 * 		remove a user from database
 * 
 * @author hailieboomboom
 *
 */
@Service
public class UserService {

    final int WRONG_PASSWORD = 0;
	final int VALID_USER = 1;
	final int NO_USER_FOUND = -1;
	// user service has user repository that is responsible for connecting with database directly
	private UserRepository userRepo;
	private ReviewService reviewService;
	private PlaceService placeService;

	private Log log = LogFactory.getLog(UserService.class);
	
	@Autowired
	public UserService(UserRepository userRepo, ReviewService reviewService, PlaceService placeService) {
		super();
		this.userRepo = userRepo;
		this.reviewService = reviewService;
		this.placeService = placeService;
	}
	 
	/**
	 * find a user by id
	 * @param id
	 * @return a user object
	 */
	public User find(int id) {
		Optional<User> foundUser = userRepo.findById(id);
		// if no user is found, return null
		if(foundUser.isEmpty()) {
			return null;
		}
		return foundUser.get();
	}
	
	/**
	 * save a user back to database
	 * @param user
	 * @return saved user object
	 */
	public User save(User user) {
		return userRepo.save(user);
	}
	
	/**
	 * find a user by username
	 * @param username
	 * @return found user object
	 */
	public User findByName(String username) {
		Optional<User> foundUser = userRepo.findByUsername(username);
		// return null if no user is found
		if(foundUser.isEmpty()) {
			return null;
		}
		return foundUser.get();
	}
	
	/**
	 * validate user credentials based on username and password
	 * @param username
	 * @param password
	 * @return an integer indicating validation result
	 */
	public int validateUser(String username, String password) {
		log.debug("enter validate user userservice with "+username);
		Optional<User> foundUser = userRepo.findByUsername(username);
		// return -1 if no user is found based on the input username
		if(foundUser.isEmpty()) {
			return NO_USER_FOUND;
		}else {
			User expectedUser = foundUser.get();
			// return 1 if user and password matches with database record
			if(expectedUser.getPassword().equals(password)) {
				return VALID_USER;
			}else {
				// return 0 if password does not match with database
				return WRONG_PASSWORD;
			}
		}
		
	}

	/**
	 * check if username already exists in database
	 * @param username
	 * @return a boolean indicating result
	 */
	public boolean ifUserExists(String username) {
		Optional<User> foundUser = userRepo.findByUsername(username);
		// return false if the username is not found from database
		if(foundUser.isEmpty()) {
			return false;
		}
		return true;
	}
	

	/**
	 * remove a user from database
	 * @param user
	 */
	public void removeUser(User user) {
		//create the user if there is no user named deleted in db
		String deletedUsername = "deleted";
		User deleteduser = findByName(deletedUsername);
		if(deleteduser == null) {
			deleteduser = new User();
			deleteduser.setUsername(deletedUsername);
			deleteduser.setfName(deletedUsername);
			deleteduser.setlName(deletedUsername);
			deleteduser.setEmail(deletedUsername);
			deleteduser.setPassword(deletedUsername);
			this.save(deleteduser);
		}
		
		// update creator of reviews and places to deleteduser
		List<Review> reviews = reviewService.findByUser(user);
		for(Review r: reviews) {
			deleteduser.addAReview(r);
			r.setUser(deleteduser);
		}
		
		List<Place> places = placeService.findPlacesByCreator(user);
		for(Place p: places) {
			log.debug("now enter removeuser: findplacebycreator "+p.getName());
			p.setCreator(deleteduser);
			p = placeService.save(p);
			log.debug(p.getCreator().getUsername());
		}
		
		List<Place> favPlaces = user.getFavoritePlaces();
		for(Place p: favPlaces) {
			p.removeSaveUser(user);
			p = placeService.save(p);
			log.debug(p.getCreator().getUsername());
			
		}
		
		user.setCreatedPlaces(new ArrayList<Place>());
		user.setCreatedReviews(new ArrayList<Review>());
		user.setFavoritePlaces(new ArrayList<Place>());
		
		// save the deleteduser and user
		this.save(deleteduser);
		this.save(user);
		
		// delete the user
		userRepo.delete(user);
	}
	 
}

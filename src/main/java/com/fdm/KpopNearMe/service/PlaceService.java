package com.fdm.KpopNearMe.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.KpopNearMe.controller.UserController;
import com.fdm.KpopNearMe.dal.PlaceRepository;
import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;

/**
 * PlaceService is responsible to perform associated validation, read and fetch Place 
 * objects from database via PlaceRepository
 * @author Hailie Long
 *
 */
@Service
public class PlaceService {
	
	private PlaceRepository placeRepo;
	private Log log = LogFactory.getLog(PlaceService.class);
	 
	/**
	 * constructor of PlaceService
	 * @param placeRepo pre-constructed placeRepository interface
	 */
	@Autowired
	public PlaceService(PlaceRepository placeRepo) {
		super();
		this.placeRepo = placeRepo;
	}
	
	/**
	 * save a Place object to database
	 * @param place to be saved
	 * @return saved Place object
	 */
	public Place save(Place place) {
		return placeRepo.save(place);
	}
	
	/**
	 * find a list of Places created by a user
	 * @param user the user created the Place objects
	 * @return found list of places
	 */
	public List<Place> findPlacesByCreator(User user){
		return placeRepo.findByCreator(user);
	}
	
	/**
	 * find all place objects stored in database
	 * @return list of found places
	 */
	public List<Place> findAll(){
		return placeRepo.findAll();
	}

	/**
	 * find a place object by id stored in database
	 * @param placeId of the place to be found
	 * @return found place
	 */
	public Place findPlaceById(int placeId) {
		// TODO Auto-generated method stub
		Optional<Place> foundplace = placeRepo.findById(placeId);
		// return null if nothing found
		if(foundplace.isEmpty()) {
			return null;
		}
		return foundplace.get();
	}

	/**
	 * check if a place exists in database based on its name attribute
	 * @param name place name to be searched in database
	 * @return boolean indicating if the place with such name exists in database
	 */
	public boolean ifPlaceExists(String name) {
		Optional<Place> foundplace = placeRepo.findByName(name);
		if(foundplace.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * update overallRating attribute associated with a place by
	 * going through all the reviews associated with the place, and calculate
	 * the average rating of all the reviews
	 * @param currentPlace place to be updated 
	 * @return the calculated overall rating of the place
	 */
	public double updateOverallRating(Place currentPlace) {
		double sum = 0;
		List<Review> reviews = currentPlace.getReviews();
	
		log.debug("now overall rating becomes "+sum/(reviews.size()));
		// if no reviews stored in the place, return rating as 0
		if(reviews.size() == 0) {
			return 0;
		}
		for (Review r: reviews) {
			sum += r.getRating();
			log.debug(r.getRating());
		}
		
		return sum/(reviews.size());
		
	}
	
	/**
	 * check if a place is valid when inserting it into database
	 * a valid place needs to have its name and address being non-empty,
	 * and should not exists in the database before.
	 * @param place to be checked
	 * @return integer result indicating validation checking result
	 */
	public int ifPlaceValid(Place place) {
		// if place already exists in db
		if(ifPlaceExists(place.getName()) == true) {
			return -1;
		}
		
		// if place name, address being empty
		if(place.getName().isEmpty() || place.getAddress().isEmpty()) {
			return -2;
			
		}
		
		return 1;
	
	}

	
	
	

}

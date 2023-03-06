package com.fdm.KpopNearMe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.KpopNearMe.controller.UserController;
import com.fdm.KpopNearMe.dal.BiasRepository;
import com.fdm.KpopNearMe.model.Bias;
import com.fdm.KpopNearMe.model.Place;

/**
 * BiasService class that is responsible for performing validation, read and write bias objects from/into database.
 * A biasService class contains a BiasRepository that performs direct read/write with database
 * @author Hailie Long
 *
 */
@Service
public class BiasService {
	
	private BiasRepository biasRepo;
	private Log log = LogFactory.getLog(BiasService.class);

	/**
	 * constructor of biasService
	 * @param biasRepo
	 */
	@Autowired
	public BiasService(BiasRepository biasRepo) {
		super();
		this.biasRepo = biasRepo;
	}

	/**
	 * save a bias object into database 
	 * @param returnedSpringBias the created bias object to be saved
	 * @return saved bias object
	 */
	public Bias save(Bias returnedSpringBias) {
		return this.biasRepo.save(returnedSpringBias);
	}

	/** 
	 * find a bias object from database based on biasId
	 * @param biasId id of the bias object in the database to be found
	 * @return found bias object, return null if nothing is found
	 */
	public Bias findById(int biasId) {
		Optional<Bias> foundBias = biasRepo.findById(biasId);
		if(foundBias.isEmpty()) {
			return null;
		}
		return foundBias.get();
	}
 
	/**
	 * remove a bias object from database
	 * @param currBias to be removed
	 */
	public void remove(Bias currBias) {
		this.biasRepo.delete(currBias);
		
	}
	 
	/**
	 * find a bias based on its name attribute from database
	 * @param name of the bias object to be found
	 * @return found bias with the name, return null if nothing is found
	 */
	public Bias findbyName(String name) {
		Optional<Bias> foundbias = biasRepo.findByName(name);
		if(foundbias.isEmpty()) {
			return null;
		}
		return foundbias.get();
	}
	

	/**
	 * check if bias is valid when trying to add the created bias to a place object
	 * @param bias to be added
	 * @param place where bias is going to be added
	 * @return integer indicating if bias is valid for adding to the place
	 */
	public int ifBiasValid(Bias bias, Place place) {
		if(bias.getName().isEmpty()) {
			return -1;
		}
		// check if place already having bias with same name
		if(place.getBiases().contains(bias)) {
			return -2;
		}
		// check if having a bias with same name (case-insensitive)
		// if it is, only add existing bias to place, add place to existing bias
		Bias foundbias = findbyName(bias.getName());
		if(foundbias != null) {
			return 0;
		}
		
		
		return 1;
	}

	/**
	 * find places that has bias with the name input
	 * @param biasName name attribute of bias to be searched
	 * @return found places list containing the bias, return null if nothing is found
	 */
	public List<Place> findPlacesByBias(String biasName) {
		biasName = biasName.toUpperCase();
		// check if there is bias with such name in database
		// return null directly if no bias exists with the name
		Bias foundbias = findbyName(biasName);
		if(foundbias == null) {
			return null;
		}
		log.debug("bias is found");
		// find a list of places with the bias attached 
		List<Place> places = foundbias.getPlaces();
		if(places == null) {
			places = new ArrayList<Place>();
		}
		return places;
	}
	
	

}

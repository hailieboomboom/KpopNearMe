package com.fdm.KpopNearMe.dal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.User;


/**
 * pre-constructed PlaceRepository extended from JPARepository, 
 * and it is responsible for directly communicating with database,
 * acts as an interface, and utilised by PlaceService
 * @author Hailie Long
 *
 */
@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer>{
	
	List<Place> findByCreator(User user);

	Optional<Place> findByName(String name);
	
	
	
	

}

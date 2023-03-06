package com.fdm.KpopNearMe.dal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;

/**
 * pre-constructed ReviewRepository extended from JPARepository, 
 * and it is responsible for directly communicating with database,
 * acts as an interface, and utilised by ReviewService
 * @author Hailie Long
 *
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{

	List<Review> findByUser(User user);

}

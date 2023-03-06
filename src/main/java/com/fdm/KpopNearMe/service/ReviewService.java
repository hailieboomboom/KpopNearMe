package com.fdm.KpopNearMe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.KpopNearMe.dal.ReviewRepository;
import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;

/**
 * 
 * ReviewService class that is responsible for read and fetch reveiw entities data 
 * from database via ReviewRepository
 * @author Hailie Long
 *
 */
@Service
public class ReviewService {
	
	private ReviewRepository reviewRepo;

	/**
	 * constructor of ReviewService object
	 * @param reviewRepo reviewRepository, a constructed interface that is responsible for directly read and fetch from database
	 */
	@Autowired
	public ReviewService(ReviewRepository reviewRepo) {
		super();
		this.reviewRepo = reviewRepo;
	}
	
	/**
	 * save a Review object to database
	 * @param review object
	 * @return the saved review
	 */
	public Review save(Review review) {
		return reviewRepo.save(review);
	}

	/**
	 * find Review object from database based on id
	 * @param reviewId id associated with Review object
	 * @return found Review object
	 */
	public Review findById(int reviewId) {
		Optional<Review> foundReview = reviewRepo.findById(reviewId);
		if(foundReview.isEmpty()) {
			return null;
		}
		return foundReview.get();
	}
	
	/**
	 * remove a review object from database
	 * @param review the review to be deleted
	 */
	public void removeReview(Review review) {
		reviewRepo.delete(review);
	}

	/**
	 * check if a review object is valid.
	 * a review is valid if content and rating not empty, and has a rating between 0 and 5
	 * @param review object to be checked
	 * @return
	 */
	public int ifReviewValid(Review review) {
		// check if rating is between 0 and 5
		if(review.getRating() < 0 || review.getRating() > 5) {
			return -2;
		}
		
		// check if content or rating is empty
		if(review.getContent().isEmpty()) {
			return -1;
		}
		
		return 1;
	}
	
	public List<Review> findByUser(User user){
		return reviewRepo.findByUser(user);
	}
	
	

}

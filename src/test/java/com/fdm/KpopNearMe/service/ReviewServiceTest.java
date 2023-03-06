package com.fdm.KpopNearMe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.fdm.KpopNearMe.dal.ReviewRepository;

import com.fdm.KpopNearMe.model.Review;


/**
 * Tests performed on ReviewService
 * @author Hailie Long
 *
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

	private ReviewService reviewService;
	private Review review;
	private Optional<Review> opReview;
	
	@Mock
	ReviewRepository mockReviewRepo;
	
	@BeforeEach
	void setUp() {
		reviewService = new ReviewService(mockReviewRepo);
		review = new Review();
	}
	
	@Test
	void test_save_review_calls_save_from_reviewRepo() {
		when(mockReviewRepo.save(review)).thenReturn(review);
		
		Review result = reviewService.save(review);
		
		assertEquals(result, review);
		verify(mockReviewRepo, times(1)).save(review);
		
	}
	
	@Test
	void test_findbyid_calls_findbyid_from_reviewRepo_and_return_review_if_found() {
		int reviewId = 1;
		opReview = Optional.of(review);
		when(mockReviewRepo.findById(reviewId)).thenReturn(opReview);
		
		Review result = reviewService.findById(reviewId);
		
		assertEquals(result, review);
		verify(mockReviewRepo, times(1)).findById(reviewId);
		
	}
	
	@Test
	void test_findbyid_calls_findbyid_from_reviewRepo_and_return_null_if_nothing_found() {
		int reviewId = 1;
		opReview = Optional.empty();
		when(mockReviewRepo.findById(reviewId)).thenReturn(opReview);
		
		Review result = reviewService.findById(reviewId);
		
		assertNull(result);
		verify(mockReviewRepo, times(1)).findById(reviewId);
		
	}
	
	@Test
	void test_remove_review_call_delete_from_reviewRepo() {
		reviewService.removeReview(review);
		
		verify(mockReviewRepo, times(1)).delete(review);
	}
	
	@Test
	void test_ifReviewValid_return_negative2_if_rating_less_than_0() {
		
		review.setRating(-5);
		
		int result = reviewService.ifReviewValid(review);
		
		assertEquals(result, -2);
	}
	
	@Test
	void test_ifReviewValid_return_negative2_if_rating_more_than_5() {
		
		review.setRating(50);
		
		int result = reviewService.ifReviewValid(review);
		
		assertEquals(result, -2);
	}
	
	@Test
	void test_ifReviewValid_return_negative1_if_reviewcontent_is_empty() {
		review.setContent("");
		review.setRating(3);
		
		int result = reviewService.ifReviewValid(review);
		
		assertEquals(result, -1);
	}
	
	@Test
	void test_ifReviewValid_return_1_if_reviewcontent_is_not_empty_and_rating_between_0and5() {
		review.setContent("good place");
		review.setRating(3);
		
		int result = reviewService.ifReviewValid(review);
		
		assertEquals(result, 1);
	}

}

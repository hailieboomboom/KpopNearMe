package com.fdm.KpopNearMe.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;
import com.fdm.KpopNearMe.service.PlaceService;
import com.fdm.KpopNearMe.service.UserService;
import com.fdm.KpopNearMe.service.ReviewService;

/**
 * 
 * Tests performed on reviewConroller
 * @author Hailie
 *
 */
@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

	private ReviewController reviewController;
	private Place place;
	private User user;
	private Review review;
	
	@Mock
	ReviewService mockReviewService;
	
	@Mock
	PlaceService mockPlaceService;
	
	@Mock
	UserService mockUserService;
	
	@Mock
	HttpSession mockSession;
	
	@Mock
	Model mockModel;
	
	@BeforeEach
	void setUp() {
		reviewController =  new ReviewController(mockPlaceService, mockUserService, mockReviewService);
		user = new User();
		place = new Place();
		review = new Review();
	}
	
	@Test
	void test_get_create_review_page_return_error_not_logged_in_when_no_user_in_session() {
		int placeId = 1;
		when(mockSession.getAttribute(reviewController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = reviewController.getCreateReviewPage(mockModel, mockSession, placeId);
		
		assertEquals(result, reviewController.REDIRECT_ERROR_NOT_LOGGED_IN_YET );
	
	}
	
	@Test
	void test_createReviewPage_return_createReview_jsp_and_set_model_and_session_attribute_when_user_loggedin() {
		int placeId = 1;
		when((User) mockSession.getAttribute(reviewController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findPlaceById(placeId)).thenReturn(place);
		
		String result = reviewController.getCreateReviewPage(mockModel, mockSession, placeId);
		
		verify(mockSession, times(1)).getAttribute(reviewController.CURRENT_USER_ATTR);
		verify(mockModel, times(1)).addAttribute(reviewController.BIND_REVIEW_ATTR, review);
		verify(mockSession, times(1)).setAttribute(reviewController.CURRENT_PLACE_ATTR, place);
		assertEquals(result, reviewController.CREATE_REVIEW_JSP);
		
	}
	
	
	@Test
	void test_process_create_review_return_error_no_review_content_if_validation_result_from_reviewService_returns_negative1() {
		when(mockReviewService.ifReviewValid(review)).thenReturn(-1);
		
		String result = reviewController.processCreateReview(mockModel, mockSession, review);
		
		verify(mockReviewService, times(1)).ifReviewValid(review);
		assertEquals(result, reviewController.REDIRECT_ERROR_NO_REVIEW_CONTENT_JSP);
	}
	
	@Test
	void test_process_create_review_return_error_rating_outofrange_if_validation_result_from_reviewService_returns_negative2() {
		when(mockReviewService.ifReviewValid(review)).thenReturn(-2);
		
		String result = reviewController.processCreateReview(mockModel, mockSession, review);
		
		verify(mockReviewService, times(1)).ifReviewValid(review);
		assertEquals(result, reviewController.REDIRECT_ERROR_REVIEW_RATING_OUT_OF_RANGE_JSP);
	}
	
	@Test
	void test_process_create_review_return_createReview_jsp_and_modify_database_and_update_session_attr_if_successful() {
	
		double rating = 5;

		List<Place> places = new ArrayList<Place>();
		user.setCreatedReviews(new ArrayList<Review>());
		place.setReviews(new ArrayList<Review>());
		when(mockReviewService.ifReviewValid(review)).thenReturn(1);
		when((User) mockSession.getAttribute(reviewController.CURRENT_USER_ATTR)).thenReturn(user);
		when((Place) mockSession.getAttribute(reviewController.CURRENT_PLACE_ATTR)).thenReturn(place);
		when(mockPlaceService.updateOverallRating(place)).thenReturn(rating);
		
		
		String result = reviewController.processCreateReview(mockModel, mockSession, review);
		
		verify(mockSession, times(1)).getAttribute(reviewController.CURRENT_USER_ATTR);
		verify(mockSession, times(1)).getAttribute(reviewController.CURRENT_PLACE_ATTR);
		
		assertEquals(place, review.getPlace());
		assertEquals(user, review.getUser());
		assertTrue(user.getCreatedReviews().contains(review));
		assertTrue(place.getReviews().contains(review));
		verify(mockPlaceService, times(1)).updateOverallRating(place);
		assertEquals(place.getOverallRating(), rating);
		verify(mockPlaceService, times(1)).save(place);
		verify(mockUserService, times(1)).save(user);
		verify(mockReviewService, times(1)).save(review);
		
		verify(mockSession, times(1)).removeAttribute(reviewController.ALL_PLACES_ATTR);
		verify(mockSession, times(1)).setAttribute(reviewController.ALL_PLACES_ATTR, places);
		verify(mockSession, times(1)).removeAttribute(reviewController.CURRENT_PLACE_ATTR);
		verify(mockSession, times(1)).setAttribute(reviewController.CURRENT_PLACE_ATTR, place);
		
		verify(mockModel, times(1)).addAttribute(reviewController.BIND_REVIEW_ATTR, review);
	
		
		assertEquals(result, reviewController.CREATE_REVIEW_JSP);
	}
	
	@Test
	void test_get_no_review_content_error_will_add_model_errormsg_attr_and_return_createReview_jsp() {
		String result = reviewController.getNoReviewContent(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(reviewController.ERROR_MESSAGE_ATTR, reviewController.ERROR_EMPTY_REVIEW_CONTENT_MSG);
		verify(mockModel, times(1)).addAttribute(reviewController.BIND_REVIEW_ATTR, review);
		assertEquals(result, reviewController.CREATE_REVIEW_JSP);
	}
	
	@Test
	void test_get_review_rating_outofrange_error_will_add_model_errormsg_attr_and_return_createReview_jsp() {
		String result = reviewController.getReviewRatingOutofRange(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(reviewController.ERROR_MESSAGE_ATTR, reviewController.ERROR_REVIEW_RATING_OUTOFRANGE_MSG);
		verify(mockModel, times(1)).addAttribute(reviewController.BIND_REVIEW_ATTR, review);
		assertEquals(result, reviewController.CREATE_REVIEW_JSP);
	}
	
	@Test
	void test_delete_review_will_return_error_not_loggedin_if_no_user_loggedin() {
		int reviewId = 1;
		when((User) mockSession.getAttribute(reviewController.CURRENT_USER_ATTR)).thenReturn(null);
		when(mockReviewService.findById(reviewId)).thenReturn(review);
		
		String result = reviewController.deleteAReview(mockModel, mockSession, reviewId);
		
		verify(mockSession, times(1)).getAttribute(reviewController.CURRENT_USER_ATTR);
		assertEquals(result, reviewController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);
	}

	@Test
	void test_delete_review_will_modify_db_and_update_session_attr_and_return_createReview_jsp_if_successful() {
		int reviewId = 1;
		double rating = 3;
		user.setCreatedReviews(new ArrayList<Review>());
		place.setReviews(new ArrayList<Review>());
		user.addAReview(review);
		place.addAReview(review);
		review.setUser(user);
		review.setPlace(place);
		when((User) mockSession.getAttribute(reviewController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockReviewService.findById(reviewId)).thenReturn(review);
		when(mockSession.getAttribute(reviewController.CURRENT_PLACE_ATTR)).thenReturn(place);
		when(mockPlaceService.updateOverallRating(place)).thenReturn(rating);
		
		
		String result = reviewController.deleteAReview(mockModel, mockSession, reviewId);
		
		
		verify(mockReviewService, times(1)).findById(reviewId);
		verify(mockSession, times(1)).getAttribute(reviewController.CURRENT_USER_ATTR);
		verify(mockSession, times(1)).getAttribute(reviewController.CURRENT_PLACE_ATTR);
		assertFalse(user.getCreatedReviews().contains(review));
		assertFalse(place.getReviews().contains(review));
		assertEquals(place.getOverallRating(), rating);
		verify(mockUserService, times(1)).save(user);
		verify(mockPlaceService, times(1)).save(place);
		verify(mockReviewService, times(1)).removeReview(review);
		
		verify(mockSession, times(1)).removeAttribute(reviewController.CURRENT_USER_ATTR);
		verify(mockSession, times(1)).setAttribute(reviewController.CURRENT_PLACE_ATTR, place);
		verify(mockSession, times(1)).removeAttribute(reviewController.CURRENT_USER_ATTR);
		verify(mockSession, times(1)).setAttribute(reviewController.CURRENT_USER_ATTR, user);
		verify(mockModel, times(1)).addAttribute(reviewController.BIND_REVIEW_ATTR, review);
		
		assertEquals(result, reviewController.CREATE_REVIEW_JSP);
	}
	

}

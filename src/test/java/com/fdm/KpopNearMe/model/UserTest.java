package com.fdm.KpopNearMe.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

	private User user;
	private Place place;
	private Review review;
	
	@BeforeEach
	void setup() {
		user = new User();
		place = new Place();
		review = new Review();
	}
	
	@Test
	void test_favorite_a_place_will_add_place_if_place_not_in_favPlaces_list() {
		user.favoriteAPlace(place);
		
		assertTrue(user.getFavoritePlaces().contains(place));
	}
	
	@Test
	void test_favorite_a_place_willnot_work_if_place_already_in_favPlaces_list() {
		List<Place> places = new ArrayList<Place>();
		places.add(place);
		user.setFavoritePlaces(places);
		
		user.favoriteAPlace(place);
		
		assertEquals(user.getFavoritePlaces().size(), places.size());
	}
	
	@Test
	void test_remove_fav_place_will_work_if_place_in_favPlaces_list() {
		List<Place> places = new ArrayList<Place>();
		places.add(place);
		user.setFavoritePlaces(places);
		
		user.removeAfavoritePlace(place);
		
		assertFalse(user.getFavoritePlaces().contains(place));
	}
	
	@Test
	void test_remove_fav_place_will_not_work_if_place_not_in_favPlaces_list() {
		List<Place> places = new ArrayList<Place>();
		Place p1 = new Place();
		p1.setName("test");
		places.add(p1);
		user.setFavoritePlaces(places);
		
		user.removeAfavoritePlace(place);
		
		assertTrue(user.getFavoritePlaces().contains(p1));
		assertEquals(user.getFavoritePlaces().size(), places.size());
	}
	
	@Test
	void test_add_review_will_work_if_review_not_in_createdReviews_list() {
		user.addAReview(review);
		
		assertTrue(user.getCreatedReviews().contains(review));
	}
	
	@Test
	void test_add_review_will_not_work_if_review_already_in_createdReviews_list() {
		List<Review> reviews = new ArrayList<Review>();
		reviews.add(review);
		user.setCreatedReviews(reviews);
		
		user.addAReview(review);
		
		assertEquals(user.getCreatedReviews().size(), reviews.size());
	}
	
	@Test
	void test_remove_review_will_work_if_review_in_createdReview_list() {
		List<Review> reviews = new ArrayList<Review>();
		reviews.add(review);
		user.setCreatedReviews(reviews);
		
		user.removeAReview(review);
		
		assertFalse(user.getCreatedReviews().contains(review));
	}
	
	@Test
	void test_remove_review_will__not_work_if_review_notin_createdReview_list() {
		List<Review> reviews = new ArrayList<Review>();
		Review r1 = new Review();
		r1.setContent("test");
		reviews.add(r1);
		user.setCreatedReviews(reviews);
		
		user.removeAReview(review);
		
		assertTrue(user.getCreatedReviews().contains(r1));
		assertEquals(user.getCreatedReviews().size(), reviews.size());
	}

}

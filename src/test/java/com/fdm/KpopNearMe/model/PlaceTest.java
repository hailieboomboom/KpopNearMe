package com.fdm.KpopNearMe.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class PlaceTest {
	
	private Place place;
	private User user;
	private Review review;
	private Bias bias;
	
	@BeforeEach
	void setup() {
		place = new Place();
		user = new User();
		review = new Review();
		bias = new Bias();
	}

	@Test
	void test_add_user_will_add_user_if_user_not_in_saveduser_list() {
		
		place.addUser(user);
		
		assertTrue(place.getSavedUsers().contains(user));
	}
	
	@Test
	void test_add_user_will_add_not_user_if_user_already_in_saveduser_list() {
		List<User> users = new ArrayList<User>();
		users.add(user);
		place.setSavedUsers(users);
		
		place.addUser(user);
		
		assertEquals(users.size(), place.getSavedUsers().size());
	}
	
	@Test
	void test_remove_user_will_remove_user_if_in_the_saveduser() {
		List<User> users = new ArrayList<User>();
		users.add(user);
		place.setSavedUsers(users);
		
		place.removeSaveUser(user);
		
		assertFalse(place.getSavedUsers().contains(user));
	}
	
	@Test
	void test_remove_user_will_not_work_if_user_not_in_saved_user_list() {
		List<User> users = new ArrayList<User>();
		User u1 = new User();
		u1.setfName("test");
		users.add(u1);
		place.setSavedUsers(users);
		
		place.removeSaveUser(user);
		
		assertEquals(users.size(), place.getSavedUsers().size());
		assertTrue(place.getSavedUsers().contains(u1));
	}
	
	@Test
	void test_add_review_will_work_if_review_not_in_reviews_list() {
		place.addAReview(review);
		
		assertTrue(place.getReviews().contains(review));
	}
	
	@Test
	void test_add_review_will_not_work_if_review_already_in_reviews_list() {
		List<Review> reviews = new ArrayList<Review>();
		reviews.add(review);
		place.setReviews(reviews);
		
		place.addAReview(review);
		
		assertEquals(place.getReviews().size(), reviews.size());
	}
	
	@Test
	void test_remove_review_will_work_if_review_in_list() {
		List<Review> reviews = new ArrayList<Review>();
		reviews.add(review);
		place.setReviews(reviews);
		
		place.removeAReview(review);
		
		assertFalse(place.getReviews().contains(review));
		
	}

	@Test
	void test_remove_review_will__not_work_if_review_notin_list() {
		List<Review> reviews = new ArrayList<Review>();
		Review r1 = new Review();
		r1.setContent("TEST");
		reviews.add(r1);
		place.setReviews(reviews);
		
		place.removeAReview(review);
		
		assertTrue(place.getReviews().contains(r1));
		assertEquals(place.getReviews().size(), reviews.size());
		
	}
	
	@Test
	void test_add_bias_will_work_if_bias_not_in_list() {
		place.addABias(bias);
		
		assertTrue(place.getBiases().contains(bias));
	}
	
	@Test
	void test_add_bias_will_not_work_if_bias_already_in_list() {
		List<Bias> biases = new ArrayList<Bias>();
		biases.add(bias);
		place.setBiases(biases);
		
		place.addABias(bias);
		
		assertEquals(place.getBiases().size(), biases.size());
	}
	
	@Test
	void test_remove_bias_will_work_if_bias_in_biases_list() {
		List<Bias> biases = new ArrayList<Bias>();
		biases.add(bias);
		place.setBiases(biases);
		
		place.removeABias(bias);
		
		assertFalse(place.getBiases().contains(bias));
	}
	
	@Test
	void test_remove_bias_will_not_work_if_bias_notin_biases_list() {
		List<Bias> biases = new ArrayList<Bias>();
		Bias b1 = new Bias();
		b1.setName("test");
		biases.add(b1);
		place.setBiases(biases);
		
		place.removeABias(bias);
		
		assertTrue(place.getBiases().contains(b1));
		assertEquals(place.getBiases().size(), biases.size());
	}
}

package com.fdm.KpopNearMe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdm.KpopNearMe.dal.PlaceRepository;
import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;

/**
 * Tests performed on placeService
 * @author Hailie Long
 *
 */
@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {
	
	private PlaceService placeService;
	private Place place;
	private List<Place> places;
	private User user;
	private Optional<Place> opplace;
	
	@Mock
	PlaceRepository mockPlaceRepo;
	
	@BeforeEach
	void setup() {
		placeService =  new PlaceService(mockPlaceRepo);
		place = new Place();
		places = new ArrayList<Place>();
		user = new User();
	}

	@Test
	void test_save_place_call_save_from_placerepo() {
		when(mockPlaceRepo.save(place)).thenReturn(place);
		
		Place result = placeService.save(place);
		
		verify(mockPlaceRepo, times(1)).save(place);
		assertEquals(place, result);
		
	}
	
	@Test
	void test_find_places_by_creator_call_findByCreator_from_placeRepo() {
		when(mockPlaceRepo.findByCreator(user)).thenReturn(places);
		
		List<Place> foundPlaces = placeService.findPlacesByCreator(user);
		
		verify(mockPlaceRepo, times(1)).findByCreator(user);
		assertEquals(foundPlaces, places);
	}
	
	@Test
	void test_findall_call_findall_from_placeRepo() {
		when(mockPlaceRepo.findAll()).thenReturn(places);
		
		List<Place> foundPlaces = placeService.findAll();
		
		verify(mockPlaceRepo, times(1)).findAll();
		assertEquals(foundPlaces, places);
	}
	
	@Test
	void test_find_place_by_id_return_null_if_nothing_found() {
		opplace = Optional.empty();
		int placeId = 1;
		when(mockPlaceRepo.findById(placeId)).thenReturn(opplace);
		
		Place result = placeService.findPlaceById(placeId);
		
		verify(mockPlaceRepo, times(1)).findById(placeId);
		assertNull(result);
		
	}
	
	@Test
	void test_find_place_by_id_return_place_if_placeis_found() {
		opplace = Optional.of(place);
		int placeId = 1;
		when(mockPlaceRepo.findById(placeId)).thenReturn(opplace);
		
		Place result = placeService.findPlaceById(placeId);
		
		verify(mockPlaceRepo, times(1)).findById(placeId);
		assertEquals(result, place);
		
	}
	
	@Test
	void test_ifPlaceExists_return_false_if_nothing_found_by_placeRepo() {
		opplace = Optional.empty();
		String name = "test";
		when(mockPlaceRepo.findByName(name)).thenReturn(opplace);
		
		boolean result = placeService.ifPlaceExists(name);
		
		verify(mockPlaceRepo, times(1)).findByName(name);
		assertFalse(result);
	}

	@Test
	void test_ifPlaceExists_return_true_if_a_place_found_by_placeRepo() {
		opplace = Optional.of(place);
		String name = "test";
		when(mockPlaceRepo.findByName(name)).thenReturn(opplace);
		
		boolean result = placeService.ifPlaceExists(name);
		
		verify(mockPlaceRepo, times(1)).findByName(name);
		assertTrue(result);
	}
	
	@Test
	void test_updateoverallrating_return_0_if_no_reviews_found_with_place() {
		List<Review> reviews = new ArrayList<Review>();
		place.setReviews(reviews);
		
		double result = placeService.updateOverallRating(place);
		
		assertEquals(result, 0);
	}
	
	@Test
	void test_updateoverallrating_return_all_review_avg_if_many_reviews_found_with_place() {
		List<Review> reviews = new ArrayList<Review>();
		place.setReviews(reviews);
		Review r1 = new Review();
		Review r2 = new Review();
		Review r3 = new Review();
		r1.setRating(1);
		r2.setRating(3);
		r3.setRating(2);
		place.addAReview(r1);
		place.addAReview(r2);
		place.addAReview(r3);
		
		double result = placeService.updateOverallRating(place);
		
		assertEquals(result, 2);
	}
	
	@Test
	void test_if_place_valid_return_negative1_if_place_already_exists_in_db() {
		opplace = Optional.of(place);
		String name = "test";
		place.setName(name);
		when(mockPlaceRepo.findByName(name)).thenReturn(opplace);
		
		int result = placeService.ifPlaceValid(place);
		
		verify(mockPlaceRepo, times(1)).findByName(name);
		assertEquals(result, -1);
		
	}
	
	@Test
	void test_if_place_valid_return_negative2_if_placeName_is_empty() {
		opplace = Optional.empty();
		String name = "";
		place.setName(name);
		when(mockPlaceRepo.findByName(name)).thenReturn(opplace);
		
		int result = placeService.ifPlaceValid(place);
		
		verify(mockPlaceRepo, times(1)).findByName(name);
		assertEquals(result, -2);
		
	}
	
	@Test
	void test_if_place_valid_return_negative2_if_place_address_is_empty() {
		opplace = Optional.empty();
		String name = "test";
		String address = "";
		place.setName(name);
		place.setAddress(address);
		when(mockPlaceRepo.findByName(name)).thenReturn(opplace);
		
		int result = placeService.ifPlaceValid(place);
		
		verify(mockPlaceRepo, times(1)).findByName(name);
		assertEquals(result, -2);
		
	}
	
	
	@Test
	void test_if_place_valid_return_1_if_place_info_is_not_empty_and_not_exists_in_db() {
		opplace = Optional.empty();
		String name = "test";
		String address = "test";
		place.setName(name);
		place.setAddress(address);
		when(mockPlaceRepo.findByName(name)).thenReturn(opplace);
		
		int result = placeService.ifPlaceValid(place);
		
		verify(mockPlaceRepo, times(1)).findByName(name);
		assertEquals(result, 1);
		
	}
}

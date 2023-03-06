package com.fdm.KpopNearMe.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class BiasTest {
	
	private Bias bias;
	private Place place;
	
	
	
	@BeforeEach
	void setup() {
		bias = new Bias();
		place = new Place();
	}
	
	@Test
	void test_addAPlace_will_not_cause_error_when_places_list_is_null() {
		bias.addAPlace(place);
		
		assertFalse(bias.getPlaces() == null);
	}

	@Test
	void test_add_a_place_will_add_a_place_if_not_in_places_list_from_bias_yet() {
		bias.addAPlace(place);
		
		assertTrue(bias.getPlaces().contains(place));
	}
	
	@Test
	void test_add_a_place_will_not_work_if_place_already_in_places_list() {
		List<Place> places = new ArrayList<Place>();
		places.add(place);
		
		bias.setPlaces(places);
		
		bias.addAPlace(place);
		
		assertEquals(bias.getPlaces().size(), 1);
		
	}
	
	@Test
	void test_remove_a_place_will_remove_the_place_if_itis_in_places_list() {
		List<Place> places = new ArrayList<Place>();
		places.add(place);
		bias.setPlaces(places);
		
		bias.removeAPlace(place);
		
		assertFalse(bias.getPlaces().contains(place));
	}
	
	@Test
	void test_remove_a_place_will_not_work_if_place_not_in_places_list() {
		List<Place> places = new ArrayList<Place>();
		Place p1 = new Place();
		p1.setName("test");
		places.add(p1);
		bias.setPlaces(places);
		
		bias.removeAPlace(place);
		
		assertTrue(bias.getPlaces().contains(p1));
		assertEquals(bias.getPlaces().size(), places.size());
	}

}

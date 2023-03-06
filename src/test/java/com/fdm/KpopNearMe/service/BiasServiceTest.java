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

import com.fdm.KpopNearMe.dal.BiasRepository;

import com.fdm.KpopNearMe.model.Bias;
import com.fdm.KpopNearMe.model.Place;


/**
 * Tests performed on BiasService
 * @author Hailie Long
 *
 */
@ExtendWith(MockitoExtension.class)
class BiasServiceTest {
	
	private BiasService biasService;
	private Bias bias;
	private Place place;
	private Optional<Bias> opBias;
	
	@Mock
	BiasRepository mockBiasRepo;
	
	@BeforeEach
	void setup() {
		biasService = new BiasService(mockBiasRepo);
		bias = new Bias();
		place =  new Place();
	}

	@Test
	void test_save_place_call_save_from_biasRepo() {
		when(mockBiasRepo.save(bias)).thenReturn(bias);
		
		Bias result = biasService.save(bias);
		
		assertEquals(result, bias);
		verify(mockBiasRepo, times(1)).save(bias);
	}
	
	@Test
	void test_findbyid_calls_findById_from_biasRepo_and_returns_null_if_nothing_found() {
		opBias = Optional.empty();
		int biasId = 1;
		when(mockBiasRepo.findById(biasId)).thenReturn(opBias);
		
		Bias result = biasService.findById(biasId);
		
		assertNull(result);
		verify(mockBiasRepo, times(1)).findById(biasId);
	}
	
	@Test
	void test_findbyid_calls_findById_from_biasRepo_and_returns_bias__found_by_biasRepo() {
		opBias = Optional.of(bias);
		int biasId = 1;
		when(mockBiasRepo.findById(biasId)).thenReturn(opBias);
		
		Bias result = biasService.findById(biasId);
		
		assertEquals(result, bias);
		verify(mockBiasRepo, times(1)).findById(biasId);
	}
	
	@Test
	void test_remove_bias_call_delete_bias_from_biasRepo() {
		biasService.remove(bias);
		
		verify(mockBiasRepo, times(1)).delete(bias);
	}
	
	@Test
	void test_findbyname_calls_findByName_from_biasRepo_and_returns_null_if_nothing_found() {
		opBias = Optional.empty();
		String name = "test";
		when(mockBiasRepo.findByName(name)).thenReturn(opBias);
		
		Bias result = biasService.findbyName(name);
		
		assertNull(result);
		verify(mockBiasRepo, times(1)).findByName(name);
	}
	
	@Test
	void test_findbyname_calls_findByName_from_biasRepo_and_returns_bias_found_by_biasRepo() {
		opBias = Optional.of(bias);
		String name = "test";
		when(mockBiasRepo.findByName(name)).thenReturn(opBias);
		
		Bias result = biasService.findbyName(name);
		
		assertEquals(bias, result);
		verify(mockBiasRepo, times(1)).findByName(name);
	}
	
	@Test
	void test_ifBiasValid_return_negative1_if_biasname_empty() {
		bias.setName("");
		
		int result = biasService.ifBiasValid(bias, place);
		
		assertEquals(result, -1);
	}

	@Test
	void test_ifBiasValid_return_negative2_if_place_has_same_bias_already() {
		bias.setName("test");
		place.setBiases(new ArrayList<Bias>());
		place.addABias(bias);
		int result = biasService.ifBiasValid(bias, place);
		
		assertEquals(result, -2);
	}
	
	@Test
	void test_ifBiasValid_return_0_if_already_having_bias_with_same_name() {
		opBias = Optional.of(bias);
		String name = "test";
		place.setBiases(new ArrayList<Bias>());
		when(mockBiasRepo.findByName(name)).thenReturn(opBias);
		bias.setName(name);
		int result = biasService.ifBiasValid(bias, place);
		
		assertEquals(result, 0);
		verify(mockBiasRepo, times(1)).findByName(name);
	}
	
	@Test
	void test_ifBiasValid_return_1_if_no_bias_exists_in_db_and_name_is_not_empty() {
		opBias = Optional.empty();
		String name = "test";
		place.setBiases(new ArrayList<Bias>());
		when(mockBiasRepo.findByName(name)).thenReturn(opBias);
		bias.setName(name);
		int result = biasService.ifBiasValid(bias, place);
		
		assertEquals(result, 1);
		verify(mockBiasRepo, times(1)).findByName(name);
	}
	
	@Test
	void test_findPlacesByBias_return_null_if_nobiasfound() {
		opBias = Optional.empty();
		String name = "TEST";
		when(mockBiasRepo.findByName(name)).thenReturn(opBias);
		
		List<Place> places = biasService.findPlacesByBias(name);
		
		assertNull(places);
		verify(mockBiasRepo, times(1)).findByName(name);
	}
	
	@Test
	void test_findPlacesByBias_return_empty_list_of_places_if_bias_found_and_has_no_places_associated() {
		opBias = Optional.of(bias);
		String name = "TEST";
		
		when(mockBiasRepo.findByName(name)).thenReturn(opBias);
		
		List<Place> places = biasService.findPlacesByBias(name);
		
		assertEquals(places, new ArrayList<Place>());
		verify(mockBiasRepo, times(1)).findByName(name);
	}
	

	@Test
	void test_findPlacesByBias_return_list_of_places_if_bias_found_and_has_places_associated() {
		opBias = Optional.of(bias);
		String name = "TEST";
		bias.setPlaces(new ArrayList<Place>());
		bias.addAPlace(place);
		
		when(mockBiasRepo.findByName(name)).thenReturn(opBias);
		
		List<Place> result = biasService.findPlacesByBias(name);
		
		assertEquals(result, bias.getPlaces());
		verify(mockBiasRepo, times(1)).findByName(name);
	}
	
}


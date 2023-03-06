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

import com.fdm.KpopNearMe.model.Bias;
import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.User;
import com.fdm.KpopNearMe.service.BiasService;
import com.fdm.KpopNearMe.service.PlaceService;

/**
 * 
 * Tests performed on biasController
 * @author Hailie Long
 *
 */
@ExtendWith(MockitoExtension.class)
class BiasControllerTest {
	
	private BiasController biasController;
	private Bias bias;
	private Place place;
	private User user;
	
	@Mock
	BiasService mockBiasService;
	
	@Mock
	PlaceService mockPlaceService;
	
	@Mock
	HttpSession mockSession;
	
	@Mock
	Model mockModel;
	
	@BeforeEach
	void setup() {
		biasController = new BiasController(mockPlaceService, mockBiasService);
		bias = new Bias();
		place = new Place();
		user = new User();
	}

	@Test
	void test_create_bias_return_error_notloggedin_when_no_user_in_session() {
		int placeId = 1;
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = biasController.getCreateBiasPage(mockModel, mockSession, placeId);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		
		assertEquals(result, biasController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);
	}
	
	@Test
	void test_create_bias_return_create_bias_jsp_and_modify_session_and_model_attr_when_user_logged_in() {
		int placeId = 1;
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findPlaceById(placeId)).thenReturn(place);
		
		String result = biasController.getCreateBiasPage(mockModel, mockSession, placeId);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		verify(mockModel, times(1)).addAttribute(biasController.BIND_BIAS_ATTR, bias);
		verify(mockSession, times(1)).setAttribute(biasController.CURRENT_PLACE_ATTR, place);
		
		assertEquals(result, biasController.CREATE_BIAS_JSP);
	}
	
	@Test
	void test_process_create_bias_will_return_error_empty_bias_name_if_biasService_return_validation_result_negative1() {
		bias.setName("test");
		when((Place) mockSession.getAttribute(biasController.CURRENT_PLACE_ATTR)).thenReturn(place);
		when(mockBiasService.ifBiasValid(bias, place)).thenReturn(-1);
		
		String result = biasController.processCreateBias(mockModel, mockSession, bias);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_PLACE_ATTR);
		assertEquals(bias.getName(), "TEST");
		verify(mockBiasService, times(1)).ifBiasValid(bias, place);
		assertEquals(result, biasController.REDIRECT_ERROR_EMPTY_BIAS_NAME_JSP);
	}
	
	@Test
	void test_process_create_bias_will_return_error_place_already_exists_if_biasService_return_validation_result_negative2() {
		bias.setName("test");
		when((Place) mockSession.getAttribute(biasController.CURRENT_PLACE_ATTR)).thenReturn(place);
		when(mockBiasService.ifBiasValid(bias, place)).thenReturn(-2);
		
		String result = biasController.processCreateBias(mockModel, mockSession, bias);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_PLACE_ATTR);
		assertEquals(bias.getName(), "TEST");
		verify(mockBiasService, times(1)).ifBiasValid(bias, place);
		assertEquals(result, biasController.REDIRECT_ERROR_PLACE_EXISTS_BIAS);
	}
	
	@Test
	void test_process_create_bias_will_add_existing_bias_to_place_if_biasService_return_validation_result_0() {
		bias.setName("test");
		String inputName = "TEST";
		bias.setPlaces(new ArrayList<Place>());
		place.setBiases(new ArrayList<Bias>());
		
		when((Place) mockSession.getAttribute(biasController.CURRENT_PLACE_ATTR)).thenReturn(place);
		when(mockBiasService.ifBiasValid(bias, place)).thenReturn(0);
		when(mockBiasService.findbyName(inputName)).thenReturn(bias);
		
		String result = biasController.processCreateBias(mockModel, mockSession, bias);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_PLACE_ATTR);
		assertEquals(bias.getName(), inputName);
		verify(mockBiasService, times(1)).ifBiasValid(bias, place);
		verify(mockBiasService, times(1)).findbyName(inputName);
		assertTrue(bias.getPlaces().contains(place));
		assertTrue(place.getBiases().contains(bias));
		verify(mockPlaceService, times(1)).save(place);
		verify(mockBiasService, times(1)).save(bias);
		
		verify(mockSession, times(1)).removeAttribute(biasController.CURRENT_PLACE_ATTR);
		verify(mockSession, times(1)).setAttribute(biasController.CURRENT_PLACE_ATTR, place);
		verify(mockModel, times(1)).addAttribute(biasController.BIND_BIAS_ATTR, new Bias());
		
		assertEquals(result, biasController.CREATE_BIAS_JSP);
		
	}
	

	@Test
	void test_process_create_bias_will_create_and_add_new_bias_to_place_if_biasService_return_validation_result_0() {
		bias.setName("test");
		String inputName = "TEST";
		bias.setPlaces(new ArrayList<Place>());
		place.setBiases(new ArrayList<Bias>());
		
		when((Place) mockSession.getAttribute(biasController.CURRENT_PLACE_ATTR)).thenReturn(place);
		when(mockBiasService.ifBiasValid(bias, place)).thenReturn(1);
		
		String result = biasController.processCreateBias(mockModel, mockSession, bias);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_PLACE_ATTR);
		assertEquals(bias.getName(), inputName);
		verify(mockBiasService, times(1)).ifBiasValid(bias, place);

		assertTrue(bias.getPlaces().contains(place));
		assertTrue(place.getBiases().contains(bias));
		verify(mockPlaceService, times(1)).save(place);
		verify(mockBiasService, times(1)).save(bias);
		
		verify(mockSession, times(1)).removeAttribute(biasController.CURRENT_PLACE_ATTR);
		verify(mockSession, times(1)).setAttribute(biasController.CURRENT_PLACE_ATTR, place);
		verify(mockModel, times(1)).addAttribute(biasController.BIND_BIAS_ATTR, new Bias());
		
		assertEquals(result, biasController.CREATE_BIAS_JSP);
		
	}
	
	@Test
	void test_get_error_empty_bias_name_will_add_errormsg_attr_to_model_and_return_create_bias_jsp() {
		String result =  biasController.getEmptyBiasName(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(biasController.ERROR_MESSAGE_ATTR, biasController.ERROR_EMPTY_BIAS_NAME_MSG);
		assertEquals(result, biasController.CREATE_BIAS_JSP);
	}
	
	@Test
	void test_get_error_place_exists_bias_add_errormsg_attr_to_model_and_return_create_bias_jsp() {
		String result =  biasController.getPlaceExistsBias(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(biasController.ERROR_MESSAGE_ATTR, biasController.ERROR_PLACE_HAS_BIAS_MSG);
		assertEquals(result, biasController.CREATE_BIAS_JSP);
	}
	
	@Test
	void test_delete_bias_will_return_not_loggedin_error_if_no_user_in_session() {
		int placeId = 1;
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = biasController.deleteABias(mockModel, mockSession, placeId);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		
		assertEquals(result, biasController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);
	
	}
	
	@Test
	void test_delete_bias_will_modify_db_and_update_session_model_attr_and_return_create_bias_jsp_if_successful() {
		int placeId = 1;
		int biasId =1;
		bias.setId(biasId);
		bias.setPlaces(new ArrayList<Place>());
		place.setBiases(new ArrayList<Bias>());
		bias.addAPlace(place);
		place.addABias(bias);
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockBiasService.findById(biasId)).thenReturn(bias);
		when((Place) mockSession.getAttribute(biasController.CURRENT_PLACE_ATTR)).thenReturn(place);
		
		
		String result = biasController.deleteABias(mockModel, mockSession, placeId);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		verify(mockBiasService, times(1)).findById(biasId);
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_PLACE_ATTR);
		assertFalse(bias.getPlaces().contains(place));
		assertFalse(place.getBiases().contains(bias));
		verify(mockPlaceService, times(1)).save(place);
		verify(mockBiasService, times(1)).save(bias);
		
		verify(mockSession, times(1)).removeAttribute(biasController.CURRENT_PLACE_ATTR);
		verify(mockSession, times(1)).setAttribute(biasController.CURRENT_PLACE_ATTR, place);
		verify(mockModel, times(1)).addAttribute(biasController.BIND_BIAS_ATTR, new Bias());
		
		assertEquals(result, biasController.CREATE_BIAS_JSP);
	}
	
	@Test
	void test_get_search_page_return_not_loggedin_error_if_no_user_loggedin() {
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = biasController.getSearchPlaceBasedOnBias(mockModel, mockSession);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		assertEquals(result, biasController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);
	}
	
	@Test
	void test_get_search_page_return_searchPlace_page_if_user_loggedin() {
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = biasController.getSearchPlaceBasedOnBias(mockModel, mockSession);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		assertEquals(result, biasController.SEARCH_PLACE_JSP);
	}
	
	@Test
	void test_process_SearchPlace_return_not_loggedin_if_no_user_in_session() {
		String searchInput = "test";
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(null);
		
		
		String result = biasController.processSearchPlace(mockModel, mockSession, searchInput);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		assertEquals(result, biasController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);
	}
	
	@Test
	void test_process_SearchPlace_return_error_empty_input_if_no_input_caught() {
		String searchInput = "test";
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = biasController.processSearchPlace(mockModel, mockSession, "");
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		assertEquals(result, biasController.REDIRECT_ERROR_EMPTY_SEARCH_INPUT);
	}
	
	
	@Test
	void test_process_SearchPlace_return_matched_places_by_biasService_and_searchplace_jsp() {
		String searchInput = "test";
		List<Place> places = new ArrayList<Place>();
		when((User)mockSession.getAttribute(biasController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockBiasService.findPlacesByBias(searchInput)).thenReturn(places);
		
		String result = biasController.processSearchPlace(mockModel, mockSession, searchInput);
		
		verify(mockSession, times(1)).getAttribute(biasController.CURRENT_USER_ATTR);
		verify(mockBiasService, times(1)).findPlacesByBias(searchInput);
		verify(mockSession, times(1)).setAttribute(biasController.FOUND_SEARCH_PLACES_ATTR, places);
		assertEquals(result, biasController.SEARCH_PLACE_JSP);
	}
	
	@Test
	void test_get_empty_search_input_will_add_errormsg_to_model_and_return_searchplace_jsp() {
		String result = biasController.getEmptySearchInput(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(biasController.ERROR_MESSAGE_ATTR, biasController.ERROR_EMPTY_SEARCH_INPUT_MSG);
		assertEquals(result, biasController.SEARCH_PLACE_JSP);
	}
}

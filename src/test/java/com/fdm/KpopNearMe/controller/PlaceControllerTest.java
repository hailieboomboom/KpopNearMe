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

/**
 * 
 * Tests performed on placeController
 * @author Hailie
 *
 */
@ExtendWith(MockitoExtension.class)
public class PlaceControllerTest {
	
	private PlaceController placeController;
	private Place place;
	private User user;
	
	@Mock
	Model mockModel;
	
	@Mock
	HttpSession mockSession;
	
	@Mock
	PlaceService mockPlaceService;
	
	@Mock
	UserService mockUserService;
	
	
	@BeforeEach
	void setup() {
		placeController = new PlaceController(mockPlaceService, mockUserService);
		user = new User();
		place = new Place();
		
	}
 
	@Test
	void test_get_create_place_will_return_errorNotLoggedinYet_if_no_session_user() {
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.getCreatePlacePage(mockModel, mockSession);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET );
	}
	
	@Test
	void test_get_create_place_add_model_attribute_bindplace_and_return_createplace_jsp_when_user_in_session() {
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = placeController.getCreatePlacePage(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(placeController.BIND_PLACE_ATTR, place);
		assertEquals(result,placeController.CREATE_PLACE_JSP );
	}
	
	@Test
	void test_process_create_place_call_placeService_to_check_ifplacevalid() {
		String result = placeController.processPostCreatePlace(place, mockModel, mockSession);
		
		verify(mockPlaceService, times(1)).ifPlaceValid(place);
	}
	
	@Test
	void test_process_create_place_return_error_placealreadyexists_if_place_validation_returns_negative1() {
		when(mockPlaceService.ifPlaceValid(place)).thenReturn(-1);
		
		String result = placeController.processPostCreatePlace(place, mockModel, mockSession);
		
		assertEquals(result, placeController.REDIRECT_ERROR_PLACE_ALREADY_EXISTS_JSP );
	}
	
	@Test
	void test_process_create_place_return_error_emptyPlacefields_if_place_validation_returns_negative2() {
		when(mockPlaceService.ifPlaceValid(place)).thenReturn(-2);
		
		String result = placeController.processPostCreatePlace(place, mockModel, mockSession);
		
		assertEquals(result, placeController.REDIRECT_ERROR_PLACE_EMPTY_FIELD_JSP );
	}
	
	@Test
	void test_process_create_place_save_created_place_return_confirm_jsp_if_place_validation_returns_1() {
		when(mockPlaceService.ifPlaceValid(place)).thenReturn(1);
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		String result = placeController.processPostCreatePlace(place, mockModel, mockSession);
		
		
		assertEquals(place.getCreator(), user);
		assertEquals(place.getSavedUsers(), new ArrayList<User>());
		verify(mockPlaceService, times(1)).save(place);
		assertEquals(result, placeController.CREATE_PLACE_CONFIRM_JSP );
	}
	
	@Test
	void test_get_error_placeAlreadyExists_will_call_model_add_attribute_and_return_createplace_jsp() {
		String result = placeController.getPlaceAlreadyExists(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(placeController.ERROR_MESSAGE_ATTR, placeController.ERROR_PLACE_EXISTS_MSG);
		verify(mockModel, times(1)).addAttribute(placeController.BIND_PLACE_ATTR, place);
		assertEquals(result, placeController.CREATE_PLACE_JSP);
		
	}
	
	@Test
	void test_get_error_placeEmptyFields_will_call_model_add_attribute_and_return_create_place_jsp() {
		String result = placeController.getPlaceEmptyFields(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(placeController.ERROR_MESSAGE_ATTR, placeController.ERROR_EMPTY_FIELD_MSG);
		verify(mockModel, times(1)).addAttribute(placeController.BIND_PLACE_ATTR, place);
		assertEquals(result, placeController.CREATE_PLACE_JSP);
	}
	
	@Test
	void test_get_places_by_currentuser_return_redirect_not_loggedinyet_error_if_no_user_insession() {
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.getPlacesByCurrentUser(mockModel, mockSession);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET );
	}
	
	@Test
	void test_get_places_by_currentuser_return_show_places_by_creator_jsp_and_set_atttribute_to_session() {
		List<Place> places = new ArrayList<Place>();
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findPlacesByCreator(user)).thenReturn(places);
		
		
		String result = placeController.getPlacesByCurrentUser(mockModel, mockSession);
		
		verify(mockSession, times(1)).setAttribute(placeController.CREATED_PLACES_ATTR, places);
		
		assertEquals(result, placeController.SHOW_PLACES_BY_CREATOR_JSP);
	}
	
	
	@Test
	void test_get_allPlaces_will_return_error_not_loggedin_yet_if_no_user_in_session() {
		
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.getAllPlaces(mockModel, mockSession);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET );
	}
	
	@Test
	void test_get_allplaces_return_showAllPlaces_jsp_and_session_set_allPlaces_attr_from_places_foundBy_placeServices() {
		List<Place> places = new ArrayList<Place>();
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findAll()).thenReturn(places);
		
		String result = placeController.getAllPlaces(mockModel, mockSession);
		
		verify(mockPlaceService, times(1)).findAll();
		verify(mockSession, times(1)).setAttribute(placeController.ALL_PLACES_ATTR, places);
		
		assertEquals(result, placeController.SHOW_ALL_PLACES_JSP);
	}
	
	@Test
	void test_show_updatePlace_Page_will_return_error_not_loggedin_when_no_user_in_session() {
		int placeId = 1;
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.showUpdatePlacePage(mockModel, mockSession, placeId);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET );
	}
	
	@Test
	void test_show_updatePlace_Page_will_return_updatePlace_jsp_and_add_attr_to_session_when_user_logged_in() {
		int placeId = 1;
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findPlaceById(placeId)).thenReturn(place);
		
		String result = placeController.showUpdatePlacePage(mockModel, mockSession, placeId);
		
		verify(mockSession, times(1)).setAttribute(placeController.CURRENT_PLACE_ATTR, place);
		verify(mockModel, times(1)).addAttribute(placeController.BIND_PLACE_ATTR, place);
		assertEquals(result, placeController.UPDATE_PLACE_JSP );
	}
	
	@Test
	void test_process_update_place_will_call_placeService_to_see_if_placeExists_when_returnedPlace_has_differentname() {
		String oldname = "test";
		Place p1 = new Place();
		p1.setName(oldname);
		place.setName("newtest");
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when((Place) mockSession.getAttribute(placeController.CURRENT_PLACE_ATTR)).thenReturn(p1);
		
		placeController.postProcessUpdatePlace(place, mockModel, mockSession);
		
		verify(mockPlaceService, times(1)).ifPlaceExists(place.getName());
	}
	
	@Test
	void test_process_update_place_will_return_errorPlaceNameExists_if_placeService_check_name_exists() {
		String oldname = "test";
		Place p1 = new Place();
		p1.setName(oldname);
		place.setName("newtest");
//		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when((Place) mockSession.getAttribute(placeController.CURRENT_PLACE_ATTR)).thenReturn(p1);
		when(mockPlaceService.ifPlaceExists(place.getName())).thenReturn(true);
		
		
		String result = placeController.postProcessUpdatePlace(place, mockModel, mockSession);
		
		verify(mockSession, times(1)).getAttribute(placeController.CURRENT_PLACE_ATTR);
		assertEquals(result, placeController.REDIRECT_ERROR_PLACE_NAME_EXIXTS_JSP);
	}
	
	@Test
	void test_process_update_place_will_return_show_places_by_creator_jsp_after_success() {
		String oldname = "test";
		Place p1 = new Place();
		int userId = 1;
		List<Place> places = new ArrayList<Place>();
		user.setId(userId);
		p1.setName(oldname);
		p1.setCreator(user);
		p1.setReviews(new ArrayList<Review>());
		p1.setSavedUsers(new ArrayList<User>());
		place.setName("newtest");
		when((User) mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
	
		when((Place) mockSession.getAttribute(placeController.CURRENT_PLACE_ATTR)).thenReturn(p1);
		when(mockPlaceService.ifPlaceExists(place.getName())).thenReturn(false);
		when(mockUserService.find(userId)).thenReturn(user);
		when(mockPlaceService.findPlacesByCreator(user)).thenReturn(places);
		
		String result = placeController.postProcessUpdatePlace(place, mockModel, mockSession);
		
		verify(mockSession, times(2)).getAttribute(placeController.CURRENT_PLACE_ATTR);
		assertEquals(place.getBiases(), p1.getBiases());
		assertEquals(place.getCreator(),p1.getCreator());
		assertEquals(place.getReviews(), p1.getReviews());
		assertEquals(place.getSavedUsers(), p1.getSavedUsers());
		verify(mockPlaceService, times(1)).save(place);
		
		verify(mockSession,times(1)).getAttribute(placeController.CURRENT_USER_ATTR);
		verify(mockUserService, times(1)).find(user.getId());
		verify(mockSession, times(1)).removeAttribute(placeController.CURRENT_USER_ATTR);
		verify(mockSession, times(1)).setAttribute(placeController.CURRENT_USER_ATTR, user);
		verify(mockPlaceService, times(1)).findPlacesByCreator(user);
		verify(mockSession, times(1)).setAttribute(placeController.CREATED_PLACES_ATTR, places);
		assertEquals(result, placeController.SHOW_PLACES_BY_CREATOR_JSP);
	}
	
	@Test
	void test_getErrorPlaceNameExists_will_add_model_attr_and_return_update_place_jsp() {
		when((Place) mockSession.getAttribute(placeController.CURRENT_PLACE_ATTR)).thenReturn(place);
		
		String result = placeController.getErrorPlaceNameExists(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(placeController.ERROR_MESSAGE_ATTR, placeController.ERROR_PLACE_NAME_EXISTS_MSG);
		verify(mockSession, times(1)).getAttribute(placeController.CURRENT_PLACE_ATTR);
		verify(mockModel, times(1)).addAttribute(placeController.BIND_PLACE_ATTR, place);
		assertEquals(result, placeController.UPDATE_PLACE_JSP);
	}
	
	@Test
	void test_savePlace_will_return_error_notloggedin_when_no_user_in_session() {
		int placeId = 1;
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.getSaveAPlaceToUserFavoriteList(mockModel, mockSession, placeId);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);
	}
	
	@Test
	void test_savePlace_will_return_showAllPlaces_jsp_and_modify_session_attr_and_modify_favplace_list_if_user_loggedin() {
		int placeId = 1;
		int userId = 1;
		user.setId(userId);
		user.setFavoritePlaces(new ArrayList<Place>());
		place.setSavedUsers(new ArrayList<User>());
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findPlaceById(placeId)).thenReturn(place);
		when((User) mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockUserService.find(userId)).thenReturn(user);
		
		String result = placeController.getSaveAPlaceToUserFavoriteList(mockModel, mockSession, placeId);
		
		verify(mockPlaceService, times(1)).findPlaceById(placeId);
		verify(mockSession, times(2)).getAttribute(placeController.CURRENT_USER_ATTR);
		assertTrue(user.getFavoritePlaces().contains(place));
		verify(mockUserService, times(1)).save(user);
		assertTrue(place.getSavedUsers().contains(user));
		verify(mockPlaceService, times(1)).save(place);
		verify(mockSession, times(1)).removeAttribute(placeController.CURRENT_USER_ATTR);
		assertEquals(result, placeController.SHOW_ALL_PLACES_JSP);
		
	}
	
	@Test
	void test_show_fav_places_will_return_error_not_logged_in_if_no_user_in_session() {
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.getFavoritePlaces(mockModel, mockSession);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);

	}
	
	@Test
	void test_show_fav_places_will_return_showFavoritePlaces_jsp_and_set_session_attr_for_loggedin_user() {
		List<Place> places = new ArrayList<Place>();
		user.setFavoritePlaces(places);
		when((User) mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = placeController.getFavoritePlaces(mockModel, mockSession);
		
		verify(mockSession, times(2)).getAttribute(placeController.CURRENT_USER_ATTR);
		verify(mockSession, times(1)).setAttribute(placeController.FAVORITE_PLACES_ATTR, places);
		assertEquals(result, placeController.SHOW_FAVORITE_PLACES_JSP);
	}
	
	@Test
	void test_delete_fav_places_return_not_loggedinError_if_no_user_in_session() {
		int placeId = 1;
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.deleteFavoritePlace(mockModel, mockSession, placeId);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);

	}
	
	@Test
	void test_delete_fav_place_will_modify_fav_place_list_in_user_and_saved_user_list_in_place_and_return_show_fav_place_jsp_for_logged_in_user() {
		int placeId = 1;
		int userId = 1;
		user.setFavoritePlaces(new ArrayList<Place>());
		place.setSavedUsers(new ArrayList<User>());
		user.favoriteAPlace(place);
		place.addUser(user);
		user.setId(userId);
		when((User) mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findPlaceById(placeId)).thenReturn(place);
		when(mockUserService.find(userId)).thenReturn(user);
		
		String result = placeController.deleteFavoritePlace(mockModel, mockSession, placeId);
		
		verify(mockSession, times(2)).getAttribute(placeController.CURRENT_USER_ATTR);
		verify(mockPlaceService, times(1)).findPlaceById(placeId);
		assertFalse(user.getFavoritePlaces().contains(place));
		assertFalse(place.getSavedUsers().contains(user));
		verify(mockPlaceService, times(1)).save(place);
		verify(mockUserService, times(1)).save(user);
		
		verify(mockSession, times(1)).removeAttribute(placeController.CURRENT_USER_ATTR);
		verify(mockUserService, times(1)).find(userId);
		verify(mockSession, times(1)).setAttribute(placeController.CURRENT_USER_ATTR, user);
		verify(mockSession, times(1)).removeAttribute(placeController.FAVORITE_PLACES_ATTR);
		verify(mockSession, times(1)).setAttribute(placeController.FAVORITE_PLACES_ATTR, user.getFavoritePlaces());
		
	}
	
	@Test
	void test_save_search_place_return_error_not_loggedin_if_no_user_in_session() {
		int placeId = 1;
		when(mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = placeController.getSaveAPlaceToUserFavoriteListFromSearch(mockModel, mockSession, placeId);
		
		assertEquals(result, placeController.REDIRECT_ERROR_NOT_LOGGED_IN_YET);

	}
	
	@Test
	void test_save_search_place_return_searchPlace_jsp_when_user_logged_in() {
		int placeId = 1;
		int userId = 1;
		user.setId(userId);
		user.setFavoritePlaces(new ArrayList<Place>());
		place.setSavedUsers(new ArrayList<User>());
		when((User) mockSession.getAttribute(placeController.CURRENT_USER_ATTR)).thenReturn(user);
		when(mockPlaceService.findPlaceById(placeId)).thenReturn(place);
		when(mockUserService.find(userId)).thenReturn(user);
		
		String result = placeController.getSaveAPlaceToUserFavoriteListFromSearch(mockModel, mockSession, placeId);
		
		verify(mockSession, times(2)).getAttribute(placeController.CURRENT_USER_ATTR);
		verify(mockPlaceService, times(1)).findPlaceById(placeId);
		assertTrue(user.getFavoritePlaces().contains(place));
		verify(mockUserService, times(1)).save(user);
		assertTrue(place.getSavedUsers().contains(user));
		verify(mockPlaceService, times(1)).save(place);
		verify(mockSession, times(1)).removeAttribute(placeController.CURRENT_USER_ATTR);
		verify(mockUserService, times(1)).find(userId);
		verify(mockSession, times(1)).setAttribute(placeController.CURRENT_USER_ATTR, user);
		assertEquals(result, placeController.SEARCH_PLACE_JSP);
	}
	


}

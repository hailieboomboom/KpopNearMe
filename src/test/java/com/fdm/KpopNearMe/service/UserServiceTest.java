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

import com.fdm.KpopNearMe.dal.UserRepository;
import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.Review;
import com.fdm.KpopNearMe.model.User;

/**
 * Test all methods from UserService class
 * @author hailieboomboom
 *
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	private UserService userService;
	private User user;
	private Optional<User> foundOptionalUser;
	private String username;
	private String password;
	
	@Mock
	UserRepository mockUserRepo;
	
	@Mock
	PlaceService mockPlaceService;
	
	@Mock
	ReviewService mockReviewService;

	@BeforeEach
	void setup() {
		userService = new UserService(mockUserRepo, mockReviewService, mockPlaceService);
		user = new User();
		username = "test";
		password = "test";
	}
	
	@Test
	void test_find_user_by_id_return_null_if_no_userfound() {
		int id = 1;
		foundOptionalUser = Optional.empty();
		when(mockUserRepo.findById(id)).thenReturn(foundOptionalUser);
		
		User founduser = userService.find(id);
		
		verify(mockUserRepo, times(1)).findById(id);
		assertNull(founduser);
	}
	
	@Test
	void test_find_user_by_id_return_a_user_if_a_user_is_found() {
		int id = 1;
		foundOptionalUser = Optional.of(user);
		when(mockUserRepo.findById(id)).thenReturn(foundOptionalUser);
		
		User foundUser = userService.find(id);
		
		verify(mockUserRepo, times(1)).findById(id);
		assertEquals(foundUser, user);
	}
	
	@Test
	void test_save_user_will_call_userrepo_save() {
		User foundUser = userService.save(user);
		
		verify(mockUserRepo, times(1)).save(user);
	}
	
	@Test
	void test_find_by_name_call_userrepo_findByUserName_and_return_null_if_nothing_is_found() {
	
		foundOptionalUser = Optional.empty();
		when(mockUserRepo.findByUsername(username)).thenReturn(foundOptionalUser);
		
		User foundUser = userService.findByName(username);
		
		verify(mockUserRepo, times(1)).findByUsername(username);
		assertNull(foundUser);
	}
	
	@Test
	void test_find_by_name_call_userrepo_findByUserName_and_return_a_user_if_a_user_is_found() {
		
		foundOptionalUser = Optional.of(user);
		when(mockUserRepo.findByUsername(username)).thenReturn(foundOptionalUser);
		
		User foundUser = userService.findByName(username);
		
		verify(mockUserRepo, times(1)).findByUsername(username);
		assertEquals(foundUser, user);
	}
	
	@Test
	void test_validate_user_call_userRepo_findbyusername() {
		
		int result = userService.validateUser(username, password);
		
		verify(mockUserRepo, times(1)).findByUsername(username);
	}
	
	@Test
	void test_validate_user_return_constant_nouserfound_if_founduser_from_userrepo_is_empty() {

		foundOptionalUser = Optional.empty();
		when(mockUserRepo.findByUsername(username)).thenReturn(foundOptionalUser);
		
		int result = userService.validateUser(username, password);
		
		assertEquals(result, userService.NO_USER_FOUND);
	}
	
	@Test
	void test_validate_user_return_constant_wrongpassword_if_password_is_wrong() {

		foundOptionalUser = Optional.of(user);
		when(mockUserRepo.findByUsername(username)).thenReturn(foundOptionalUser);
		user.setPassword("nottest");
		
		int result = userService.validateUser(username, password);
		
		assertEquals(result, userService.WRONG_PASSWORD);
	}
	
	@Test
	void test_validate_user_return_constant_validuser_if_username_and_password_matches() {
	
		foundOptionalUser = Optional.of(user);
		when(mockUserRepo.findByUsername(username)).thenReturn(foundOptionalUser);
		user.setPassword(password);
		
		int result = userService.validateUser(username, password);
		
		assertEquals(result, userService.VALID_USER);
	}
	
	@Test
	void test_ifUserExists_return_false_if_userRepo_return_empty() {
		foundOptionalUser = Optional.empty();
		when(mockUserRepo.findByUsername(username)).thenReturn(foundOptionalUser);
		
		boolean result = userService.ifUserExists(username);
		
		assertFalse(result);
	}

	@Test
	void test_ifUserExists_return_true_if_userRepo_return_a_user() {
		foundOptionalUser = Optional.of(user);
		when(mockUserRepo.findByUsername(username)).thenReturn(foundOptionalUser);
		
		boolean result = userService.ifUserExists(username);
		
		assertTrue(result);
	}
	
	@Test
	void test_removeuser_call_userRepo_delete() {
		userService.removeUser(user);
		
		verify(mockUserRepo, times(1)).delete(user);
	}
	
	@Test
	void test_removeuser_update_deleteduser_created_places_and_reviews_and_remove_from_user() {
		String deletedUsername = "deleted";
		User deleteduser = new User();
		deleteduser.setUsername(deletedUsername);
		deleteduser.setfName(deletedUsername);
		deleteduser.setlName(deletedUsername);
		deleteduser.setEmail(deletedUsername);
		deleteduser.setPassword(deletedUsername);
		Review review = new Review();
		List<Review> reviews = new ArrayList<Review>();
		reviews.add(review);
		user.setCreatedReviews(reviews);
		List<Place> places = new ArrayList<Place>();
		Place place = new Place();
		place.setCreator(user);
		places.add(place);
		user.setFavoritePlaces(places);
		user.setCreatedPlaces(places);
		when(mockUserRepo.findByUsername(deletedUsername)).thenReturn(Optional.of(deleteduser));
		when(mockReviewService.findByUser(user)).thenReturn(reviews);
		when(mockPlaceService.findPlacesByCreator(user)).thenReturn(places);
		when(mockUserRepo.save(user)).thenReturn(user);
		when(mockUserRepo.save(deleteduser)).thenReturn(deleteduser);
		
		
		userService.removeUser(user);
		
		assertEquals(place.getCreator(), deleteduser);
		assertEquals(review.getUser(), deleteduser);
	
	    assertTrue(deleteduser.getCreatedReviews().contains(review));
		assertFalse(place.getSavedUsers().contains(user));
		verify(mockPlaceService, times(2)).save(place);
		assertEquals(user.getFavoritePlaces().size(), 0);
		assertEquals(user.getCreatedPlaces().size(), 0);
		assertEquals(user.getCreatedReviews().size(), 0);
		
	}

}

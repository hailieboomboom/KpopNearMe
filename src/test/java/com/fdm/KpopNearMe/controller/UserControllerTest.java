package com.fdm.KpopNearMe.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

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
import com.fdm.KpopNearMe.service.UserService;

/**
 * Test all methods from UserController class
 * @author Hailie Long
 *
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	private UserController userController;
	private User user;
	
	@Mock
	HttpSession mockSession;
	
	@Mock
	UserService mockUserService;
	
	
	@Mock
	Model mockModel;
	
	@BeforeEach
	public void setup() {
		userController = new UserController(mockUserService);
		user = new User();
	}
	
	// test user registration: 
	// get user registration page if no user in session
	@Test
	void test_session_getattribute_called_when_getting_user_registration_page() {
		
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getRegisterUserPage(mockModel, mockSession);
		
		verify(mockSession, times(1)).getAttribute(userController.CURRENT_USER_ATTR);
	}
	
	@Test
	void test_model_add_attribute_called_when_getting_user_registration_page() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = userController.getRegisterUserPage(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(userController.BIND_USER_ATTR, user);
	}
	
	@Test
	void test_register_user_jsp_returned_when__guest_getting_user_registration_page() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = userController.getRegisterUserPage(mockModel, mockSession);
		
		assertEquals(result, userController.REGISTER_USER_JSP);
	}
	
	@Test
	void test_error_logged_in_already_returned_when__guest_getting_user_registration_page() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getRegisterUserPage(mockModel, mockSession);
		
		assertEquals(result, userController.REDIRECT_ERROR_LOGGED_IN_ALREADY_JSP);
	}
	
	

	// test process UserRegistration
	// test if existing username will show error message
	@Test
	void test_processuser_existing_username_will_show_error_existing_username() {
		user.setUsername("test");
		when(mockUserService.ifUserExists(user.getUsername())).thenReturn(true);
		
		String result = userController.postProcessUser(user, mockModel, mockSession);
		
		assertEquals(result, userController.REDIRECT_ERROR_EXISTING_USERNAME_JSP);
		
	}
	// test user can register successfully (model add attribute/session add attribute, userservice save)
	@Test
	void test_processuser_new_username_will_let_model_add_current_user_attribute() {
		user.setUsername("test");
		when(mockUserService.ifUserExists(user.getUsername())).thenReturn(false);
		
		String result = userController.postProcessUser(user, mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(userController.CURRENT_USER_ATTR, user);
	}
	
	@Test
	void test_processuser_new_username_will_let_session_set_current_user_attribute() {
		user.setUsername("test");
		when(mockUserService.ifUserExists(user.getUsername())).thenReturn(false);
		
		String result = userController.postProcessUser(user, mockModel, mockSession);
		
		verify(mockSession, times(1)).setAttribute(userController.CURRENT_USER_ATTR, user);
	}
	
	@Test
	void test_processuser_new_username_will_let_userService_save_newuser() {
		user.setUsername("test");
		when(mockUserService.ifUserExists(user.getUsername())).thenReturn(false);
		
		String result = userController.postProcessUser(user, mockModel, mockSession);
		
		verify(mockUserService, times(1)).save(user);
	}
	
	@Test
	void test_processuser_new_username_will_return_register_confirm_jsp() {
		user.setUsername("test");
		when(mockUserService.ifUserExists(user.getUsername())).thenReturn(false);
		
		String result = userController.postProcessUser(user, mockModel, mockSession);
		
		assertEquals(result, userController.REGISTER_CONFIRM_JSP);
	}
	
	@Test
	void test_error_existing_username_will_let_model_add_attrbute_errormessage_and_binduser() {
		String result = userController.getExistingUserMessage(mockModel);
		
		verify(mockModel, times(1)).addAttribute(userController.ERROR_MESSAGE_ATTR, userController.USERNAME_ALREADY_EXISTS_PLEASE_RE_ENTER_ONE_MSG);
		verify(mockModel, times(1)).addAttribute(userController.BIND_USER_ATTR, user);
	}
	// test user login:
	// a logged in person cannot access login page
	@Test
	void test_logged_in_user_cannot_access_login_jsp() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getUserLoginPage(mockSession);
		
		assertEquals(result, userController.REDIRECT_ERROR_LOGGED_IN_ALREADY_JSP);
	}
	// not logged in person can access login page
	@Test
	void test_not_logged_in_user_can_access_login_jsp() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = userController.getUserLoginPage(mockSession);
		
		assertEquals(result, userController.LOGIN_JSP);
	}
	// test process login: based on validation result (-1/0/1)
	@Test
	void test_processLogin_not_existing_username_will_fail_login(){
		String reqUsername = "test";
		String reqPassword = "test";
		
		when(mockUserService.validateUser(reqUsername, reqPassword)).thenReturn(-1);
		
		String result = userController.postLoginUser(reqUsername, reqPassword, mockSession);
		
		assertEquals(result, userController.REDIRECT_ERROR_NOT_EXISTING_USERNAME_JSP);
	}
	
	@Test
	void test_processLogin_wrong_password_will_fail_login(){
		String reqUsername = "test";
		String reqPassword = "test";
		
		when(mockUserService.validateUser(reqUsername, reqPassword)).thenReturn(0);
		
		String result = userController.postLoginUser(reqUsername, reqPassword, mockSession);
		
		assertEquals(result, userController.REDIRECT_ERROR_WRONG_PASSWORD_JSP);
	}
	
	@Test
	void test_processLogin_successful_login_will_call_userService_tofind_user(){
		String reqUsername = "test";
		String reqPassword = "test";
		
		when(mockUserService.validateUser(reqUsername, reqPassword)).thenReturn(1);
		
		String result = userController.postLoginUser(reqUsername, reqPassword, mockSession);
		
		verify(mockUserService, times(1)).findByName(reqUsername);
	}
	
	@Test
	void test_processLogin_successful_login_will_let_session_set_currentuser_attribute(){
		String reqUsername = "test";
		String reqPassword = "test";
		
		when(mockUserService.validateUser(reqUsername, reqPassword)).thenReturn(1);
		when(mockUserService.findByName(reqUsername)).thenReturn(user);
		
		String result = userController.postLoginUser(reqUsername, reqPassword, mockSession);
		
		verify(mockSession, times(1)).setAttribute(userController.CURRENT_USER_ATTR, user);;
	}
	
	@Test
	void test_processLogin_successful_login_will_return_displayUserpage(){
		String reqUsername = "test";
		String reqPassword = "test";
		
		when(mockUserService.validateUser(reqUsername, reqPassword)).thenReturn(1);
		
		String result = userController.postLoginUser(reqUsername, reqPassword, mockSession);
		
		assertEquals(result, userController.DISPLAY_USER_JSP);
	}
	
	@Test
	void test_error_not_existing_username_will_let_mockmodel_add_error_msg() {
		
		String result = userController.getNoUserNameMessage(mockModel);
		
		verify(mockModel, times(1)).addAttribute(userController.ERROR_MESSAGE_ATTR, userController.NOT_EXISTING_USERNAME_IN_DB_MSG);
		
	}
	
	@Test
	void test_error_not_existing_username_will_return_login_jsp() {
		
		String result = userController.getNoUserNameMessage(mockModel);
		
		assertEquals(result, userController.LOGIN_JSP);
		
	}
	
	@Test
	void test_error_wrong_password_will_let_mockmodel_add_error_msg() {
		
		String result = userController.getWrongPasswordMessage(mockModel);
		
		verify(mockModel, times(1)).addAttribute(userController.ERROR_MESSAGE_ATTR, userController.WRONG_PASSWORD_MSG);
		
	}
	
	@Test
	void test_error_wrpng_password_will_return_login_jsp() {
		
		String result = userController.getWrongPasswordMessage(mockModel);
		
		assertEquals(result, userController.LOGIN_JSP);
		
	}
	
	// test display user page
	// a not-logged in user cannot access displayuser page
	@Test
	void test_displayuser_a_guest_cannot_access() {
		
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = userController.getDisplayUserPage(mockSession);
		
		assertEquals(result, userController.REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP);
	}
	
	// a logged in user can access the displayuser page
	@Test
	void test_displayuser_a_logged_in_user_access() {
		
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getDisplayUserPage(mockSession);
		
		assertEquals(result, userController.DISPLAY_USER_JSP);
	}
	
	// test logout page
	
	// testa not logged in user cannot access page
	@Test
	void a_guest_cannot_access_logout_page() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = userController.getUserLogout(mockSession);
		
		assertEquals(result, userController.REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP);
	}
	// test session is invalidated 
	@Test
	void test_logout_will_let_session_invalidate() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getUserLogout(mockSession);
		
		verify(mockSession, times(1)).invalidate();
	}
	
	@Test
	void test_logout_will_let_user_return_to_index_page() {
		
        when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getUserLogout(mockSession);
		
		assertEquals(result, userController.INDEX_JSP);
	}
	
	// test updateUser:
	
	@Test 
	void test_process_update_user_will_return_error_redirect_existing_username_error_if_userService_check_username_exixts_in_db() {
		String oldname = "test";
		User olduser = new User();
		olduser.setUsername(oldname);
		user.setUsername("nottest");
		when( (User) mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(olduser);
		
		when(mockUserService.ifUserExists(user.getUsername())).thenReturn(true);
		
		String result = userController.processUpdateUserPage(user, mockModel, mockSession);
		
		verify(mockSession, times(2)).getAttribute(userController.CURRENT_USER_ATTR);
		verify(mockUserService, times(1)).ifUserExists(user.getUsername());
		assertEquals(result,userController.REDIRECT_ERROR_EXISTING_USERNAME_UPDATE_JSP);
	}
	
	@Test 
	void test_process_update_user_will_return_displayUser_jsp_if_userService_check_username_does_not_exixts_in_db() {
		String oldname = "test";
		User olduser = new User();
		olduser.setUsername(oldname);
		olduser.setCreatedPlaces(new ArrayList<Place>());
		olduser.setCreatedReviews(new ArrayList<Review>());
		olduser.setFavoritePlaces(new ArrayList<Place>());
		user.setUsername("nottest");
		when( (User) mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(olduser);
		
		when(mockUserService.ifUserExists(user.getUsername())).thenReturn(false);
		
		String result = userController.processUpdateUserPage(user, mockModel, mockSession);
		
		verify(mockSession, times(2)).getAttribute(userController.CURRENT_USER_ATTR);
		verify(mockUserService, times(1)).ifUserExists(user.getUsername());
		assertEquals(user.getCreatedPlaces(), olduser.getCreatedPlaces());
		assertEquals(user.getFavoritePlaces(), olduser.getFavoritePlaces());
		assertEquals(user.getCreatedReviews(), olduser.getCreatedReviews());
		verify(mockUserService, times(1)).save(user);
		verify(mockModel, times(1)).addAttribute(userController.CURRENT_USER_ATTR, user);
		verify(mockSession, times(1)).removeAttribute(userController.CURRENT_USER_ATTR);
		verify(mockSession, times(1)).setAttribute(userController.CURRENT_USER_ATTR, user);
		assertEquals(result,userController.DISPLAY_USER_JSP);
	}
	
	
	
	@Test
	void test_error_update_user_with_existing_username_will_add_errormsg_to_model() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getExistingUserMessageUpdate(mockModel, mockSession);
		
		verify(mockModel, times(1)).addAttribute(userController.ERROR_MESSAGE_ATTR, userController.USERNAME_ALREADY_EXISTS_PLEASE_RE_ENTER_ONE_MSG);
		verify(mockModel, times(1)).addAttribute(userController.BIND_USER_ATTR, user);
	}
	
	@Test
	void test_error_update_user_with_existing_username_will_call_session_to_get_currentuser() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getExistingUserMessageUpdate(mockModel, mockSession);
		
	    verify(mockSession, times(1)).getAttribute(userController.CURRENT_USER_ATTR);
	}

	@Test
	void test_error_update_user_will_return_update_user_page() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.getExistingUserMessageUpdate(mockModel, mockSession);
		
	    assertEquals(result, userController.UPDATE_USER_JSP);
	}
	
	
	// test deleteUser:
	
	// test a not logged in user cannot access the page
	@Test
	void test_deleteuser_cannot_be_accessed_by_guest() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(null);
		
		String result = userController.postDeleteUser(mockModel, mockSession);
		
	    assertEquals(result, userController.REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP);
	}

	// test session can get current user
	@Test
	void test_deleteuser_will_get_current_user_from_session() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.postDeleteUser(mockModel, mockSession);
		
	    verify(mockSession, times(2)).getAttribute(userController.CURRENT_USER_ATTR);
	}

	// test userservice has removed the user
	@Test
	void test_deleteuser_will_remove_user_from_db_via_userService() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.postDeleteUser(mockModel, mockSession);
		
	    verify(mockUserService, times(1)).removeUser(user);
	}

	// test session has invalidate
	@Test
	void test_deleteuser_will_invalidate_the_session() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.postDeleteUser(mockModel, mockSession);
		
	    verify(mockSession, times(1)).invalidate();
	}
	
	@Test
	void test_deleteuser_will_return_removeuser_page() {
		when(mockSession.getAttribute(userController.CURRENT_USER_ATTR)).thenReturn(user);
		
		String result = userController.postDeleteUser(mockModel, mockSession);
		
	    assertEquals(result, userController.REMOVE_USER_JSP);
	}

	@Test
	void test_error_user_not_logged_in_yet_will_add_error_msg_to_model() {
		String result = userController.getNoLoggedInMessage(mockModel);
		
		verify(mockModel, times(1)).addAttribute(userController.ERROR_MESSAGE_ATTR, userController.USER_NOT_LOGGED_IN_MSG);
	}
	
	@Test
	void test_error_user_not_logged_in_yet_will_return_index_page() {
		String result = userController.getNoLoggedInMessage(mockModel);
		
		assertEquals(result, userController.INDEX_JSP);
	}
	
	@Test
	void test_error_user_logged_in_already_will_add_error_msg_to_model() {
		String result = userController.getLoggedInMessage(mockModel);
		
		verify(mockModel, times(1)).addAttribute(userController.ERROR_MESSAGE_ATTR, userController.USER_LOGGED_IN_ALREADY_MSG);
	}
	
	@Test
	void test_error_user_logged_in_already_will_return_index_page() {
		String result = userController.getLoggedInMessage(mockModel);
		
		assertEquals(result, userController.INDEX_JSP);
	}
	


}

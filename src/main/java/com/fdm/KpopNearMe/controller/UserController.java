package com.fdm.KpopNearMe.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdm.KpopNearMe.model.Place;
import com.fdm.KpopNearMe.model.User;
import com.fdm.KpopNearMe.service.UserService;

/**
 * 
 * UserController: is responsible for communicating between views (user interface) and user services/models.
 * the controller is responsible for following requests:
 * 		user registration
 * 		user login
 * 		user check user details
 * 		user update user details
 * 		user delete his own account
 * 		user logout
 *
 * @author hailieboomboom
 *
 */
@Controller
public class UserController {
  
    final String USER_DETAILS_HAS_BEEN_UPDATED_SUCCESSFULLY_MSG = "User details has been updated successfully";
	final String UPDATE_MESSAGE_ATTR = "updateMessage";
	final String USER_LOGGED_IN_ALREADY_MSG = "User logged in already, please re-attempt again";
	final String USER_NOT_LOGGED_IN_MSG = "User not logged in yet, please log in first";
	final String REMOVE_USER_JSP = "removeUser";
	final String UPDATE_USER_JSP = "updateUser";
	final String REDIRECT_ERROR_EXISTING_USERNAME_UPDATE_JSP = "redirect:/errorExistingUsernameUpdate";
	final String INDEX_JSP = "index";
	final String REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP = "redirect:/errorNotLoggedInYet";
	final String WRONG_PASSWORD_MSG = "Wrong password, please attempt again";
	final String NOT_EXISTING_USERNAME_IN_DB_MSG = "Could not find your user details in database, please try again";
	final String DISPLAY_USER_JSP = "checkUser";
	final String REDIRECT_ERROR_WRONG_PASSWORD_JSP = "redirect:/errorWrongPassword";
	final String REDIRECT_ERROR_NOT_EXISTING_USERNAME_JSP = "redirect:/errorNotExistingUsername";
	final String LOGIN_JSP = "login";
	final String ERROR_MESSAGE_ATTR = "errorMessage";
	final String USERNAME_ALREADY_EXISTS_PLEASE_RE_ENTER_ONE_MSG = "Username already exists, please re-enter one";
	final String REGISTER_CONFIRM_JSP = "registerConfirm";
	final String REDIRECT_ERROR_EXISTING_USERNAME_JSP = "redirect:/errorExistingUsername";
	final String REDIRECT_ERROR_LOGGED_IN_ALREADY_JSP = "redirect:/errorLoggedInAlready";
	final String REGISTER_USER_JSP = "registerUser";
	final String BIND_USER_ATTR = "bindUser";
	final String CURRENT_USER_ATTR = "currentUser";
	
	private Log log = LogFactory.getLog(UserController.class);
	private UserService userService;
	
	
	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	/**
	 * when a guest tries to get registerUser page.
	 * a logged in user cannot access the page
	 * @param model
	 * @param session the httpsession a user is in
	 * @return user registration form for a new guest
	 */
	@GetMapping("/registerUser")
	public String getRegisterUserPage(Model model, HttpSession session) {
		if(session.getAttribute(CURRENT_USER_ATTR)!=null) {
			return REDIRECT_ERROR_LOGGED_IN_ALREADY_JSP;
		}
		User userObject = new User();
		model.addAttribute(BIND_USER_ATTR,userObject);
		return REGISTER_USER_JSP;
	}

	/**
	 * when a user submit user registration form, the controller process the post
	 * request and lead the user to registration confirmation page if successfully registered.
	 * @param returnedSpringUser the user object submitted via spring form from user registration page
	 * @param model
	 * @param session
	 * @return registerConfirm page if completed
	 */
	@PostMapping("/processUser")
	public String postProcessUser(User returnedSpringUser, Model model, HttpSession session) {
		
		log.info("Registered User: "+returnedSpringUser.toString());
		// a user needs to register with a unique username.
		// registration with existing username will not succeed.
		boolean ifUserExists = userService.ifUserExists(returnedSpringUser.getUsername());
		if(ifUserExists) {
			return REDIRECT_ERROR_EXISTING_USERNAME_JSP;
		}
		returnedSpringUser.setFavoritePlaces(new ArrayList<Place>());
		
		model.addAttribute(CURRENT_USER_ATTR,returnedSpringUser);
		session.setAttribute(CURRENT_USER_ATTR, returnedSpringUser);
		userService.save(returnedSpringUser);
		return REGISTER_CONFIRM_JSP;
	}
	
	/**
	 * when getting existing username error while having user registration, 
	 * an error message will show up, and redirect the user to registerUser page
	 * @param model
	 * @return registerUser page
	 */
	@GetMapping("/errorExistingUsername")
	public String getExistingUserMessage(Model model) {
		model.addAttribute(ERROR_MESSAGE_ATTR, USERNAME_ALREADY_EXISTS_PLEASE_RE_ENTER_ONE_MSG);
		User userObject = new User();
		model.addAttribute(BIND_USER_ATTR,userObject);
		return REGISTER_USER_JSP;
	}

	/**
	 * user controller directs user to login page when receiving get request on login page.
	 * a logged in user cannot access the login page
	 * @param session
	 * @return login page containing login form
	 */
	@GetMapping("/login")
	public String getUserLoginPage(HttpSession session) {
		if(session.getAttribute(CURRENT_USER_ATTR)!=null) {
			return REDIRECT_ERROR_LOGGED_IN_ALREADY_JSP;
		}
		return LOGIN_JSP;
	}
	
	/**
	 * user controller receives post request when user submitted login form, validates the user and redirects the user 
	 * to checkUser page if login successful.
	 * @param username username string user input from the login form
	 * @param password password string user input from the login session
	 * @param session
	 * @return checkUser page that displays user details when sueccessfully login
	 */
	@PostMapping("/processLogin")
	public String postLoginUser(@RequestParam String username, @RequestParam String password, HttpSession session) {
         
		// controller call userService to validate the user
		int validationResult = userService.validateUser(username, password);
		// if no username is found from validation, show error message
		if(validationResult == -1) {
			return REDIRECT_ERROR_NOT_EXISTING_USERNAME_JSP;
		}
		// if wrong password is entered, show error message
		else if(validationResult == 0) {
			return REDIRECT_ERROR_WRONG_PASSWORD_JSP;
		}else {
			// when login successful, set current user as an attribute in the session,
			// and direct user to check user page with user details displayed
			User currentUser = userService.findByName(username);
			session.setAttribute(CURRENT_USER_ATTR, currentUser);
			log.info("user logged in successfully");
			return DISPLAY_USER_JSP;
		}
	}
	
	/**
	 * when input username does not exist in database, show the error, and
	 * redirect user to login page
	 * @param model
	 * @return login page
	 */
	@GetMapping("/errorNotExistingUsername")
	public String getNoUserNameMessage(Model model) {
		model.addAttribute(ERROR_MESSAGE_ATTR, NOT_EXISTING_USERNAME_IN_DB_MSG);
		return LOGIN_JSP;
	}
	
	/**
	 * when input password does not match with the database, show the error, and 
	 * redirect the user to login page
	 * @param model
	 * @return login page
	 */
	@GetMapping("/errorWrongPassword")
	public String getWrongPasswordMessage(Model model) {
		model.addAttribute(ERROR_MESSAGE_ATTR,WRONG_PASSWORD_MSG);
		return LOGIN_JSP;
	}

	/**
	 * user controller returns checkUser page that displaying a logged in user's details when receiving get request
	 * a user without successful login cannot access the page
	 * @param session
	 * @return checkUser page 
	 */
	@GetMapping("/displayUser")
	public String getDisplayUserPage(HttpSession session) {
		// a guest cannot access display user page
		if(session.getAttribute(CURRENT_USER_ATTR)==null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP;
		}
		return DISPLAY_USER_JSP;
	}
	
	/**
	 * user controller log the current user out when receiving a logout request, 
	 * and redirects the user back to home page after logging out sucessfully.
	 * a guest cannot access logout page.
	 * @param session
	 * @return index page
	 */
	@GetMapping("/logout")
	public String getUserLogout(HttpSession session) {
		// a guest cannot have a logout request
		if(session.getAttribute(CURRENT_USER_ATTR)==null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP;
		}
		session.invalidate();
		log.info("user has been logged out");
		return INDEX_JSP;
	}
	
	/**
	 * user controller receives updateUser get request from a logged in user, 
	 * read current user details into model, 
	 * and direct user to updateUser page with user details prefilled in the form.
	 * @param session
	 * @param model
	 * @return updateUser page
	 */
	@GetMapping("/updateUser")
	public String getUpdateUerPage(HttpSession session, Model model) {
		// a guest cannot have a updateuser request
		if(session.getAttribute(CURRENT_USER_ATTR)==null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP;
		}
		User userObject = (User) session.getAttribute(CURRENT_USER_ATTR);
		model.addAttribute(BIND_USER_ATTR,userObject);
		
		return UPDATE_USER_JSP;
	}
	
	/**
	 * user controller receives post processUpdateUser request from form on updateUser page, 
	 * update the user details based on form input, and redirects the user
	 * back to display user page with new user details upon a successful update.
	 * @param returnedSpringUser current logged in user
	 * @param model
	 * @param session
	 * @return checkUser page if update succeeds.
	 */
	@PostMapping("/processUpdateUser")
	public String processUpdateUserPage(User returnedSpringUser, Model model, HttpSession session) {
		boolean ifUserExists = false;
		// check if user has updated his username. 
		// if the user enters a new username, check if it is unique.
		// if not, show the error, and exit the update.
		User olduser = (User) session.getAttribute(CURRENT_USER_ATTR);
		String oldusername = ((User) session.getAttribute(CURRENT_USER_ATTR)).getUsername();
		if(returnedSpringUser.getUsername().equals(oldusername) == false) {
			ifUserExists = userService.ifUserExists(returnedSpringUser.getUsername());
		}
		if(ifUserExists) {
			return REDIRECT_ERROR_EXISTING_USERNAME_UPDATE_JSP;
		}
		log.debug("returned user is" +returnedSpringUser.toString());
		
		//copy favoritePlaces, createdPlaces, createdReviews
		returnedSpringUser.setCreatedPlaces(olduser.getCreatedPlaces());
		returnedSpringUser.setCreatedReviews(olduser.getCreatedReviews());
		returnedSpringUser.setFavoritePlaces(olduser.getFavoritePlaces());
		
		userService.save(returnedSpringUser);
		model.addAttribute(CURRENT_USER_ATTR,returnedSpringUser);
		session.removeAttribute(CURRENT_USER_ATTR);
		session.setAttribute(CURRENT_USER_ATTR, returnedSpringUser);
		log.info("user has been updated successfully: new details are "+ returnedSpringUser.toString());
		return DISPLAY_USER_JSP;
	}
	
	/**
	 * when updating user details, user controller will show error message if updated username exists in database already.
	 * the user will stay in updateUser page
	 * @param model
	 * @param session
	 * @return updateUser page
	 */
	@GetMapping("/errorExistingUsernameUpdate")
	public String getExistingUserMessageUpdate(Model model, HttpSession session) {
		model.addAttribute(ERROR_MESSAGE_ATTR, USERNAME_ALREADY_EXISTS_PLEASE_RE_ENTER_ONE_MSG);
		// refill current user details from session to model
		User userObject = (User) session.getAttribute(CURRENT_USER_ATTR);
		model.addAttribute(BIND_USER_ATTR,userObject);
		return UPDATE_USER_JSP;
	}
	
	/**
	 * when user controller receives a deleteUser request, it will ask user service to delete the user
	 * from the database, and log the user out by invalidating current user's session. 
	 * After deletion, user will be directed to removeUser page indicating successful account deletion.
	 * a guest cannot send this request.
	 * @param model
	 * @param session
	 * @return removeUser page 
	 */
	@GetMapping("/deleteUser")
	public String postDeleteUser(Model model, HttpSession session) {
		// a guest cannot submit delete user request
		if(session.getAttribute(CURRENT_USER_ATTR)==null) {
			return REDIRECT_ERROR_NOT_LOGGED_IN_YET_JSP;
		}
		User currentUser = (User) session.getAttribute(CURRENT_USER_ATTR);
		// remove current user from the database, and invalidate the session
		
		
		userService.removeUser(currentUser);
		session.invalidate();
		log.info("currrent user has been deleted");
		return REMOVE_USER_JSP;
		
	}
	
	/**
	 * when getting user_not_logged_in error, show the error message via model, and 
	 * redirect the user back to homepage.
	 * @param model
	 * @return index page
	 */
	@GetMapping("/errorNotLoggedInYet")
	public String getNoLoggedInMessage(Model model) {
		model.addAttribute(ERROR_MESSAGE_ATTR,USER_NOT_LOGGED_IN_MSG);
		return INDEX_JSP;
	}
	
	/**
	 * when getting error because user logged in already, show the error message on index page via model, 
	 * and redirect the user back to index page (homepage).
	 * @param model
	 * @return
	 */
	@GetMapping("/errorLoggedInAlready")
	public String getLoggedInMessage(Model model) {
		model.addAttribute(ERROR_MESSAGE_ATTR,USER_LOGGED_IN_ALREADY_MSG);
		return INDEX_JSP;
	}


}

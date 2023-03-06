package com.fdm.KpopNearMe.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.fdm.KpopNearMe.model.User;

/**
 * 
 * User Repository that is reponsible for communicating with database directly.
 * It extends from JPA Repository, acts as an interface, and utilised by User Service
 * @author hailieboomboom
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	/**
	 * find a user by username
	 * @param username
	 * @return a user as an Optional object
	 */
	Optional<User> findByUsername(String username);

}

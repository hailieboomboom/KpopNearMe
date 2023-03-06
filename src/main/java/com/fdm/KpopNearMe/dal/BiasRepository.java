package com.fdm.KpopNearMe.dal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.fdm.KpopNearMe.model.Bias;

/**
 * pre-constructed BiasRepository extended from JPARepository, 
 * and it is responsible for directly communicating with database,
 * acts as an interface, and utilised by BiasService
 * @author Hailie Long
 *
 */
@Repository
public interface BiasRepository extends JpaRepository<Bias, Integer>{
	
	Optional<Bias> findByName(String name);

}

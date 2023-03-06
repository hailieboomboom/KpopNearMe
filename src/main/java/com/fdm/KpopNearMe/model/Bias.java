package com.fdm.KpopNearMe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Bias model to be stored in database as an entity
 * getters,setters, hashcode, equals, and toString are auto-generated
 * @author Hailie Long
 *
 */
@Entity
public class Bias {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	
	@ManyToMany
	private List<Place> places;
	
	public Bias() {
		super();
		// TODO Auto-generated constructor stub
	}
  
	public Bias(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	@Override
	public String toString() {
		return "Bias [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bias other = (Bias) obj;
		return   Objects.equals(name, other.name);
	}
	
	/**
	 * add a place object to places list when a place add bias as one of its biases.
	 * the place will be added if it is not in the places list before
	 * @param place to be added
	 */
	public void addAPlace(Place place) {
		if(this.places == null) {
			this.places = new ArrayList<Place>();
		}
		if(places.contains(place) ==  false) {
			this.places.add(place);
		}
	}
	
	/**
	 * remove a place object from places list when deleting this bias from its biases.
	 * a place will be removed if it is in the places list before
	 * @param place
	 */
	public void removeAPlace(Place place) {
		if(places.contains(place)) {
			this.places.remove(place);
		}
	}
	
	

}

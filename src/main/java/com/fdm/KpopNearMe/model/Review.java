package com.fdm.KpopNearMe.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Review class to describe a review, act as an entity to be stored in the database
 * getters setters, toString, hashcode, and equals are auto-generated
 * @author hailieboomboom
 *
 */
@Entity
public class Review {
	@Id
	@GeneratedValue
	private int id;
	private String content;
	private double rating;
	
	@ManyToOne
	private Place place; // the place the review is about
	
	@ManyToOne
	private User user; // user writes the review

	/**
	 * no argument constructor
	 */
	public Review() {
		super();
		// TODO Auto-generated constructor stub
	}
 
	/**
	 * constructor with fields
	 * @param content review content
	 * @param rating rating given to the place in review
	 * @param place where the review is dedicated to
	 * @param user who writes the review
	 */
	public Review( String content, double rating, Place place, User user) {
		super();
		this.content = content;
		this.rating = rating;
		this.place = place;
		this.user = user;
	}
 
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", content=" + content + ", rating=" + rating + ", place=" + place + ", user="
				+ user + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, id, place, rating, user);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		return Objects.equals(content, other.content) && id == other.id && Objects.equals(place, other.place)
				&& Double.doubleToLongBits(rating) == Double.doubleToLongBits(other.rating)
				&& Objects.equals(user, other.user);
	}
	
	
	
	

}

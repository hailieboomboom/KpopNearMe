package com.fdm.KpopNearMe.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Place model to describe a place object, used to store as an antity in database
 * getters,setters, hashcode, equals, and toString are auto-generated
 * @author Hailie Long
 *
 */
@Entity
public class Place {
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String address;
	private int postcode;
	private double overallRating;
	private String img; //url of the image
	
	@ManyToOne
	private User creator; //creator of the place
	

	
	@ManyToMany(mappedBy="places")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Bias> biases;
	
	@OneToMany(mappedBy="place")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Review> reviews;
	
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<User> savedUsers; // list of users storing the place in their favoritePlaces list

	/**
	 * no argument constructor
	 */
	public Place() {
		super();
		// TODO Auto-generated constructor stub
	}
 
	/**
	 * constructor with fields
	 * @param name place name
	 * @param address place address
	 * @param postcode place postcode
	 * @param img image of the place
	 */
	public Place( String name, String address, int postcode, String img) {
		super();
		this.name = name;
		this.address = address;
		this.postcode = postcode;
		this.img = img;
		this.overallRating = 0;
		
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPostcode() {
		return postcode;
	}

	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	public double getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(double overallRating) {
		this.overallRating = overallRating;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public List<Bias> getBiases() {
		return biases;
	}

	public void setBiases(List<Bias> biases) {
		this.biases = biases;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<User> getSavedUsers() {
		return savedUsers;
	}

	public void setSavedUsers(List<User> savedUsers) {
		this.savedUsers = savedUsers;
	}
	
	

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Place [id=" + id + ", name=" + name + ", address=" + address + ", postcode=" + postcode+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, creator, id, name,  postcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		return Objects.equals(address, other.address) && Objects.equals(creator, other.creator) && id == other.id
				&& Objects.equals(name, other.name)
			
				&& postcode == other.postcode;
	}

	/**
	 * add a user to savedUser list when a user save the place to favoriteplaces list
	 * if a user is not in saveduser list already
	 * @param currentuser to be added
	 */
	public void addUser(User currentuser) {
		if(savedUsers == null) {
			savedUsers = new ArrayList<User>();
		}
		if(savedUsers.contains(currentuser) == false) {
			savedUsers.add(currentuser);
		}

	}
	
	/**
	 * remove a user from savedUser list when the user is already in the list
	 * @param currentuser to be removed
	 */
	public void removeSaveUser(User currentuser) {
		if(savedUsers == null) {
			savedUsers = new ArrayList<User>();
		}
		if(savedUsers.contains(currentuser)) {
			savedUsers.remove(currentuser);
		}
		
	}
	
	/**
	 * add a review into reviews list if the review is not in the reviews yet
	 * @param review to be added
	 */
	public void addAReview(Review review) {
		if(this.reviews == null) {
			this.reviews = new ArrayList<Review>();
		}
		if(reviews.contains(review)== false) {
			this.reviews.add(review);
		}		
	}
	
	/**
	 * remove a review from reviews list if it is in the list before
	 * @param review to be removed
	 */
	public void removeAReview(Review review) {
		if(this.reviews == null) {
			this.reviews = new ArrayList<Review>();
		}
		if(reviews.contains(review)) {
			this.reviews.remove(review);
		}
	}
	
	/**
	 * add a bias into biases list if not stored before
	 * @param bias to be added
	 */
	public void addABias(Bias bias) {
		if(this.biases == null) {
			this.biases = new ArrayList<Bias>();
		}
		if(biases.contains(bias)==false) {
			this.biases.add(bias);
		}
	}
	
	/**
	 * remove a bias from biases list if it is in the list previously
	 * @param bias
	 */
	public void removeABias(Bias bias) {
		if(this.biases == null) {
			this.biases = new ArrayList<Bias>();
		}
		if(biases.contains(bias)) {
			this.biases.remove(bias);
		}
	}
	
	
	
	
	
	

}

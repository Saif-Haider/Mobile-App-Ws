package com.haider.app.ws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.haider.app.ws.shared.dto.UserDto;
// strong address in Database

@Entity(name="address")
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = -1304874820143654520L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(length=30,nullable=false)
	private String addressId; 
	@Column(length=15,nullable=false)
	private String city;
	@Column(length=15,nullable=false)
	
	private String country;
	@Column(length=100,nullable=false)
	
	private String streetName;
	@Column(length=30,nullable=false)
	
	private String postalCode;
	@Column(length=10,nullable=false)
	
	private String type;
	
	// users_id because users is table name and id is uniquelly generated autoincremnetded
	@ManyToOne
	@JoinColumn(name="users_id")
	private UserEntity userDetails;  // name needs to be same in AddressEntity and AddressDto

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

}

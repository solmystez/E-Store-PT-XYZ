package com.demo.eStore.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long address_id;
	
	@NotBlank(message = "Address is required")
	private String addressLabel;

	private String addressDescription;
	private String addressPostalCode;
	private String addressCity;
	private String addressProvince;
	private String addressCountry;
	
	private String username;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User userAddress;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getAddress_id() {
		return address_id;
	}

	public void setAddress_id(Long address_id) {
		this.address_id = address_id;
	}

	public String getAddressLabel() {
		return addressLabel;
	}

	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}

	public String getAddressDescription() {
		return addressDescription;
	}

	public void setAddressDescription(String addressDescription) {
		this.addressDescription = addressDescription;
	}

	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressProvince() {
		return addressProvince;
	}

	public void setAddressProvince(String addressProvince) {
		this.addressProvince = addressProvince;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public User getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(User userAddress) {
		this.userAddress = userAddress;
	}	
	
}

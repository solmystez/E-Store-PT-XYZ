package com.demo.lookopediaSinarmas.entity;

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
	private String address_label;

	private String address_description;
	private String address_postalCode;
	private String address_city;
	private String address_province;
	private String address_country;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	@JsonIgnore
	private User userAddress;

	public Long getAddress_id() {
		return address_id;
	}

	public void setAddress_id(Long address_id) {
		this.address_id = address_id;
	}

	public String getAddress_label() {
		return address_label;
	}

	public void setAddress_label(String address_label) {
		this.address_label = address_label;
	}

	public String getAddress_description() {
		return address_description;
	}

	public void setAddress_description(String address_description) {
		this.address_description = address_description;
	}

	public String getAddress_postalCode() {
		return address_postalCode;
	}

	public void setAddress_postalCode(String address_postalCode) {
		this.address_postalCode = address_postalCode;
	}

	public String getAddress_city() {
		return address_city;
	}

	public void setAddress_city(String address_city) {
		this.address_city = address_city;
	}

	public String getAddress_province() {
		return address_province;
	}

	public void setAddress_province(String address_province) {
		this.address_province = address_province;
	}

	public String getAddress_country() {
		return address_country;
	}

	public void setAddress_country(String address_country) {
		this.address_country = address_country;
	}

	public User getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(User userAddress) {
		this.userAddress = userAddress;
	}	
	
}

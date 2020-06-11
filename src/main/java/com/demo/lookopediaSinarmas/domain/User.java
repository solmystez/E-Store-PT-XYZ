package com.demo.lookopediaSinarmas.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "User Name is required")
	private String userName;
	
	@NotBlank(message = "Password is required")
	private String password;
	
//	@NotBlank(message = "User identifier is required")
//	@Size(min=4, max=5, message = "Please use 4 to 5 characters")
//	@Column(updatable = false, unique = true)
//	private String userIdentifier; //for seperate type member
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,  mappedBy = "user")
	@JsonIgnore
	private List<Cart> cart = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "user_merchant")
	@JsonIgnore
	private Merchant merchant;
	
	public User() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	
}

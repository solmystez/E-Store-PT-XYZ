package com.demo.lookopediaSinarmas.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Email(message = "Needs to be an email !")
	@NotBlank(message = "Email is required !")
	@Column(unique = true)
	private String email;
	
	@NotBlank(message = "Username is required !")
	private String username;
	
	@NotBlank(message = "Password is required !")
	private String password;
	
	@Transient //for match with pw, before persist, once persist not persist anymore
	private String confirmPassword;
	
	private Date created_At;
	private Date updated_At;
	
//	@NotBlank(message = "User identifier is required")
//	@Size(min=4, max=5, message = "Please use 4 to 5 characters")
//	@Column(updatable = false, unique = true)
//	private String userIdentifier; //for seperate type member
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "user")
	@JsonIgnore
	private Cart cart;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Date getCreated_At() {
		return created_At;
	}

	public void setCreated_At(Date created_At) {
		this.created_At = created_At;
	}

	public Date getUpdated_At() {
		return updated_At;
	}

	public void setUpdated_At(Date updated_At) {
		this.updated_At = updated_At;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	
	
	@PrePersist
	protected void onCreate() {
		this.created_At = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_At = new Date();
	}
	
	

	//	user details interface method
	//json ignore for, we want get rid of this from postman result
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {//we don't play role
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {//case : the account expired because didn't pay
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {//the account not locked
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {//make return true this too 
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}




}

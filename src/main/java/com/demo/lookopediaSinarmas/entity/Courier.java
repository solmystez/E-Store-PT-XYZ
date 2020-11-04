package com.demo.lookopediaSinarmas.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Courier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courier_id;
	
	@NotBlank(message = "Courier Name is Required")
	private String courierName;
	private String courierDescription;
	
	@Min(value = 0, message = "Courier price cannot less then 0 !")
	private int courierPrice;
	
	@OneToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy = "courier")
	@JsonIgnore
	private Cart cart;
	
	public Long getCourier_id() {
		return courier_id;
	}
	
	public void setCourier_id(Long courier_id) {
		this.courier_id = courier_id;
	}
	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getCourierName() {
		return courierName;
	}
	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}
	public String getCourierDescription() {
		return courierDescription;
	}
	public void setCourierDescription(String courierDescription) {
		this.courierDescription = courierDescription;
	}
	public int getCourierPrice() {
		return courierPrice;
	}
	public void setCourierPrice(int courierPrice) {
		this.courierPrice = courierPrice;
	}
}

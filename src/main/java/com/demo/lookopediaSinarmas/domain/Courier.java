package com.demo.lookopediaSinarmas.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Courier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String courierName;
	private String courierDescription;
	private String courierPrice;
	
	@OneToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy = "courier")
	@JsonIgnore
	private Order order;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getCourierPrice() {
		return courierPrice;
	}
	public void setCourierPrice(String courierPrice) {
		this.courierPrice = courierPrice;
	}	
}

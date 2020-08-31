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
	private Long courier_id;
	
	private String courierName;
	private String courierDescription;
	private String courierPrice;
	
	@OneToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			mappedBy = "courier")
	@JsonIgnore
	private Orders order;
	
	public Long getCourier_id() {
		return courier_id;
	}
	
	public void setCourier_id(Long courier_id) {
		this.courier_id = courier_id;
	}
	
	public Orders getOrder() {
		return order;
	}
	public void setOrder(Orders order) {
		this.order = order;
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

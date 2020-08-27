package com.demo.lookopediaSinarmas.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(value = 0, message = "Rating cannot be lower then 0")
	@Max(value = 5, message = "Max Rating value is 5 !")
	private Integer ratingValue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Product productRating;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User userRating;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(Integer ratingValue) {
		this.ratingValue = ratingValue;
	}

	public Product getProductRating() {
		return productRating;
	}

	public void setProductRating(Product productRating) {
		this.productRating = productRating;
	}
}

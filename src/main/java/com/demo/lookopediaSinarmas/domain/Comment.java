package com.demo.lookopediaSinarmas.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String comment_message;
	
	@ManyToOne
	@JoinColumn(name = "product_id", updatable = false)
	@JsonIgnore
	private Product productComment;
	
	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false)
	@JsonIgnore
	private User userComment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment_message() {
		return comment_message;
	}

	public void setComment_message(String comment_message) {
		this.comment_message = comment_message;
	}

	public Product getProductComment() {
		return productComment;
	}

	public void setProductComment(Product productComment) {
		this.productComment = productComment;
	}

	public User getUserComment() {
		return userComment;
	}

	public void setUserComment(User userComment) {
		this.userComment = userComment;
	}

}

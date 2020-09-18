package com.demo.lookopediaSinarmas.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private Long comment_id;
	
	private String comment_message;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", updatable = false)
	@JsonIgnore
	private Product productComment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", updatable = false)
	@JsonIgnore
	private User userComment;

	public Long getComment_id() {
		return comment_id;
	}

	public void setComment_id(Long comment_id) {
		this.comment_id = comment_id;
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

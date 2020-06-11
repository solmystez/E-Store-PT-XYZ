package com.demo.lookopediaSinarmas.domain;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Invoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Invoice Name is required")
	private String invoiceName;
	
	@NotBlank(message = "Invoice identifier is required")
	@Size(min=4, max=5, message = "Please use 4 to 5 characters")
	@Column(updatable = false, unique = true)
	private String invoiceIdentifier;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	@Column(updatable = false)
	private Date created_at;
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date updated_at;
	
	@OneToOne(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Cart cart;

	@PrePersist
	protected void onCreate() {
		this.created_at = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_at = new Date();
	}
}

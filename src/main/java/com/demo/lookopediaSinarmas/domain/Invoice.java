package com.demo.lookopediaSinarmas.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Invoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@NotBlank(message = "Invoice Name is required")
//	private String invoiceName;
//	
//	@NotBlank(message = "Invoice identifier is required")
//	@Size(min=4, max=5, message = "Please use 4 to 5 characters")
//	@Column(updatable = false, unique = true)
//	private String invoiceIdentifier;
	
	private String iProductName;
	private Integer iProductQuantity;
	private Integer iProductPrice;
	private Integer iTotalPrice;
	private Integer iGrandTotal;
	
	private String invoiceSequence;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	@Column(updatable = false)
	private Date created_at;
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date updated_at;
	
	
	@OneToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, 
			mappedBy = "cartD")
	@JsonIgnore
	private List<CartDetail> cart_detail = new ArrayList<>();
	
	@OneToOne(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private Cart cart;
	
	public Invoice() {}
	
//	public Invoice(CartDetail cart, Product product) {
//		super();
//		this.cart_detail = cart;
//	}
	
	@PrePersist
	protected void onCreate() {
		this.created_at = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_at = new Date();
	}
	
	
	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getInvoiceSequence() {
		return invoiceSequence;
	}

	public void setInvoiceSequence(String invoiceSequence) {
		this.invoiceSequence = invoiceSequence;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getiProductName() {
		return iProductName;
	}

	public void setiProductName(String iProductName) {
		this.iProductName = iProductName;
	}

	public Integer getiProductQuantity() {
		return iProductQuantity;
	}

	public void setiProductQuantity(Integer iProductQuantity) {
		this.iProductQuantity = iProductQuantity;
	}

	public Integer getiProductPrice() {
		return iProductPrice;
	}

	public void setiProductPrice(Integer iProductPrice) {
		this.iProductPrice = iProductPrice;
	}

	public Integer getiTotalPrice() {
		return iTotalPrice;
	}

	public void setiTotalPrice(Integer iTotalPrice) {
		this.iTotalPrice = iTotalPrice;
	}

	public Integer getiGrandTotal() {
		return iGrandTotal;
	}

	public void setiGrandTotal(Integer iGrandTotal) {
		this.iGrandTotal = iGrandTotal;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public List<CartDetail> getCart_detail() {
		return cart_detail;
	}

	public void setCart_detail(List<CartDetail> cart_detail) {
		this.cart_detail = cart_detail;
	}


	
	
}

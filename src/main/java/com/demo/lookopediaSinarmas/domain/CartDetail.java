package com.demo.lookopediaSinarmas.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = true)
	@JsonIgnore
    private Cart cart;

	private Integer invoiceSequence = 0;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
	@JsonIgnore
    private Product product;    
	
    @ManyToOne
    @JoinColumn(name = "invoice_id",nullable = true)
	@JsonIgnore
    private Invoice invoice;
    
	private Integer quantity = 0;
	private Integer totalToPaid = 0;
	private String productName;
	
	private CartDetail() {}
	
	public CartDetail(Cart cart, Product product) {
		super();
		this.cart = cart;
		this.product = product;
	}
	
	

	public Integer getInvoiceSequence() {
		return invoiceSequence;
	}

	public void setInvoiceSequence(Integer invoiceSequence) {
		this.invoiceSequence = invoiceSequence;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTotalToPaid() {
		return totalToPaid;
	}

	public void setTotalToPaid(Integer totalToPaid) {
		this.totalToPaid = totalToPaid;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
	
	
}

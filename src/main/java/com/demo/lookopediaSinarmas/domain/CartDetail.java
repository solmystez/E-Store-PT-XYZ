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
public class CartDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "cart_id", nullable = true)
//	@JsonIgnore
//    private Cart cart;
	
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
	
	private String status;
	private String invoiceIdentifier;
	
	private CartDetail() {}
	
	public CartDetail(Invoice invoice, Product product) {
		super();
		this.invoice = invoice;
		this.product = product;
	}

	public String getInvoiceIdentifier() {
		return invoiceIdentifier;
	}

	public void setInvoiceIdentifier(String invoiceIdentifier) {
		this.invoiceIdentifier = invoiceIdentifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
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

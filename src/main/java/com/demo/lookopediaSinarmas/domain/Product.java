package com.demo.lookopediaSinarmas.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Product Name is required")
	private String productName;

	@NotBlank(message = "Product description is required")
	private String productDescription;

	@Min(value = 0, message = "price cannot less then 0 !")
	private int productPrice;

	@Min(value = 0, message = "stock cannot less then 0 !")
	private int productStock;
	
	private String productImage;
	private String productCategory;
	
	@JsonFormat(pattern = "yyyy-mm-dd") 
	private Date created_at;
	  
	@JsonFormat(pattern = "yyyy-mm-dd") 
	private Date updated_at;
	  
	 
//	@ManyToMany
//	@JoinTable(
//			name = "cart_detail",
//			joinColumns = @JoinColumn(name="product_id"),
//			inverseJoinColumns = @JoinColumn(name="cart_id")
//			)
//	@JsonIgnore
//	private List<Cart> cart_product = new ArrayList<>();
	
	@OneToMany(mappedBy = "product", 
			cascade = CascadeType.ALL, 
			orphanRemoval = true)
	private List<CartDetail> cart_detail = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "merchant_id", 
		updatable = false, 
		nullable= true)//default nullable = true
	@JsonIgnore
	private Merchant merchant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getProductStock() {
		return productStock;
	}

	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}
	



	public List<CartDetail> getCart_detail() {
		return cart_detail;
	}

	public void setCart_detail(List<CartDetail> cart_detail) {
		this.cart_detail = cart_detail;
	}

	public Date getCreated_at() { return created_at; }
	
	public void setCreated_at(Date created_at) { this.created_at = created_at; }
	 
	public Date getUpdated_at() { return updated_at; }
	 
	public void setUpdated_at(Date updated_at) { this.updated_at = updated_at; }
	


	@PrePersist
	protected void onCreate() { this.created_at = new Date(); }
	
	@PreUpdate 
	protected void onUpdate() { this.updated_at = new Date(); }
	 
}

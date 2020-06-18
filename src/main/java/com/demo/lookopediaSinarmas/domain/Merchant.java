package com.demo.lookopediaSinarmas.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Merchant {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String merchantName;
	private Integer merchantBalance = 0;

//	private String merchantType;//premium, usual member

	private Integer totalProduct = 0;
	private String merchantAddress;
	
	@OneToOne
	@JoinColumn(name = "user_id", updatable = false)
	@JsonIgnore
	private User user_merchant;
	
	@OneToMany(fetch = FetchType.LAZY, 
			cascade = CascadeType.REFRESH, 
			mappedBy = "merchant", orphanRemoval = true)
	private List<Product> productList = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getMerchantBalance() {
		return merchantBalance;
	}

	public void setMerchantBalance(Integer merchantBalance) {
		this.merchantBalance = merchantBalance;
	}

	public Integer getTotalProduct() {
		return totalProduct;
	}

	public void setTotalProduct(Integer totalProduct) {
		this.totalProduct = totalProduct;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public User getUser_merchant() {
		return user_merchant;
	}

	public void setUser_merchant(User user_merchant) {
		this.user_merchant = user_merchant;
	}

	public User getUser() {
		return user_merchant;
	}

	public void setUser(User user) {
		this.user_merchant = user;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	
	
}

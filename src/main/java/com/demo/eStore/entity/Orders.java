package com.demo.eStore.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Orders {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private double total_price;
	private Integer total_item;
	private String status;//(process, rejected, finish)
	private String merchantName;
	private String merchantAddress;
	private String username;
	private String userAddress;
	private String courierName;
	private String invoiceNumber;
	private String hasVoucher;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	@Column(updatable = false)
	private Date created_at;
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date updated_at;
	
	@OneToMany(mappedBy = "order", 
			cascade = CascadeType.ALL, 
			orphanRemoval = true)
	private List<Cart> cart_detail = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", updatable = false)
	@JsonIgnore
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voucher_id", updatable = false)
	@JsonIgnore
	private Voucher voucher;
	
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courier_id", nullable = true)
	@JsonIgnore
	private Courier courier;
	
	public Orders() {}
	
	@PrePersist
	protected void onCreate() {
		this.created_at = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_at = new Date();
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getHasVoucher() {
		return hasVoucher;
	}

	public void setHasVoucher(String hasVoucher) {
		this.hasVoucher = hasVoucher;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public Integer getTotal_item() {
		return total_item;
	}

	public void setTotal_item(Integer total_item) {
		this.total_item = total_item;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public List<Cart> getCart_detail() {
		return cart_detail;
	}

	public void setCart_detail(List<Cart> cart_detail) {
		this.cart_detail = cart_detail;
	}


	public Voucher getVoucher() {
		return voucher;
	}

	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}

}

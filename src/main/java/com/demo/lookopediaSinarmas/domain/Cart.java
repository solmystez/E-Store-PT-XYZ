package com.demo.lookopediaSinarmas.domain;

import java.util.ArrayList;
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
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@NaturalIdCache
@Cache(
    usage = CacheConcurrencyStrategy.READ_WRITE
)
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer totalItem = 0;
	private Integer totalPrice = 0;

//	not paid, pick up, done
//	private String status;
	
	@Column(updatable = false, unique = true)
	private String cartSequence;
	
//	@OneToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "user_id", updatable = false)
//	@JsonIgnore
//	private User user;
//	
//	@OneToOne
//	@JoinColumn(name = "invoice_id", updatable = false)
//	@JsonIgnore
//	private Invoice invoice;
	
//	@ManyToMany
//	@JoinTable(
//			name = "cart_detail",
//			joinColumns = @JoinColumn(name="cart_id"),
//			inverseJoinColumns = @JoinColumn(name="product_id")
//			)
//	private List<Product> cart_product = new ArrayList<>();

	
//	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<CartDetail> cart_detail = new ArrayList<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(Integer totalItem) {
		this.totalItem = totalItem;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getCartSequence() {
		return cartSequence;
	}

	public void setCartSequence(String cartSequence) {
		this.cartSequence = cartSequence;
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public Invoice getInvoice() {
//		return invoice;
//	}
//
//	public void setInvoice(Invoice invoice) {
//		this.invoice = invoice;
//	}

//	public List<CartDetail> getCart_detail() {
//		return cart_detail;
//	}
//
//	public void setCart_detail(List<CartDetail> cart_detail) {
//		this.cart_detail = cart_detail;
//	}
	
}
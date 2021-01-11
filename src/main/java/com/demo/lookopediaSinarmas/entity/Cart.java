package com.demo.lookopediaSinarmas.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cart_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
	@JsonIgnore
    private Product product;    
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = true)
	@JsonIgnore
    private Orders order;
    
	private Integer quantity = 0;
	
	private String username;
	
	private Long p_id;
	private int p_qty;
	private int p_price;
	private String p_name;
	private String p_description;
	private String p_filePath;
	private String status; //not paid, paid
	private int total_price;
	private String hasRating;
	
	private Cart() {}
	
	public Cart(Orders order, Product product) {
		super();
		this.order = order;
		this.product = product;
	}

	public int getTotal_price() {
		return total_price;
	}

	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}

	public String getP_filePath() {
		return p_filePath;
	}

	public void setP_filePath(String p_filePath) {
		this.p_filePath = p_filePath;
	}

	public int getP_price() {
		return p_price;
	}

	public void setP_price(int p_price) {
		this.p_price = p_price;
	}
	
	public String getHasRating() {
		return hasRating;
	}

	public void setHasRating(String hasRating) {
		this.hasRating = hasRating;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
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

	public Long getCart_id() {
		return cart_id;
	}

	public void setCart_id(Long cart_id) {
		this.cart_id = cart_id;
	}

	public Long getP_id() {
		return p_id;
	}

	public void setP_id(Long p_id) {
		this.p_id = p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}




	public String getP_description() {
		return p_description;
	}

	public void setP_description(String p_description) {
		this.p_description = p_description;
	}

	public int getP_qty() {
		return p_qty;
	}

	public void setP_qty(int p_qty) {
		this.p_qty = p_qty;
	}
	
	

}

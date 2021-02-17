package com.demo.eStore.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Voucher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long voucher_id;
	
//	@NotBlank(message = "Voucher name is required !")
	private String voucherName;
	
//	@NotBlank(message = "Voucher Code is required !")
	@Column(unique = true)
	private String voucherCode;
	
//	@Min(value = 1)
	private double voucherDiscount;
	
//	@NotBlank(message = "Voucher Description is required !")
	private String voucherDescription;
	
	@OneToMany(orphanRemoval = true,
			cascade = CascadeType.ALL,
			mappedBy = "voucher")
	@JsonIgnore
	private List<Orders> order;

	public Long getVoucher_id() {
		return voucher_id;
	}

	public void setVoucher_id(Long voucher_id) {
		this.voucher_id = voucher_id;
	}

	public String getVoucherName() {
		return voucherName;
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public double getVoucherDiscount() {
		return voucherDiscount;
	}

	public void setVoucherDiscount(double voucherDiscount) {
		this.voucherDiscount = voucherDiscount;
	}

	public String getVoucherDescription() {
		return voucherDescription;
	}

	public void setVoucherDescription(String voucherDescription) {
		this.voucherDescription = voucherDescription;
	}

	public List<Orders> getOrder() {
		return order;
	}

	public void setOrder(List<Orders> order) {
		this.order = order;
	}

}

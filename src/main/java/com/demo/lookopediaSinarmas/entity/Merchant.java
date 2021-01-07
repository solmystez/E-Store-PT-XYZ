package com.demo.lookopediaSinarmas.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Merchant implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Merchant name is required")
	@Column(unique = true)
	private String merchantName;
	private String merchantDesc;
	
	
	private Integer merchantBalance = 0;
	private String merchantAddress;
	private String username;
	
	private Integer totalProduct = 0;
	
		
	private String fileName;
	private String filePath;
	private String fileType;
	private String fileSize;
		
	@OneToOne
	@JoinColumn(name = "user_id", updatable = false)
	@JsonIgnore
	private User userMerchant;
	
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
	
	public String getMerchantDesc() {
		return merchantDesc;
	}

	public void setMerchantDesc(String merchantDesc) {
		this.merchantDesc = merchantDesc;
	}

	public Integer getMerchantBalance() {
		return merchantBalance;
	}

	public void setMerchantBalance(Integer merchantBalance) {
		this.merchantBalance = merchantBalance;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getTotalProduct() {
		return totalProduct;
	}

	public void setTotalProduct(Integer totalProduct) {
		this.totalProduct = totalProduct;
	}

	public User getUserMerchant() {
		return userMerchant;
	}

	public void setUserMerchant(User userMerchant) {
		this.userMerchant = userMerchant;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
		
}

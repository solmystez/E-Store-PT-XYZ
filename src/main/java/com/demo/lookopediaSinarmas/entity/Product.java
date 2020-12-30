package com.demo.lookopediaSinarmas.entity;

import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long product_id;
	
	@Column(nullable = false)
	private String productName;
	
	private String productDescription;
	private int productPrice; // belum validator
	private int productStock;// belum validator
	private int productWeight;
//	@Lob
//	@Column(name = "productImage", length = Integer.MAX_VALUE, nullable = true)
//	private byte[] productImage;
	
	private String fileName;
	private String filePath;
	private String fileType;
	private String fileSize;
	
	private String merchantName;
	private String base64;
	
	@NotBlank(message = "product category must be input")
	private String productCategoryName;

	//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "created_date", nullable = false)
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
	private List<Cart> cart_detail = new ArrayList<>();
	
	@OneToMany(mappedBy = "productComment", 
			cascade = CascadeType.ALL, 
			orphanRemoval = true)
	private List<Comment> comment = new ArrayList<>();
	
	@OneToMany(mappedBy = "productRating", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true)
	private List<Rating> rating = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="productCategory_id")
	@JsonIgnore
	private Category productCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)//we dont need load the merchant information, we know the project
	@JsonIgnore//resolve infinite recursion problem
	private Merchant merchant;
	
	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public int getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(int productWeight) {
		this.productWeight = productWeight;
	}
	
	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
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

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public List<Rating> getRating() {
		return rating;
	}

	public void setRating(List<Rating> rating) {
		this.rating = rating;
	}

	public Category getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(Category productCategory) {
		this.productCategory = productCategory;
	}
	
	public Long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Long product_id) {
		this.product_id = product_id;
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
	
	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public List<Cart> getCart_detail() {
		return cart_detail;
	}

	public void setCart_detail(List<Cart> cart_detail) {
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

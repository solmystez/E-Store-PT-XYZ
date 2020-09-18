package com.demo.lookopediaSinarmas.exceptions.product;

public class ProductIdExceptionResponse {

	private String productIdentifier;
	
	public ProductIdExceptionResponse(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}

	public String getProductIdentifier() {
		return productIdentifier;
	}

	public void setProductIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}
	
	
}

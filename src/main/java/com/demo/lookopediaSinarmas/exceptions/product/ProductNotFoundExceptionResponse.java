package com.demo.lookopediaSinarmas.exceptions.product;

public class ProductNotFoundExceptionResponse {
	
	private String productNotFound;

	public ProductNotFoundExceptionResponse(String productNotFound) {
		this.productNotFound = productNotFound;
	}

	public String getProductNotFound() {
		return productNotFound;
	}

	public void setProductNotFound(String productNotFound) {
		this.productNotFound = productNotFound;
	}	
	
}

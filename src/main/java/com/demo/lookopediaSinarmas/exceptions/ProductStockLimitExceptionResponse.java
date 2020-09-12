package com.demo.lookopediaSinarmas.exceptions;

public class ProductStockLimitExceptionResponse {

	private String productStockLimit;

	public ProductStockLimitExceptionResponse(String productStockLimit) {
		this.productStockLimit = productStockLimit;
	}
	
	public String getProductStockLimit() {
		return productStockLimit;
	}

	public void setProductStockLimit(String productStockLimit) {
		this.productStockLimit = productStockLimit;
	}
}

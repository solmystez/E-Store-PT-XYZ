package com.demo.eStore.exceptions.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductStockLimitException extends RuntimeException{
	
	public ProductStockLimitException(String message) {
		super(message);
	}
}

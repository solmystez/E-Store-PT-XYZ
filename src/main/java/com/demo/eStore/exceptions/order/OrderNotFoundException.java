package com.demo.eStore.exceptions.order;

public class OrderNotFoundException extends RuntimeException{

	public OrderNotFoundException(String message) {
		super(message);
	}
}

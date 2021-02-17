package com.demo.eStore.exceptions.category;

public class CategoryAlreadyExistsException extends RuntimeException{
	
	public CategoryAlreadyExistsException(String message) {
		super(message);
	}
	
}

package com.demo.eStore.exceptions.email;

public class EmailAlreadyExistsException extends RuntimeException{

	public EmailAlreadyExistsException(String message) {
		super(message);
	}
	
}

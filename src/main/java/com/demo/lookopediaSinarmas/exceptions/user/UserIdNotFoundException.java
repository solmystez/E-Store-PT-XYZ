package com.demo.lookopediaSinarmas.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIdNotFoundException extends RuntimeException{
	
	public UserIdNotFoundException(String message) {
		super(message);
	}
} 

package com.demo.lookopediaSinarmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MerchantNotFoundException extends RuntimeException{

	public MerchantNotFoundException(String message) {
		super(message);
	}
}

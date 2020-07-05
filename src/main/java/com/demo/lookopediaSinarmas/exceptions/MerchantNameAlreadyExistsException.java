package com.demo.lookopediaSinarmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MerchantNameAlreadyExistsException extends RuntimeException{

	public MerchantNameAlreadyExistsException(String message) {
		super(message);
	}
}

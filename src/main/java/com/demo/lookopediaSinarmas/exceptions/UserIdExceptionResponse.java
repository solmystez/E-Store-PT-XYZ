package com.demo.lookopediaSinarmas.exceptions;

public class UserIdExceptionResponse {

	private String userIdentifier;

	public UserIdExceptionResponse(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	
}
package com.demo.lookopediaSinarmas.exceptions.user;

public class UserIdNotFoundExceptionResponse {

	private String userIdentifier;

	public UserIdNotFoundExceptionResponse(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	
}
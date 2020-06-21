package com.demo.lookopediaSinarmas.exceptions;

public class InvalidLoginResponse {
	
	private String email; //please note here, must be same, when we wired up to user entity
	private String password;

	public InvalidLoginResponse() {
		super();
		this.email = "Invalid Email";
		this.password = "Invalid Password";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
}

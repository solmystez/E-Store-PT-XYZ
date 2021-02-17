package com.demo.eStore.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {	//object an object for your request
	//need a response in payload to pass react side, when we have a valid user 
	//return back JSON WEBtoken, so redux can keep token
	@NotBlank(message = "Email cannot be blank")
	private String email;//must match
	
	@NotBlank(message = "Password cannot be blank")
	private String password;

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

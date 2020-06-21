package com.demo.lookopediaSinarmas.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.demo.lookopediaSinarmas.exceptions.InvalidLoginResponse;
import com.google.gson.Gson;

@Component 
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

		//a user trying to access a resource that require authentication
		//401 unauthorized(postman response) result
		//task : for give better response for frontEnd
	
	
	@Override //this is what user will see, if trying to access
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException authException) throws IOException, ServletException {
		
		InvalidLoginResponse loginResponse = new InvalidLoginResponse();//class from exception 
		String jsonLoginResponse = new Gson().toJson(loginResponse); //gson = convert string into JSON Response
		
		
		//once done, next handling the http response(BE CAREFUL NOT PICK httpServletREQUEST)
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setStatus(401);
		httpServletResponse.getWriter().print(jsonLoginResponse);
		
	}


}
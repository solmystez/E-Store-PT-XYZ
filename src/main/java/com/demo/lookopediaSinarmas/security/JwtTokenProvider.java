package com.demo.lookopediaSinarmas.security;

import static com.demo.lookopediaSinarmas.security.SecurityConstants.EXPIRATION_TIME;
import static com.demo.lookopediaSinarmas.security.SecurityConstants.SECRET;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.demo.lookopediaSinarmas.domain.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	//generate token
		//org.springframework.security.core.Authentication;
		public String generateToken(Authentication authentication) {//token gonna be string
			User user = (User) authentication.getPrincipal();//need to be cascade userType 
			Date now = new Date(System.currentTimeMillis());
			
			Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
			
			//the token is a string, the token is going to store some of user attribute
			//so we cannot pass the USER_ID with normal, from which is Long
			String userId = Long.toString(user.getId());
			
			//refactor : set claim one by one in here, not inside Jwts.builder()
			//setClaim take Map<K , V>, actually we 
			Map<String, Object> claims = new HashMap<>();
			claims.put("id", (Long.toString(user.getId())));
			claims.put("email", user.getEmail());
			claims.put("username", user.getUsername());
			//throw roles in here too
			
			//this is we start build our jsonWebToken
			return Jwts.builder()
					.setSubject(userId)
					.setClaims(claims) //claim information about the information about user
					//you can pass claim one by one 
					.setIssuedAt(now)
					.setExpiration(expiryDate)
					.signWith(SignatureAlgorithm.HS512, SECRET)//(signature Algo, secretKey) signature algo so many //research based on your case
					.compact();//wrap thing all over here
		}
		 
		
		//validate the token
		
		
		//get user id from token 
		//custom detail service validating user, when validate the token
}

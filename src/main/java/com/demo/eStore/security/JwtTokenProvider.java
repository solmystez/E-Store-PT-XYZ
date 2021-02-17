package com.demo.eStore.security;

import static com.demo.eStore.security.SecurityConstants.EXPIRATION_TIME;
import static com.demo.eStore.security.SecurityConstants.SECRET;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.demo.eStore.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

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
			claims.put("id", userId);
			claims.put("email", user.getEmail());
			claims.put("username", user.getUsername());
			claims.put("password", user.getPassword());
//			claims.put("hasMerchant", user.get hasmerchant);//boolean not read??
			
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
		 
		
		//validate the token, for Header in postman
		public boolean validateToken(String token) {
			try {
				Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
				return true;
				
				//all catch, comes from JSON WebToken library
			}catch (SignatureException ex) {
				System.out.println("Invalid JWT Signature");
			}catch (MalformedJwtException ex) {
				System.out.println("Invalid JWT Token");
			}catch(ExpiredJwtException ex) {
				System.out.println("Expired JWT Token");
			}catch (UnsupportedJwtException ex) {
				System.out.println("Unsupported JWT Token");
			}catch (IllegalArgumentException ex) {
				System.out.println("JWT Claims String is Empty");
			}
			return false;//means invalid token
		}
		
		//get user id from token 
		//custom detail service validating user, when validate the token
		public Long getUserIdFromJwt(String token) {
			Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
			String id = (String)claims.get("id");
			
			return Long.parseLong(id);
		}
}

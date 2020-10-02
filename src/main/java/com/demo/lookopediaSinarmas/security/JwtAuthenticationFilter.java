package com.demo.lookopediaSinarmas.security;


import static com.demo.lookopediaSinarmas.security.SecurityConstants.HEADER_STRING;
import static com.demo.lookopediaSinarmas.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.services.otherService.CustomUserDetailService;



public class JwtAuthenticationFilter extends OncePerRequestFilter{
	//here use customeUserDetailService to validate user + token
	//authenticate user, have access to get resource
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private CustomUserDetailService customeUserDetailService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//1. grab JSON WebToken,from request
		try {
			String jwt = getJWTFromRequest(request);
			
			if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
				Long userId = jwtTokenProvider.getUserIdFromJwt(jwt);
				User userDetails = customeUserDetailService.loadUserById(userId);
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Collections.EMPTY_LIST);//we not use credential here, because not doing anything with password using token 
						//parameter 3 = take a list of roles, but we dont have that
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
		} catch (Exception e) {
			logger.error("Couldn't set user authentication in security context", e);
		}
		
		filterChain.doFilter(request, response);
		
	}

	//extracted method for //1. grab JSON WebToken,from request
	private String getJWTFromRequest(HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader(HEADER_STRING);
	
		//org.springframework.util.StringUtils;
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
	
}
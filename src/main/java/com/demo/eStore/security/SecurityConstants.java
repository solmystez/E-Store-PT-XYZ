package com.demo.eStore.security;

public class SecurityConstants {

	public static final String SIGN_UP_URLS = "/api/user/**";
	public static final String H2_URL = "h2-console/**";
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final String TOKEN_PREFIX = "Bearer "; //important: space 
	public static final String HEADER_STRING = "Authorization";
	public static final long EXPIRATION_TIME = 18000_00000;//300_000 = 30 sec
	
}

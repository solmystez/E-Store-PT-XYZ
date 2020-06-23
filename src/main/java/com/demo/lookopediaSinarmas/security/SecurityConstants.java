package com.demo.lookopediaSinarmas.security;

public class SecurityConstants {

	public static final String SIGN_UP_URLS = "/api/user/**";
	public static final String H2_URL = "h2-console/**";
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final String TOKEN_PREFIX = "Bearer "; //important: space 
	public static final String HEADER_STRING = "Authorization";//very important
	public static final long EXPIRATION_TIME = 30_000;//30000miliSec = 30sec
	
}

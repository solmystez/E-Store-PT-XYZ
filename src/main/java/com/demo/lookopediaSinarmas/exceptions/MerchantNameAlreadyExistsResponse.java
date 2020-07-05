package com.demo.lookopediaSinarmas.exceptions;

public class MerchantNameAlreadyExistsResponse {

	String merchantName;

	public MerchantNameAlreadyExistsResponse(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	
}

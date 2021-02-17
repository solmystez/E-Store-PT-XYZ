package com.demo.eStore.exceptions.merchant;

public class MerchantNotFoundExceptionResponse {

	private String merchantIdentifier;

	public MerchantNotFoundExceptionResponse(String merchantIdentifier) {
		this.merchantIdentifier = merchantIdentifier;
	}

	public String getMerchantIdentifier() {
		return merchantIdentifier;
	}

	public void setMerchantIdentifier(String merchantIdentifier) {
		this.merchantIdentifier = merchantIdentifier;
	}
	
	
}

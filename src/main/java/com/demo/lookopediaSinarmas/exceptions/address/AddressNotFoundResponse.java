package com.demo.lookopediaSinarmas.exceptions.address;

public class AddressNotFoundResponse {
	
	private String addressName;
	
	public AddressNotFoundResponse(String addressName) {
		this.addressName = addressName;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	
}

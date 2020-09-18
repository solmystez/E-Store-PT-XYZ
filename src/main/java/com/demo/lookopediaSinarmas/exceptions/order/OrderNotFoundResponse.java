package com.demo.lookopediaSinarmas.exceptions.order;

public class OrderNotFoundResponse {

	String invoiceIdentifier;

	public String getInvoiceIdentifier() {
		return invoiceIdentifier;
	}

	public void setInvoiceIdentifier(String invoiceIdentifier) {
		this.invoiceIdentifier = invoiceIdentifier;
	}
	
	public OrderNotFoundResponse(String invoiceIdentifier) {
		this.invoiceIdentifier = invoiceIdentifier;
	}
	
	
	
}

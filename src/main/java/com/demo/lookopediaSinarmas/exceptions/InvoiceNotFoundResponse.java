package com.demo.lookopediaSinarmas.exceptions;

public class InvoiceNotFoundResponse {

	String invoiceIdentifier;

	public String getInvoiceIdentifier() {
		return invoiceIdentifier;
	}

	public void setInvoiceIdentifier(String invoiceIdentifier) {
		this.invoiceIdentifier = invoiceIdentifier;
	}
	
	public InvoiceNotFoundResponse(String invoiceIdentifier) {
		this.invoiceIdentifier = invoiceIdentifier;
	}
	
	
	
}

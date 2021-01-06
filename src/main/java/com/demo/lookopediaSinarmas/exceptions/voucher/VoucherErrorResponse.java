package com.demo.lookopediaSinarmas.exceptions.voucher;

public class VoucherErrorResponse {

	private String voucherName;
	
	public VoucherErrorResponse(String voucherName) {
		this.voucherName = voucherName;
	}

	public String getVoucherName() {
		return voucherName;
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	
}

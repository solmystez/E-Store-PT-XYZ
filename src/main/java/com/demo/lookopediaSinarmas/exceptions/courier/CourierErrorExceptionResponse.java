package com.demo.lookopediaSinarmas.exceptions.courier;

public class CourierErrorExceptionResponse {

	private String courierMessage;
	
	public CourierErrorExceptionResponse(String courierMessage) {
		this.courierMessage = courierMessage;
	}

	public String getCourierMessage() {
		return courierMessage;
	}

	public void setCourierMessage(String courierMessage) {
		this.courierMessage = courierMessage;
	}
	
}

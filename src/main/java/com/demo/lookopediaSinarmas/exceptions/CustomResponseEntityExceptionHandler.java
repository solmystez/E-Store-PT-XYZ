package com.demo.lookopediaSinarmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler
	public final ResponseEntity<Object> handleProductIdException(ProductIdException ex, WebRequest request) {
		ProductIdExceptionResponse exceptionResponse = new ProductIdExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
		ProductNotFoundExceptionResponse exceptionResponse = new ProductNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleUserIdException(UserIdNotFoundException ex, WebRequest request) {
		UserIdNotFoundExceptionResponse exceptionResponse = new UserIdNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleMerchantIdException(MerchantNotFoundException ex, WebRequest request) {
		MerchantNotFoundExceptionResponse exceptionResponse = new MerchantNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleUsernameAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {
		EmailAlreadyExistsResponse exceptionResponse = new EmailAlreadyExistsResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleMerchantNameAlreadyExists(MerchantNameAlreadyExistsException ex, WebRequest request) {
		MerchantNameAlreadyExistsResponse exceptionResponse = new MerchantNameAlreadyExistsResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleInvoiceNotFound(InvoiceNotFoundException ex, WebRequest request) {
		InvoiceNotFoundResponse exceptionResponse = new InvoiceNotFoundResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
}

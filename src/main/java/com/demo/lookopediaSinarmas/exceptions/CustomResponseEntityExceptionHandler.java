package com.demo.lookopediaSinarmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.demo.lookopediaSinarmas.exceptions.address.AddressNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.address.AddressNotFoundResponse;
import com.demo.lookopediaSinarmas.exceptions.category.CategoryAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.category.CategoryAlreadyExistsResponse;
import com.demo.lookopediaSinarmas.exceptions.comment.CommentNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.comment.CommentNotFountExceptionResponse;
import com.demo.lookopediaSinarmas.exceptions.courier.CourierErrorException;
import com.demo.lookopediaSinarmas.exceptions.courier.CourierErrorExceptionResponse;
import com.demo.lookopediaSinarmas.exceptions.email.EmailAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.email.EmailAlreadyExistsResponse;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNameAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNameAlreadyExistsResponse;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNotFoundExceptionResponse;
import com.demo.lookopediaSinarmas.exceptions.order.OrderNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.order.OrderNotFoundResponse;
import com.demo.lookopediaSinarmas.exceptions.product.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductIdExceptionResponse;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundExceptionResponse;
import com.demo.lookopediaSinarmas.exceptions.product.ProductStockLimitException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductStockLimitExceptionResponse;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundExceptionResponse;
import com.demo.lookopediaSinarmas.exceptions.voucher.VoucherErrorException;
import com.demo.lookopediaSinarmas.exceptions.voucher.VoucherErrorResponse;

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
	public final ResponseEntity<Object> handleInvoiceNotFound(OrderNotFoundException ex, WebRequest request) {
		OrderNotFoundResponse exceptionResponse = new OrderNotFoundResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleCategoryNameAlreadyExists(CategoryAlreadyExistsException ex, WebRequest request) {
		CategoryAlreadyExistsResponse exceptionResponse = new CategoryAlreadyExistsResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> productStockLimit(ProductStockLimitException ex, WebRequest request) {
		ProductStockLimitExceptionResponse exceptionResponse = new ProductStockLimitExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> commentNotFound(CommentNotFoundException ex, WebRequest request) {
		CommentNotFountExceptionResponse exceptionResponse = new CommentNotFountExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> courierException(CourierErrorException ex, WebRequest request) {
		CourierErrorExceptionResponse exceptionResponse = new CourierErrorExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> addressException(AddressNotFoundException ex, WebRequest request) {
		AddressNotFoundResponse exceptionResponse = new AddressNotFoundResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> addressException(VoucherErrorException ex, WebRequest request) {
		VoucherErrorResponse exceptionResponse = new VoucherErrorResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
}

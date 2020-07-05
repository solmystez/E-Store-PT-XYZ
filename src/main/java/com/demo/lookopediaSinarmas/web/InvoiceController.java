package com.demo.lookopediaSinarmas.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.services.InvoiceService;
import com.demo.lookopediaSinarmas.services.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
//	@PostMapping("/makeInvoiceFromCartId/{cart_id}")
//	public ResponseEntity<?> makeInvoiceFromCartId(@Valid @RequestBody Invoice invoice, BindingResult result,
//			 							@PathVariable Long cart_id){
//				
//		
//		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
//		if(mapError != null) return mapError;
//		
//		Invoice invoice1 = invoiceService.createInvoice(cart_id, invoice);
//		return new ResponseEntity<Invoice>(invoice1, HttpStatus.CREATED);
//	}
}

package com.demo.lookopediaSinarmas.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/loadAllInvoice/{user_id}")
	public Iterable<Invoice> loadAllInvoiceUser(@PathVariable Long user_id){
		return invoiceService.loadAllInvoiceByUserId(user_id);	
	}
	
	//buat tombol bayar : generate new invoice, user invoice_now updated
	@PostMapping("/processItem/{invoice_identifier}/{user_id}")
	public ResponseEntity<?> processItem(@Valid @RequestBody Invoice invoice,
			BindingResult result, @PathVariable String invoice_identifier, @PathVariable Long user_id) {
		
		
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		
		Invoice invoice1 = invoiceService.processItem(invoice_identifier, user_id);
				
		return new ResponseEntity<Invoice>(invoice1, HttpStatus.CREATED);
	}
	

	
	
}

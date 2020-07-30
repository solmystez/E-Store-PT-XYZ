package com.demo.lookopediaSinarmas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.InvoiceNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.InvoiceRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class InvoiceService {
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartDetailRepository cartDetailRepository;
	
	@Autowired 
	UserService userService;
	
	
	public Invoice processItem(String invoice_identifier, Long user_id) {	
		
		Invoice invoice = null;
		User user = null;
		
		invoice = invoiceRepository.findByInvoiceIdentifier(invoice_identifier);
		if(invoice == null) {
			throw new InvoiceNotFoundException("invoice not found");
		}
		
		user = userRepository.findById(invoice.getUser().getId()).get();
	
		Integer invSeq = 0;
		if(user.getInvoice() != null) invSeq = user.getInvoiceSequence();
		invSeq++;
		user.setInvoiceSequence(invSeq);
		
		CartDetail cartDetail = cartDetailRepository.findByInvoiceIdentifier(invoice_identifier);

		Integer merchantBal = 0;

		
//		merchant.setMerchantBalance(cartDetail.getTotalToPaid());
					
		userService.applyInvoiceNow(user_id, user);

		return invoiceRepository.save(invoice);

	}

	public List<Invoice> loadAllInvoiceByUserId(Long user_id) {
		return invoiceRepository.findAllByUserId(user_id);
	}
	
}

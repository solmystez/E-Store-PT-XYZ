package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.CartRepository;
import com.demo.lookopediaSinarmas.repositories.InvoiceRepository;

@Service
public class InvoiceService {
	
	@Autowired
	InvoiceRepository invoiceRepository;
		
	@Autowired
	CartDetailRepository cartDetailRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CartService cartService;
	
	public Invoice createInvoice(Long cartId, Invoice invoice) {
		//find cart id
		Cart cart = cartRepository.findById(cartId).get();
		
		Iterator<CartDetail> it = cartDetailRepository.findAllByCartId(cartId).iterator();

		
		if(!it.hasNext()) {
			it = cart.getCart_detail().iterator();
		}
		
		while(it.hasNext()) {
			CartDetail c = it.next();
			
			
		}
		
		CartDetail cartDetail = cartDetailRepository.findByCartId(cartId);
		
//		CartDetail cartDetail = cartDetailRepository.findByCartId(cartId);
		
		//store to invoice data
		
		Integer invoiceSequence = cartDetail.getInvoiceSequence();
		invoiceSequence++;
		
		cartDetail.setInvoiceSequence(invoiceSequence);
		
		invoice.setInvoiceSequence("Invoice-" + invoiceSequence);
		//need invoice identifier for react maybe
		
		
		//
		
		return invoiceRepository.save(invoice);
	}
}

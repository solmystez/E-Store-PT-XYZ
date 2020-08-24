package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.InvoiceNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.InvoiceRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class CartService {

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	CartDetailRepository cartDetailRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	//load all item before go to /buy
	public List<CartDetail> getCartDetailByInvoiceIdentifier(String invoice_now) {
		
		List<CartDetail> cartDetail = null;
	
		cartDetail = cartDetailRepository.findAllByInvoiceIdentifier(invoice_now);

		return cartDetail;
	}
	
	//delete product on cart, not delete product
	@Transactional
	public List<CartDetail> removeProductFromCart(Long product_id, String invoiceIdentifier) {
//		Invoice invoice = null;
//		
//		invoice = invoiceRepository.findByInvoiceIdentifier(cartDetail.getInvoiceIdentifier());
//		if(invoice == null) throw new InvoiceNotFoundException("invoice not found");

		
		try {
		   cartDetailRepository.deleteCartDetailByInvoiceIdentifierAndProductId(invoiceIdentifier, product_id);
//			cartDetailRepository.delete(cartDetail);
		} catch (Exception e) {
			System.err.println(e);
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
		return cartDetailRepository.findAllByInvoiceIdentifier(invoiceIdentifier);
	
	}
	
	public Invoice addProductToCartOrAddQty(Long product_id, Long user_id, Invoice invoice) {
		
//		try {
			//check status cart 1. paid = generate new cart, 
			//if invoice null || invoice.status != paid
			
			
//			product prtama kali add ke cart? kalo udah quantity++ 
			

			Product product;
			try {
				product = productRepository.findById(product_id).get();
			} catch (Exception e) {
				throw new ProductNotFoundException("Product not found");
			}
		
			
			User user;
			try {
				user = userRepository.findById(user_id).get();
			} catch (Exception e) {
				throw new UserIdNotFoundException("User not found");
			}
		
			invoice = invoiceRepository.findByInvoiceIdentifier(invoice.getInvoiceIdentifier());
			if(invoice == null) {
				throw new InvoiceNotFoundException("invoice not found");
			}
			
			int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
			int totalForPaid = 0;
			
			Invoice tempInvoice = null;
			
			Iterator<CartDetail> it = product.getCart_detail().iterator();
		
			if(!it.hasNext()) {
				
				it = invoice.getCart_detail().iterator();
				invoice.setInvoiceIdentifier(user.getInvoiceNow());
				
				int stock = product.getProductStock();
				stock--;
				if(stock < 0) {
					System.out.println("out of stock");
				}
				product.setProductStock(stock);

			}
			
			while(it.hasNext()){
				CartDetail c = it.next();
				
				if(c.getInvoice().getId().equals(invoice.getId()) 
						&&  c.getProduct().getId().equals(product.getId())) {
					
					int stock = product.getProductStock();
					stock--;
					if(stock < 0) {
						System.out.println("out of stock");

						return null;
					}
					product.setProductStock(stock);
					
					//set product nya ke invoice jg
//					invoice.setiProductName(c.getProduct().getProductName());
					
					c.setProductName(c.getProduct().getProductName());
					c.setQuantity(c.getQuantity()+1);
					c.setTotalToPaid(totalForPaid + c.getProduct().getProductPrice() * c.getQuantity());
					c.setStatus("Not Paid");
					c.setP_id(product_id);
					cartDetailRepository.save(c);
					flag = 0;
					tempInvoice = c.getInvoice();
					break;
				}
			}
			
			//create cart detail kalo belom pernah di add ke cart
			if(flag == 1) {
				CartDetail currDetail= new CartDetail(invoice,product);
				currDetail.setInvoiceIdentifier(user.getInvoiceNow());
				invoice.setInvoiceIdentifier(user.getInvoiceNow());
				currDetail.setQuantity(1);
				currDetail.setTotalToPaid(totalForPaid + product.getProductPrice());
				currDetail.setProductName(product.getProductName());
				currDetail.setInvoice(invoice);
				currDetail.setProduct(product);
				cartDetailRepository.save(currDetail);

				// add cart detail ke cart
				invoice.getCart_detail().add(currDetail);
				product.getCart_detail().add(currDetail);
				productRepository.save(product);
				tempInvoice = invoiceRepository.save(invoice);
				
			}
			return tempInvoice;
		
	}
	
	
}

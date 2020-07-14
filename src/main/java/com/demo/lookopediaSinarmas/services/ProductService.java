package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.ProductNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.InvoiceRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	CartDetailRepository cartDetailRepository;
	
//	public Invoice findInvoiceNow(Long user_id) {
//		User user = userRepository.findById(user_id).get();
//		Invoice invoiceSeq = invoiceRepository.findSequenceByUserId(user_id);
//		
//		user.setInvoiceNow("inv" + user_id +"-" + invoiceSeq);
//		return invoiceSeq;
//	}
	
	public Invoice addProductToCartDetailOrAddQty(Long product_id, Long user_id, Invoice invoice) {
		
		try {
			//check status cart 1. paid = generate new cart, 
			//if invoice null || invoice.status != paid
			//
			
			//check cart ini udh prnh bkin invoice-n blm
			//kalo blom bkin inv1-1, inv1-2 ( 1- = user 1 )

			//case : item di delete smua(prevent bikin invoice 1-2) <= after /buy
			//hasInvoice? boolean
			
//		Invoice invoice = new Invoice(cart.getId(), sequenceInvoce);
			
			
			
			/*
			 * product prtama di add ke cart? kalo udah quantity++ 
			 * */

			Product product = productRepository.findById(product_id).get();
			User user = userRepository.findById(user_id).get();
			
			//!!! find by inv-ident for first time
			invoice = invoiceRepository.findById(invoice.getId()).get();	
//		invoice.setInvoiceIdentifier("Inv" + user.getId() +"-" + invoice.getInvoiceSequence());
			
			int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
			int totalForPaid = 0;
			
			Invoice tempInvoice = null;
			
			Iterator<CartDetail> it = product.getCart_detail().iterator();
//		Iterator<CartDetail> ic = invoice.getCart_detail().iterator();
			
			if(!it.hasNext()) {
				it = invoice.getCart_detail().iterator();
				invoice.setInvoiceIdentifier("Inv" + user.getId() +"-" + user.getInvoiceSequence());
				int stock = product.getProductStock();
				stock--;
				if(stock < 0) {
					System.out.println("out of stock");
				}
				product.setProductStock(stock);
				
			}
			
			
			
			while(it.hasNext()){//validasi kalo stock gblh kurang dr db
				CartDetail c = it.next();
				
				if(c.getInvoice().getId().equals(invoice.getId()) 
						&&  c.getProduct().getId().equals(product.getId())) {
					
					int stock = product.getProductStock();
					stock--;
					if(stock < 0) {
						System.out.println("out of stock");
//					break;
						return null;
					}
					product.setProductStock(stock);
//				i.setiProductName(c.getProduct().getProductName());
					
					//set product nya ke invoice jg
//				invoice.setiProductName(c.getProduct().getProductName());
					
					c.setProductName(c.getProduct().getProductName());
					c.setQuantity(c.getQuantity()+1);
					c.setTotalToPaid(totalForPaid + c.getProduct().getProductPrice() * c.getQuantity());
					cartDetailRepository.save(c);
					flag = 0;
					tempInvoice = c.getInvoice();
					break;
				}
			}
			
			//create cart detail kalo belom pernah di add ke cart
			if(flag == 1) {
				CartDetail currDetail= new CartDetail(invoice,product);
				currDetail.setInvoiceIdentifier("Inv" + user.getId() +"-" + user.getInvoiceSequence());
				invoice.setInvoiceIdentifier("Inv" + user.getId() +"-" + user.getInvoiceSequence());
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
		} catch (Exception e) {
			throw new ProductNotFoundException("Product not found");
		}
		
	}
	
	public Product createProduct(Long merchantId, Product product, String merchantName) {

		try {
		    Merchant merchant = merchantRepository.findById(merchantId).get();
//		    Merchant merchant1 = merchantRepository.findByMerchantName(merchantName);
		    
		    
		    product.setMerchant(merchant);
		    product.setMerchantName(merchant.getMerchantName());
		    product.setProductCategory(product.getProductCategory().toLowerCase());
		    
		    Integer totalProduct = merchant.getTotalProduct();
		    totalProduct++;		    
		    merchant.setTotalProduct(totalProduct);
		    
				
			return productRepository.save(product);
		} catch (Exception e) {
			throw new MerchantNotFoundException("merchant not found");
		}
	}
	
	
	public Product updateProduct(Long merchantId, Product product, String merchantName) {
		//when we trying to update the project that doesn't belong to that user
		//project != null, find by db id -> null

		if(product.getId() != null) {
			Product existingProduct = productRepository.findById(product.getId()).get();
			
			if(existingProduct != null && (!existingProduct.getMerchantName().equals(merchantName))) {
				throw new ProductNotFoundException("Product not found in your merchant");
			}else if (existingProduct == null) {
				throw new ProductNotFoundException("Product '" + product.getProductName() + "' cannot updated, because it doesn't exist");
			}
		}

		
		try {
		    Merchant merchant = merchantRepository.findById(merchantId).get();
		    	    
			product.setMerchant(merchant);
			product.setProductCategory(product.getProductCategory().toLowerCase());
			
			return productRepository.save(product);
		} catch (Exception e) {
			throw new MerchantNotFoundException("merchant not found");
		}

	}
	

	public Invoice processItem(String invoice_identifier, Invoice invoice) {	
		
//		CartDetail cartDetail = cartDetailRepository.findByInvoiceIdentifier(invoice_identifier);
		
		//find By invoice identifier
		invoice = invoiceRepository.findByInvoiceIdentifier(invoice_identifier);
		
		User user = userRepository.findById(invoice.getUser().getId()).get();
		
		Integer invSeq = 0;
		if(user.getInvoice() !=null) invSeq = user.getInvoiceSequence();
		invSeq++;
		user.setInvoiceSequence(invSeq);
		
		
		Invoice invoice1 = new Invoice();
		invoice1.setUser(user);
		invoice1.setInvoiceIdentifier(user.getInvoiceNow());
		return invoiceRepository.save(invoice1);
	}
	
	public Iterable<Product> findAllProducts() {
		return productRepository.findAll(); 
	}
	
	public List<Product> findAllProductsByMerchantId(Long id) {
		return productRepository.findAllProductsByMerchantId(id); 
	}
	
	public List<Product> findProductByCategory(String categoryName) {
		return productRepository.findProductsByProductCategory(categoryName.toLowerCase()); 
	}
	
	
	

	public void deleteProductById(Long product_id) {
		
		try {
			Product product = productRepository.findById(product_id).get();
			 productRepository.delete(product);	
		} catch (Exception e) {
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
	}
	
	
}

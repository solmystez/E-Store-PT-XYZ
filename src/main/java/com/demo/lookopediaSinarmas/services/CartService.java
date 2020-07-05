package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.CartRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;

@Service
public class CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartDetailRepository cartDetailRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MerchantRepository merchantRepository;

	public Cart addProductToCartOrAddQty(Long id, Cart cart) {
		
		//check cart ini udh prnh bkin invoice-n blm
		//kalo blom bkin inv1-1, inv1-2 ( 1- = user 1 )

		//case : item di delete smua(prevent bikin invoice 1-2) <= after /buy
		//hasInvoice? boolean
		
//		Invoice invoice = new Invoice(cart.getId(), sequenceInvoce);
		
		
		
		
		/*
		 * product prtama di add ke cart? kalo udah quantity++ 
		 * */
		Product product = productRepository.findById(id).get();
		cart = cartRepository.findById(cart.getId()).get();	
		
		int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
		int totalForPaid = 0;
		
		Cart tempCart = null;
		
		Iterator<CartDetail> it = product.getCart_detail().iterator();
//		Iterator<CartDetail> ic = invoice.getCart_detail().iterator();
		
		if(!it.hasNext()) {
			it = cart.getCart_detail().iterator();
			int stock = product.getProductStock();
			stock--;
			if(stock < 0) {
				System.out.println("out of stock");
			}
			product.setProductStock(stock);
		}
		
		
		Invoice i = null;
		while(it.hasNext()){//validasi kalo stock gblh kurang dr db
			CartDetail c = it.next();
			
			if(c.getCart().getId().equals(cart.getId()) 
					&&  c.getProduct().getId().equals(product.getId())) {
				
				int stock = product.getProductStock();
				stock--;
				if(stock < 0) {
					System.out.println("out of stock");
//					break;
					return null;
				}
				product.setProductStock(stock);
				i.setiProductName(c.getProduct().getProductName());
				
				//set product nya ke invoice jg
//				invoice.setiProductName(c.getProduct().getProductName());
				
				c.setProductName(c.getProduct().getProductName());
				c.setQuantity(c.getQuantity()+1);
				c.setTotalToPaid(totalForPaid + c.getProduct().getProductPrice() * c.getQuantity());
				cartDetailRepository.save(c);
				flag = 0;
				tempCart = c.getCart();
				break;
			}
		}
		
		//create cart detail kalo belom pernah di add ke cart
		if(flag == 1) {
			CartDetail currDetail= new CartDetail(cart,product);
			currDetail.setQuantity(1);
			currDetail.setTotalToPaid(totalForPaid + product.getProductPrice());
			currDetail.setProductName(product.getProductName());
			currDetail.setCart(cart);
			currDetail.setProduct(product);
			cartDetailRepository.save(currDetail);

			// add cart detail ke cart
			cart.getCart_detail().add(currDetail);
			product.getCart_detail().add(currDetail);
			productRepository.save(product);
			tempCart = cartRepository.save(cart);
			
		}
		return tempCart;
		
	}
	//load all item before go to /buy
	public List<CartDetail> getCartDetailByCartId(Long cart_id) {
		return cartDetailRepository.findAllByCartId(cart_id);
	}
	
	
	public Cart subQtyProductFromCartId(Long id, Cart cart) {
		/*
		 * product prtama di add ke cart? kalo udah quantity++ 
		 * */
		Product product = productRepository.findById(id).get();
		cart = cartRepository.findById(cart.getId()).get();

		int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
		int totalForPaid = 0;
		
		Cart tempCart = null;
		
		Iterator<CartDetail> it = product.getCart_detail().iterator();
		if(!it.hasNext()) {
			it = cart.getCart_detail().iterator();
			int stock = product.getProductStock();
			stock++;
			if(stock < 0) {
				System.out.println("out of stock");
			}
			product.setProductStock(stock);
		}
		
		while(it.hasNext()){//coba kurangin stock dr sini, validasi kalo stock gblh kurang dr db
			CartDetail c = it.next();
			if(c.getCart().getId().equals(cart.getId()) 
					&&  c.getProduct().getId().equals(product.getId())) {
				
				int stock = product.getProductStock();
				stock++;
				if(stock < 0) {
					System.out.println("out of stock");
					break;
				}
				product.setProductStock(stock);
				
				c.setProductName(c.getProduct().getProductName());
				c.setQuantity(c.getQuantity()-1);
				c.setTotalToPaid(totalForPaid + c.getProduct().getProductPrice() * c.getQuantity());
				cartDetailRepository.save(c);
				flag = 0;
				tempCart = c.getCart();
				break;
			}
		}
		
		//create cart detail kalo belom pernah di add ke cart
		if(flag == 1) {
			CartDetail currDetail= new CartDetail(cart,product);
			currDetail.setQuantity(1);
			currDetail.setTotalToPaid(totalForPaid + product.getProductPrice());
			currDetail.setProductName(product.getProductName());
			currDetail.setCart(cart);
			currDetail.setProduct(product);
			cartDetailRepository.save(currDetail);

			// add cart detail ke cart
			cart.getCart_detail().add(currDetail);
			product.getCart_detail().add(currDetail);
			productRepository.save(product);
			tempCart = cartRepository.save(cart);
			
		}
		return tempCart;
		
	}
	//loadAllItemWantToBuy
	@Transactional
	public CartDetail processItem(Long cart_id, CartDetail cartDetail) {	

		//1. generate new invoice
		
		//2. assign attribute from cart_id(price)
		
		
		cartDetail = cartDetailRepository.findByCartId(cart_id);
		
		
		
		//3. Loop : find item_id from cart_id
		//get item_id_stock, getStock
		
		//save
		
		
		
		return null;
		
	}
	
	
	
}

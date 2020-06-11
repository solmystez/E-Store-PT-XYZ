package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.CartDetail;
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
		
		/*
		 * jadi kalo ada product baru buat cart detail kalo belum
		 * pernah di add ke cart kalo udah quantity++ 
		 * */
		Product product = productRepository.findById(id).get();
		cart = cartRepository.findById(cart.getId()).get();

		int flag = 1;
		// cek udah ada di cart ga, kalo udah ada quantity + 1
			
		Cart tempCart = null;
		
		Iterator<CartDetail> it = product.getCart_detail().iterator();
		if(!it.hasNext()) it = cart.getCart_detail().iterator();
		
		while(it.hasNext()){
			CartDetail c = it.next();
			if(c.getCart().getId().equals(cart.getId()) 
					&&  c.getProduct().getId().equals(product.getId())) {
				c.setQuantity(c.getQuantity()+1);
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

	public Cart removeProductFromCart(Long id, Cart cart) {
		
		Product product = productRepository.findById(id).get();
		cart = cartRepository.findById(cart.getId()).get();
		
		// klo di remove sampe 0 gmn
		// cek udah ada di cart ga, kalo udah ada quantity - 1
		// if qtt = 0, remove from cartDetail
			
		Cart tempCart = null;
		
		Iterator<CartDetail> it = product.getCart_detail().iterator();
		if(!it.hasNext()) it = cart.getCart_detail().iterator();
		
		while(it.hasNext()){
			CartDetail c = it.next();
			if(c.getCart().getId().equals(cart.getId()) &&  c.getProduct().getId().equals(product.getId())) {
				c.setQuantity(c.getQuantity()-1);
				cartDetailRepository.save(c);
				tempCart = c.getCart();
				break;
			}

		}
		
		return tempCart;
	}

	//load all item before go to /buy
	public List<CartDetail> getCartDetailByCartId(Long cart_id) {
		return cartDetailRepository.findByCartId(cart_id);
	}

}

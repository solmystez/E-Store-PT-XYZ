package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Cart;
import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.order.OrderNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductStockLimitException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class CartService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CartRepository cartDetailRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	//load all item before go to /buy
	public List<Cart> getCartDetailByOrderIdentifier(String order_now) {
		
		List<Cart> cartDetail = null;
		cartDetail = cartDetailRepository.findAllByOrderIdentifier(order_now);

		return cartDetail;
	}
	
	//delete product on cart, not delete product
	@Transactional
	public List<Cart> removeProductFromCart(Long product_id, String orderIdentifier) {

		try { 		
			cartDetailRepository.deleteCartDetailByOrderIdentifierAndProductId(orderIdentifier, product_id);
		} catch (Exception e) {
//			System.err.println(e);
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
		return cartDetailRepository.findAllByOrderIdentifier(orderIdentifier);
	
	}
	
	public Orders addProductToCartOrAddQty(Long product_id, Long user_id, Orders order) {
				
		
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
	
		order = orderRepository.findByOrderIdentifier(order.getOrderIdentifier());
		if(order == null) {
			throw new OrderNotFoundException("order not found");
		}
		
		int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
		
		Orders tempOrder = null;
		
		Iterator<Cart> it = product.getCart_detail().iterator();
		
		if(!it.hasNext()) {
			
			it = order.getCart_detail().iterator();
			order.setOrderIdentifier(user.getTrackOrder());

		}
		
		while(it.hasNext()){
			Cart c = it.next();
			
			if(c.getOrder().getId().equals(order.getId()) 
					&&  c.getProduct().getProduct_id().equals(product.getProduct_id())) {
				
				if(c.getQuantity() >= product.getProductStock()) {
					throw new ProductStockLimitException("Product stock only left : " + product.getProductStock());
				}
				c.setQuantity(c.getQuantity()+1);	
				c.setP_id(product.getProduct_id());
				c.setP_name(product.getProductName());
				c.setP_price(product.getProductPrice());
				c.setP_qty(product.getProductStock());
				c.setP_description(product.getProductDescription());
				cartDetailRepository.save(c);
				flag = 0;
				tempOrder = c.getOrder();
				break;
			}
		}
		
		//create cart detail kalo belom pernah di add ke cart
		if(flag == 1) {
			Cart currDetail= new Cart(order,product);
			currDetail.setOrderIdentifier(user.getTrackOrder());
			order.setOrderIdentifier(user.getTrackOrder());
			currDetail.setQuantity(1);
			currDetail.setOrder(order);
			currDetail.setProduct(product);
			currDetail.setP_id(product_id);
			currDetail.setP_name(product.getProductName());
			currDetail.setP_price(product.getProductPrice());
			currDetail.setP_qty(product.getProductStock());
			currDetail.setP_description(product.getProductDescription());
			cartDetailRepository.save(currDetail);

			// add cart detail ke cart
			order.getCart_detail().add(currDetail);
			product.getCart_detail().add(currDetail);
			productRepository.save(product);
			tempOrder = orderRepository.save(order);
			
		}
		return tempOrder;
		
	}
	
	public Orders subProductInCart(Long product_id, Long user_id, Orders order) {
		

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
	
		order = orderRepository.findByOrderIdentifier(order.getOrderIdentifier());
		if(order == null) {
			throw new OrderNotFoundException("order not found");
		}
		
		int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
		
		Orders tempOrder = null;
		
		Iterator<Cart> it = product.getCart_detail().iterator();
		
		if(!it.hasNext()) {
			
			it = order.getCart_detail().iterator();
			order.setOrderIdentifier(user.getTrackOrder());

		}
		
		while(it.hasNext()){
			Cart c = it.next();
			
			if(c.getOrder().getId().equals(order.getId()) 
					&&  c.getProduct().getProduct_id().equals(product.getProduct_id())) {
				
				if(c.getQuantity() <= 0) {
					throw new ProductStockLimitException("Product must at least 1 pcs");
				}
				c.setQuantity(c.getQuantity()-1);	
				c.setP_id(product.getProduct_id());
				c.setP_name(product.getProductName());
				c.setP_price(product.getProductPrice());
				c.setP_qty(product.getProductStock());
				c.setP_description(product.getProductDescription());
				cartDetailRepository.save(c);
				flag = 0;
				tempOrder = c.getOrder();
				break;
			}
		}
		
		//create cart detail kalo belom pernah di add ke cart
		if(flag == 1) {
			Cart currDetail= new Cart(order,product);
			currDetail.setOrderIdentifier(user.getTrackOrder());
			order.setOrderIdentifier(user.getTrackOrder());
			currDetail.setQuantity(1);
			currDetail.setOrder(order);
			currDetail.setProduct(product);
			currDetail.setP_id(product_id);
			currDetail.setP_name(product.getProductName());
			currDetail.setP_price(product.getProductPrice());
			currDetail.setP_qty(product.getProductStock());
			currDetail.setP_description(product.getProductDescription());
			cartDetailRepository.save(currDetail);

			// add cart detail ke cart
			order.getCart_detail().add(currDetail);
			product.getCart_detail().add(currDetail);
			productRepository.save(product);
			tempOrder = orderRepository.save(order);
			
		}
		return tempOrder;
	}
	
}

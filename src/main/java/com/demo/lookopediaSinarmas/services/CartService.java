package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Order;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.OrderNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class CartService {

	@Autowired
	OrderRepository orderRepository;

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
	public List<CartDetail> getCartDetailByOrderIdentifier(String order_now) {
		
		List<CartDetail> cartDetail = null;
	
		cartDetail = cartDetailRepository.findAllByOrderIdentifier(order_now);

		return cartDetail;
	}
	
	//delete product on cart, not delete product
	@Transactional
	public List<CartDetail> removeProductFromCart(Long product_id, String orderIdentifier) {

		try {
		   cartDetailRepository.deleteCartDetailByOrderIdentifierAndProductId(orderIdentifier, product_id);
		} catch (Exception e) {
			System.err.println(e);
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
		return cartDetailRepository.findAllByOrderIdentifier(orderIdentifier);
	
	}
	
	public Order addProductToCartOrAddQty(Long product_id, Long user_id, Order order) {
		

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
			int totalForPaid = 0;
			
			Order tempOrder = null;
			
			Iterator<CartDetail> it = product.getCart_detail().iterator();
		
			if(!it.hasNext()) {
				
				it = order.getCart_detail().iterator();
				order.setOrderIdentifier(user.getTrackOrder());
				
				int stock = product.getProductStock();
				stock--;
				if(stock < 0) {
					System.out.println("out of stock");
				}
				product.setProductStock(stock);

			}
			
			while(it.hasNext()){
				CartDetail c = it.next();
				
				if(c.getOrder().getId().equals(order.getId()) 
						&&  c.getProduct().getId().equals(product.getId())) {
					
					int stock = product.getProductStock();
					stock--;
					if(stock < 0) {
						System.out.println("out of stock");

						return null;
					}
					product.setProductStock(stock);
					
					//set product nya ke order jg
//					order.setiProductName(c.getProduct().getProductName());
					
					c.setProductName(c.getProduct().getProductName());
					c.setQuantity(c.getQuantity()+1);
					c.setTotalToPaid(totalForPaid + c.getProduct().getProductPrice() * c.getQuantity());
					c.setStatus("Not Paid");
					c.setP_id(product_id);
					cartDetailRepository.save(c);
					flag = 0;
					tempOrder = c.getOrder();
					break;
				}
			}
			
			//create cart detail kalo belom pernah di add ke cart
			if(flag == 1) {
				CartDetail currDetail= new CartDetail(order,product);
				currDetail.setOrderIdentifier(user.getTrackOrder());
				order.setOrderIdentifier(user.getTrackOrder());
				currDetail.setQuantity(1);
				currDetail.setTotalToPaid(totalForPaid + product.getProductPrice());
				currDetail.setProductName(product.getProductName());
				currDetail.setOrder(order);
				currDetail.setProduct(product);
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

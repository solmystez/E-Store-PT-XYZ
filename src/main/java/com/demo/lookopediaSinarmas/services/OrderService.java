package com.demo.lookopediaSinarmas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Order;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.OrderNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartDetailRepository cartDetailRepository;
	
	@Autowired 
	UserService userService;
	
	
	public Order processItem(String order_identifier, Long user_id) {	
		
		Order order = null;
		User user = null;
		
		order = orderRepository.findByOrderIdentifier(order_identifier);
		if(order == null) {
			throw new OrderNotFoundException("order not found");
		}
		
		user = userRepository.findById(order.getUser().getId()).get();
	
		Integer invSeq = 0;
		if(user.getOrder() != null) invSeq = user.getOrderSequence();
		invSeq++;
		user.setOrderSequence(invSeq);
		
		CartDetail cartDetail = cartDetailRepository.findByOrderIdentifier(order_identifier);

		Integer merchantBal = 0;

		
//		merchant.setMerchantBalance(cartDetail.getTotalToPaid());
					
		userService.applyInvoiceNow(user_id, user);

		return orderRepository.save(order);

	}

	public List<Order> loadAllInvoiceByUserId(Long user_id) {
		return orderRepository.findAllByUserId(user_id);
	}
	
}

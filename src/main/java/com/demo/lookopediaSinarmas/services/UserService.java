package com.demo.lookopediaSinarmas.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Order;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.EmailAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired//comes up with spring security
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveOrUpdateUser(User user) {
		try {
			//1. Username has to be unique(custom exception)
			
			//2. make sure password and confirmPassword match
			//we don't persist or show the confirmPassword
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setConfirmPassword("");//for postman result, we don't need another encode
			
			
			if(user.getId() == null) {//create
				
			}
			
			if(user.getId() != null) {//update
				//user.setMerchant(merchantRepository.findByUserId(user.getId()));
			}
			
			return userRepository.save(user);
		} catch (Exception e) {
			throw new EmailAlreadyExistsException(user.getEmail() + " already exists");
		}
		
	}
	
	
	public User findUserById(Long id) {
		User user;
		
		try {
			user = userRepository.findById(id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User Id '" + id + "' doesn't exist");
		}
		
		return user;
	}


	public Iterable<User> findAllUsers() {
		return userRepository.findAll();
	}
	
	public User applyInvoiceNow(Long user_id, User user) {
		
		try {
			
			//1. find user id
			user = userRepository.findById(user_id).get();
			user.setOrderNow("inv" + user.getId() + "-" + user.getInvoiceSequence());

			Set<Order> inv = user.getOrder();
			
//			System.out.println(inv);
//			System.out.println(user.getInvoiceNow());
			
			Order order = orderRepository.findByOrderIdentifier(user.getOrderNow());
			if(user.getOrderNow() == null  || order == null) {
				order = new Order();
				order.setUser(user);
				order.setOrderIdentifier("inv" + user.getId() + "-" + user.getInvoiceSequence());
								
				inv.add(order);
				user.setOrder(inv);
				
			}

//			Invoice invoice = invoiceRepository.findByUserId(user_id);//gblh
//			invoice.setInvoiceNow("inv" + user.getId() + "-" + user.getInvoiceSequence());
			
			return userRepository.save(user);
		} catch (Exception e) {
			throw new UserIdNotFoundException("User Id '" + user_id + "' doesn't exist");
		}
	}
	
	public Order applyInvoiceIdentifier(String invoiceNow, Order order) {
		
//		invoice = invoiceRepository.findByInvoiceNow(invoiceNow);
//		
//		invoice.setInvoiceIdentifier(invoiceNow);
		return orderRepository.save(order);
	}


	public User addUserAddress(Long user_id, User user) {
		
		return null;
	}
}

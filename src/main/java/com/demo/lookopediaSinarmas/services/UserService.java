package com.demo.lookopediaSinarmas.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Address;
import com.demo.lookopediaSinarmas.domain.Orders;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.email.EmailAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.UserAddressRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserAddressRepository userAddressRepository;
	
	@Autowired//comes up with spring security
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveOrUpdateUser(User user) {
		try {
			//1. Username has to be unique(custom exception)
			
			//2. make sure password and confirmPassword match
			//we don't persist or show the confirmPassword
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setConfirmPassword("");//for postman result, we don't need another encode
			
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
			user.setTrackOrder("ord" + user.getId() + "-" + user.getOrderSequence());

			Set<Orders> ord = user.getOrder();
			
//			System.out.println(inv);
//			System.out.println(user.getInvoiceNow());
			
			Orders order = orderRepository.findByOrderIdentifier(user.getTrackOrder());
			if(user.getTrackOrder() == null  || order == null) {
				order = new Orders();
				order.setUser(user);
				order.setOrderIdentifier("ord" + user.getId() + "-" + user.getOrderSequence());
								
				ord.add(order);
				user.setOrder(ord);
				
			}
			
			return userRepository.save(user);
		} catch (Exception e) {
			throw new UserIdNotFoundException("User Id '" + user_id + "' doesn't exist");
		}
	}
	
	public Orders applyOrderIdentifier(String invoiceNow, Orders order) {
		
//		invoice = invoiceRepository.findByInvoiceNow(invoiceNow);
//		
//		invoice.setInvoiceIdentifier(invoiceNow);
		return orderRepository.save(order);
	}


	public Address addUserAddress(Long user_id, Address user_address) {
		
		User user = userRepository.findById(user_id).get();
		
//		Address newAdr = new Address();
		
		user_address.setUserAddress(user);
		
		user_address.setAddress_label(user_address.getAddress_label());
		
		return userAddressRepository.save(user_address);
	}
}
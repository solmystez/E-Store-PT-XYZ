package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.EmailAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
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
				
				//set relasi user with cart
				Cart cart = new Cart(); 
				user.setCart(cart);
				cart.setUser(user);
			}
			
			//bug : kalo langsung update id yg ga ad, user ke create tanpa punya cart
			if(user.getId() != null) {//update
				//user.setMerchant(merchantRepository.findByUserId(user.getId()));
			}
			
			return userRepository.save(user);
		} catch (Exception e) {
			throw new EmailAlreadyExistsException("Email : '" + user.getEmail() + "' already exists");
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
	

}

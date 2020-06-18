package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.User;
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
	
	public User saveOrUpdateUser(User user) {

		if(user.getId() == null) {//create
			
			//set relasi user with cart
			Cart cart = new Cart(); 
			user.setCart(cart);
			cart.setUser(user);
		}
		
		//bug : kalo langsung update id yg ga ad, user ke create tanpa punya cart
		if(user.getId() != null) {//update
//				user.setMerchant(merchantRepository.findByUserId(user.getId()));
		}
		
		return userRepository.save(user);
		
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

package com.demo.lookopediaSinarmas.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.UserIdException;
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
				
				Merchant merchant = new Merchant();
				user.setMerchant(merchant);
				merchant.setUser(user);
				
				//set relasi user with cart
				Cart cart = new Cart();
//				user.setCart(cart);
				cart.setUser(user);
			}
			
			//bug : kalo langsung update id yg ga ad, user ke create tanpa punya cart
			//create exception utk user yg ga ad
			if(user.getId() != null) {//update
//				user.setMerchant(merchantRepository.findByUserId(user.getId()));
			}
			
			return userRepository.save(user);
		
	}
	
	
	
}

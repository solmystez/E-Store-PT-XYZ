package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Merchant;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNameAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;


@Service
public class MerchantService {
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public Merchant createMerchant(Long user_id, Merchant merchant, String username) {
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e1) {
			throw new UserIdNotFoundException("User not found");
		}
		
		
		try {
			user.setHasMerchant(true);
			
			user.setMerchant(merchant);
			merchant.setUserMerchant(user);
			merchant.setUserName(user.getUsername());
			
			if(merchant.getId() != null) {
				merchant.setUserMerchant(userRepository.findById(user_id).get());
			}
			
			return merchantRepository.save(merchant);
		} catch (Exception e) {
			throw new MerchantNameAlreadyExistsException("Merchant name already used !");
		}
		
	}
	
	public Merchant findMerchantByUserId(Long id) {
		Merchant merchant;
		
		try {
			merchant = merchantRepository.findMerchantByUserMerchantId(id);
		} catch (Exception e) {
			throw new UserIdNotFoundException("User Id '" + id + "' doesn't exist");
		}
		
		return merchant;
	}
	
	
	

}

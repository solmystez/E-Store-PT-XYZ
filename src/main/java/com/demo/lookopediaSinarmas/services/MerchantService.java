package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.MerchantNameAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;


@Service
public class MerchantService {
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public Merchant createMerchant(Long user_id, Merchant merchant, String username) {
		
		try {
			User user = userRepository.findById(user_id).get();
			user.setHasMerchant(true);
			
			user.setMerchant(merchant);
			merchant.setUserMerchant(user);
			
			return merchantRepository.save(merchant);
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
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

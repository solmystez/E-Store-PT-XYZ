package com.demo.lookopediaSinarmas.services;

import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;


@Service
public class MerchantService {
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public Merchant createMerchant(Long user_id, Merchant merchant) {
		
		try {
			User user = userRepository.findById(user_id).get();
			
			user.setMerchant(merchant);
			merchant.setUser(user);
			
			return merchantRepository.save(merchant);
		} catch (Exception e) {
			throw new UserIdNotFoundException("wrong id user, check your url");
		}
		
	}
	
	

}

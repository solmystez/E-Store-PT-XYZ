package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;

@Service
public class MerchantService {
	
	@Autowired
	private MerchantRepository merchantRepository;
	
	public Merchant addProduct(Long merchantId, Product product) {
		//useCase : we didn't create product that don't belong to nothing
		
		Merchant merchant = merchantRepository.findById(merchantId).get();
		//merchant != null == product exists, then set product to merchant
		product.setMerchant(merchant);
		
		return null;
	}
	
	
}

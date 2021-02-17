package com.demo.eStore.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.demo.eStore.entity.Merchant;

@Component
public class MerchantValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Merchant.class.equals(clazz);//this means support Product Entity class
	}

	@Override
	public void validate(Object object, Errors errors) {
		// TODO Auto-generated method stub
		Merchant merchant = (Merchant) object;
		
		if(merchant.getMerchantName().equals("")) {
			errors.rejectValue("merchantName", "Empty", "Merchant Name cannot be empty !");
		}
		if(merchant.getMerchantAddress().equals("")) {
			errors.rejectValue("merchantAddress", "Empty", "Merchant address cannot be empty !");
		}
	}

}

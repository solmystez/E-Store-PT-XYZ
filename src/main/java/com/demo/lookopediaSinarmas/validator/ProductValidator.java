package com.demo.lookopediaSinarmas.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.demo.lookopediaSinarmas.entity.Product;

@Component
public class ProductValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Product.class.equals(clazz);//this means support Product Entity class
	}
	
	@Override
	public void validate(Object object, Errors errors) {
		// TODO Auto-generated method stub
		Product product = (Product) object;
		
		if(product.getProductName().equals("")) {
			errors.rejectValue("productName", "Empty", "Product Name cannot be empty !");
		}
		if(product.getProductCategoryName().equals("undefined")) {
			errors.rejectValue("productCategoryName", "Empty", "please select product Category");
		}
		if(product.getProductDescription().length() < 10) {
			errors.rejectValue("productDescription", "Lenght", "Product Description at least 10 characters !");
		}
//		ValidationUtils.rejectIfEmpty(errors, "productName", "field product name required");
		
//		if(StringUtils.isEmpty(product.getProductPrice())) {
//			errors.rejectValue("productPrice", "Invalid Price", "Product price cannot be empty !");
//		}
//		
//		if(product.getProductPrice() < 0 || product.getProductPrice() > 999999999) {
//			errors.rejectValue("productPrice", "Invalid Price", "Product price range is 0 - 999.999.999");
//		}
		
//		if(product.getProductName().length() < 2) {
//			//password <= atribute from User entity(must same in parameter 1)
//			errors.rejectValue("productName", "Lenght", "product name must be at least 2 characters");
//		}
		
//		if(!user.getPassword().equals(user.getConfirmPassword())) {
//			errors.rejectValue("confirmPassword", "Match", "Password must be match !");
//		}
		
	}
}

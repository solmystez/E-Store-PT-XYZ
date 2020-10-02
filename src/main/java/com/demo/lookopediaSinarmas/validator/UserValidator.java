package com.demo.lookopediaSinarmas.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.demo.lookopediaSinarmas.entity.User;

@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return User.class.equals(clazz);//this means support User Entity class
	}

	@Override
	public void validate(Object object, Errors errors) {
		// TODO Auto-generated method stub
		User user = (User) object;
		
		if(user.getPassword().length() < 6) {
			//password <= atribute from User entity(must same in parameter 1)
			errors.rejectValue("password", "Lenght", "Password must be at least 6 characters");
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "Match", "Password must be match !");
		}
		
	}
}

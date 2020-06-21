package com.demo.lookopediaSinarmas.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.repositories.UserRepository;
import com.demo.lookopediaSinarmas.services.MapValidationErrorService;
import com.demo.lookopediaSinarmas.services.UserService;
import com.demo.lookopediaSinarmas.validator.UserValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private UserValidator userValidator;
	
	//1. create or update user
	//our app is lockdown because spring security
	@PostMapping("/storeUserToDatabase")//but this route, shouldn't need a password, addRoute permitAll for this route in class SecurityConfig
	public ResponseEntity<?> createOrUpdateUser(@Valid @RequestBody User user, BindingResult result){//@valid for get better response view
	
		userValidator.validate(user, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		User user1 = userService.saveOrUpdateUser(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}
	
	//2. find user 
	@GetMapping("/getUserInfo/{user_id}")
	public ResponseEntity<?> findUser(@PathVariable Long user_id) {
		
		User user = userService.findUserById(user_id);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	//3. delete user
	
}

package com.demo.lookopediaSinarmas.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.services.MapValidationErrorService;
import com.demo.lookopediaSinarmas.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private MapValidationErrorService mapValidationErrorService;
	
	//1. create or update user
	@PostMapping("/storeUser")
	public ResponseEntity<?> createOrUpdateUser(@Valid @RequestBody User user, BindingResult result){//@valid for get better response view
	
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		User user1 = userService.saveOrUpdateUser(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}
	
	//2. find user 
	
	
	//3. delete user
	
}

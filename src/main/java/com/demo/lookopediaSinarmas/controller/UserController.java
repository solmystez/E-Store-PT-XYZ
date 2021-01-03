package com.demo.lookopediaSinarmas.controller;

import static com.demo.lookopediaSinarmas.security.SecurityConstants.TOKEN_PREFIX;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.entity.Address;
import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.payload.JWTLoginSuccessResponse;
import com.demo.lookopediaSinarmas.payload.LoginRequest;
import com.demo.lookopediaSinarmas.security.JwtTokenProvider;
import com.demo.lookopediaSinarmas.services.UserService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;
import com.demo.lookopediaSinarmas.validator.UserValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	AuthenticationManager authenticationManager;//process authentication request
	
	//1. create or update user
	//our app is lockdown because spring security
	@PostMapping("/register")//but this route, shouldn't need a password, addRoute permitAll for this route in class SecurityConfig
	public ResponseEntity<?> createOrUpdateUser(@Valid @RequestBody User user, BindingResult result){
	
		userValidator.validate(user, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		User user1 = userService.saveUser(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}

	
	@GetMapping("/getUserInfo/{user_id}")
	public ResponseEntity<?> findUser(@PathVariable Long user_id) {
		
		User user = userService.findUserById(user_id);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/loadAllUser")
	public Iterable<User> loadAllProduct(){
		return userService.findAllUsers();
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		
		//org.springframework.security.core.Authentication;
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(), 
						loginRequest.getPassword())
				);
		//authenticated the user
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
		
		//if we get a valid username&pw, we get a token
		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
	}
	
	
	@PostMapping("/addAddress/{user_id}")	
	public ResponseEntity<?> addUserAddress(@Valid @RequestBody Address address,
			BindingResult result, @PathVariable Long user_id, Principal principal) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		//belom di block validate jwt filter
		Address address1 = userService.addUserAddress(user_id, address, principal.getName());
				
		return new ResponseEntity<Address>(address1, HttpStatus.CREATED);
	}
	
	@GetMapping("/loadAllAddress")
	public ResponseEntity<?> loadAllAddress(Principal principal){
		List<Address> address = userService.findAllAddressByUserId(principal.getName());
		return new ResponseEntity<List<Address>>(address, HttpStatus.OK);
	}
	
	@PatchMapping("/updateAddress/{user_id}")
	public ResponseEntity<?> updateAddress(@Valid @RequestBody Address address,
			BindingResult result, @PathVariable Long user_id, Principal principal) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		
		Address address1 = userService.updateUserAddress(user_id, address, principal.getName());
		return new ResponseEntity<Address>(address1, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/deleteAddress/{address_id}")
	public ResponseEntity<?> deleteAddress(@PathVariable Long address_id) {
		userService.deleteAddressById(address_id);
		return new ResponseEntity<String>("Address ID '" + address_id  + "' was successfully deleted", HttpStatus.OK);
	}
}

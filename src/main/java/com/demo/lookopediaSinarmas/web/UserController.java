package com.demo.lookopediaSinarmas.web;

import static com.demo.lookopediaSinarmas.security.SecurityConstants.TOKEN_PREFIX;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.Address;
import com.demo.lookopediaSinarmas.domain.Order;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.payload.JWTLoginSuccessResponse;
import com.demo.lookopediaSinarmas.payload.LoginRequest;
import com.demo.lookopediaSinarmas.security.JwtTokenProvider;
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
	public ResponseEntity<?> createOrUpdateUser(@Valid @RequestBody User user, BindingResult result){//@valid for get better response view
	
		userValidator.validate(user, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		User user1 = userService.saveOrUpdateUser(user);
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}
	
	@PostMapping("/trackOrder/{user_id}")	//next postMapping kena effect jwt filter after /login
	public ResponseEntity<?> trackOrder(@Valid @RequestBody User user,
			BindingResult result, @PathVariable Long user_id) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		//belom di block validate jwt filter
		User user1 = userService.applyInvoiceNow(user_id, user);
				
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}
	
	@PostMapping("/addAddress/{user_id}")	
	public ResponseEntity<?> addAddress(@Valid @RequestBody Address address,
			BindingResult result, @PathVariable Long user_id) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		//belom di block validate jwt filter
		Address address1 = userService.addUserAddress(user_id, address);
				
		return new ResponseEntity<Address>(address1, HttpStatus.CREATED);
	}
	
	
//	@PostMapping("/checkInvoiceIdentifier/{user_invoiceNow}")	//next postMapping kena effect jwt filter after /login
//	public ResponseEntity<?> applyInvoiceIdentifier(@Valid @RequestBody Invoice invoice,
//			BindingResult result, @PathVariable String user_invoiceNow) {
//		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//		if(errorMap != null) return errorMap;
//		//belom di block validate jwt filter
//		Invoice invoice1 = userService.applyInvoiceIdentifier(user_invoiceNow, invoice);
//				
//		return new ResponseEntity<Invoice>(invoice1, HttpStatus.CREATED);
//	}
	
	//2. find user 
	@GetMapping("/getUserInfo/{user_id}")
	public ResponseEntity<?> findUser(@PathVariable Long user_id) {
		
		User user = userService.findUserById(user_id);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/loadAllUser")
	public Iterable<User> loadAllProduct(){
		return userService.findAllUsers();
	}
	
	//3. delete user
	//??
	
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
	
}

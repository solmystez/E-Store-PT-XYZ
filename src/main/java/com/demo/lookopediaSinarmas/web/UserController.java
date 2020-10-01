package com.demo.lookopediaSinarmas.web;

import static com.demo.lookopediaSinarmas.security.SecurityConstants.TOKEN_PREFIX;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Base64;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.lookopediaSinarmas.domain.Address;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.email.EmailAlreadyExistsException;
import com.demo.lookopediaSinarmas.payload.JWTLoginSuccessResponse;
import com.demo.lookopediaSinarmas.payload.LoginRequest;
import com.demo.lookopediaSinarmas.repositories.UserRepository;
import com.demo.lookopediaSinarmas.security.JwtTokenProvider;
import com.demo.lookopediaSinarmas.services.ImageService;
import com.demo.lookopediaSinarmas.services.UserService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;
import com.demo.lookopediaSinarmas.validator.UserValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);
	public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired//comes up with spring security
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;//process authentication request
	
	//1. create or update user
	//our app is lockdown because spring security
	@PostMapping("/register")//but this route, shouldn't need a password, addRoute permitAll for this route in class SecurityConfig
	public ResponseEntity<?> createOrUpdateUser(@Valid @RequestBody User user, BindingResult result){
	
		userValidator.validate(user, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		User user1 = userService.saveOrUpdateUser(user);
		return new ResponseEntity<User>(user1,HttpStatus.CREATED);
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
	public ResponseEntity<?> addUserAddress(@Valid @RequestBody Address address,
			BindingResult result, @PathVariable Long user_id) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		//belom di block validate jwt filter
		Address address1 = userService.addUserAddress(user_id, address);
				
		return new ResponseEntity<Address>(address1, HttpStatus.CREATED);
	}
	
	@PostMapping("/saveProfilePicture/{user_id}")
	public ResponseEntity<?> saveProfilePicture(@PathVariable Long user_id,
			BindingResult result, @RequestParam("profilePicture") MultipartFile file) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		//belom di block validate jwt filter
		User user1 = userService.saveProfilePicture(user_id, file);
				
		return new ResponseEntity<User>(user1, HttpStatus.CREATED);
	}
	
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

package com.demo.lookopediaSinarmas.services;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.lookopediaSinarmas.controller.MerchantController;
import com.demo.lookopediaSinarmas.entity.Address;
import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.email.EmailAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.UserAddressRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;
import com.demo.lookopediaSinarmas.services.image.ImageStorageService;

@Service
public class UserService {
	
	private static Logger log = LoggerFactory.getLogger(MerchantController.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserAddressRepository userAddressRepository;
	
	@Autowired
	private ImageStorageService imageStorageService;
	
	@Autowired//comes up with spring security
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User user) {
		try {
			//1. Username has to be unique(custom exception)
			
			//2. make sure password and confirmPassword match
			//we don't persist or show the confirmPassword
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setConfirmPassword("");//for postman result, we don't need another encode
//			boolean checkUsername = userRepository.existsByUsername(user.getUsername());
//			boolean checkEmail = userRepository.existsByEmail(user.getEmail());
//			
//			if(checkUsername) throw new UsernameNotFoundException(user.getUsername() + " already exists");
//			if(checkEmail) throw new EmailAlreadyExistsException(user.getEmail() + " already exists");
			return userRepository.save(user);
		} catch (Exception e) {
			throw new EmailAlreadyExistsException(user.getEmail() + " already exists");
		}
		
	}
	
	public User updateUser(User user, Long user_id, MultipartFile file, String username) {
		try {
			if(user.getId() != null ) {
				try {
					user = userRepository.findById(user_id).get();
				} catch (Exception e) {
					throw new UserIdNotFoundException("User not found");
				}
			}
			String fileName = null;
			if(!file.isEmpty()) fileName = imageStorageService.storeFile(file);
			
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/api/user/loadImageUser/")
					.path(fileName)
					.toUriString();
			
			
			String userFileName = file.getOriginalFilename();
			String userFileType = file.getContentType();
			long size = file.getSize();
			String userFileSize = String.valueOf(size);
			
			user.setFileName(userFileName);
			user.setFilePath(fileDownloadUri);
			user.setFileType(userFileType);
			user.setFileSize(userFileSize);
			
			
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setConfirmPassword("");
			log.info("user updated");
			return userRepository.save(user);
		} catch (Exception e) {
			throw new EmailAlreadyExistsException(user.getEmail() + " already exists");
		}
		
	}
	
	public User findUserById(Long id) {
		User user;
		
		try {
			user = userRepository.findById(id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User Id '" + id + "' doesn't exist");
		}
		
		return user;
	}


	public Iterable<User> findAllUsers() {
		return userRepository.findAll();
	}
	
	public User applyInvoiceNow(Long user_id, User user) {
		
		try {
			
			//1. find user id
			try {
				user = userRepository.findById(user_id).get();
			} catch (Exception e) {
				throw new UserIdNotFoundException("User not found while trackOrder");
			}
			user.setTrackOrder("ord" + user.getId() + "-" + user.getOrderSequence());

			Set<Orders> ord = user.getOrder();
			
//			System.out.println(inv);
//			System.out.println(user.getInvoiceNow());
			
			Orders order = orderRepository.findByOrderIdentifier(user.getTrackOrder());
			if(user.getTrackOrder() == null  || order == null) {
				order = new Orders();
				order.setUser(user);
				order.setOrderIdentifier("ord" + user.getId() + "-" + user.getOrderSequence());
				order.setUsername(user.getUsername());
				ord.add(order);
				user.setOrder(ord);
				
			}
			
			return userRepository.save(user);
		} catch (Exception e) {
			throw new UserIdNotFoundException("User Id '" + user_id + "' doesn't exist(track order)");
		}
	}
	
	public Orders applyOrderIdentifier(String invoiceNow, Orders order) {
		
//		invoice = invoiceRepository.findByInvoiceNow(invoiceNow);
//		
//		invoice.setInvoiceIdentifier(invoiceNow);
		return orderRepository.save(order);
	}


	public Address addUserAddress(Long user_id, Address user_address) {
		
		User user = userRepository.findById(user_id).get();
		
//		Address newAdr = new Address();
		
		user_address.setUserAddress(user);
		
		user_address.setAddress_label(user_address.getAddress_label());
		
		return userAddressRepository.save(user_address);
	}
}
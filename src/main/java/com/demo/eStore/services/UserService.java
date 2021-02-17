package com.demo.eStore.services;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.eStore.controller.MerchantController;
import com.demo.eStore.entity.Address;
import com.demo.eStore.entity.Orders;
import com.demo.eStore.entity.Product;
import com.demo.eStore.entity.User;
import com.demo.eStore.exceptions.address.AddressNotFoundException;
import com.demo.eStore.exceptions.email.EmailAlreadyExistsException;
import com.demo.eStore.exceptions.product.ProductIdException;
import com.demo.eStore.exceptions.product.ProductNotFoundException;
import com.demo.eStore.exceptions.user.UserIdNotFoundException;
import com.demo.eStore.repositories.MerchantRepository;
import com.demo.eStore.repositories.OrderRepository;
import com.demo.eStore.repositories.UserAddressRepository;
import com.demo.eStore.repositories.UserRepository;
import com.demo.eStore.services.image.ImageStorageService;

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
	
	public Address addUserAddress(Long user_id, Address user_address, String username) {
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
		}
		user_address.setAddressLabel(user_address.getAddressLabel());
		user_address.setAddressCity(user_address.getAddressCity());
		user_address.setAddressCountry(user_address.getAddressCountry());
		user_address.setAddressDescription(user_address.getAddressDescription());
		user_address.setAddressPostalCode(user_address.getAddressPostalCode());
		user_address.setAddressProvince(user_address.getAddressProvince());
		user_address.setUserAddress(user);
		user_address.setUsername(username);
		return userAddressRepository.save(user_address);
	}
	
	public Address updateUserAddress(Long user_id, Address user_address, String username) {
		
//		if(user_address.getAddress_id() != null) {
//			Address existUserAddress = userAddressRepository.findById(user_address.getAddress_id()).get();
//			
//			if(existUserAddress != null && (!existUserAddress.getUsername().equals(username))) {
//				throw new ProductNotFoundException("Product not found in your merchant");
//			}else if (existUserAddress == null) {
//				throw new AddressNotFoundException("Address '" + existUserAddress.getAddressLabel() + "' cannot updated, because it doesn't exist");
//			}
//		}
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
		}
		user_address.setAddressLabel(user_address.getAddressLabel());
		user_address.setAddressCity(user_address.getAddressCity());
		user_address.setAddressCountry(user_address.getAddressCountry());
		user_address.setAddressDescription(user_address.getAddressDescription());
		user_address.setAddressPostalCode(user_address.getAddressPostalCode());
		user_address.setAddressProvince(user_address.getAddressProvince());
		user_address.setUserAddress(user);
		user_address.setUsername(username);
		return userAddressRepository.save(user_address);
	}

	public List<Address> findAllAddressByUserId(String username) {
		
		List<Address> addr;
		try {
			addr = userAddressRepository.findAllAddressByUsername(username);
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
		}
		
		return addr;
	}
	
	public void deleteAddressById(Long address_id) {
		try {
			Address address = userAddressRepository.findById(address_id).get();
			userAddressRepository.delete(address);
		} catch (Exception e) {
			throw new ProductIdException("Address with ID '" + address_id +"' cannot delete because doesn't exists");			
		}
	}
}
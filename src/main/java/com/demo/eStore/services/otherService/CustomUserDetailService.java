package com.demo.eStore.services.otherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.eStore.entity.User;
import com.demo.eStore.repositories.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{//actually if we not using implements in User.class, there still another way too

	@Autowired
	private UserRepository userRepository;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//extract what user data
		
		//User class, careful
		User user = userRepository.findByEmail(email);
		
		if(user == null) new UsernameNotFoundException("User not found");
		return user;
	}
	
	//we gonna load lot with userId
	//when we start filtering the token
	
	@Transactional//org.spring.framework
	public User loadUserById(Long id) {
		User user = userRepository.getById(id);
		
		if(user == null) new UsernameNotFoundException("User not found");
		return user;
	}
	
	
}

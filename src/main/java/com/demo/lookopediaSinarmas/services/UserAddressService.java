package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.repositories.UserAddressRepository;

@Service
public class UserAddressService {
	
	@Autowired
	UserAddressRepository userAddressRepository;
	
	
}

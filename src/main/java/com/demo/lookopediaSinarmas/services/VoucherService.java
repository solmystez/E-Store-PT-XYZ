package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.repositories.VoucherRepository;

@Service
public class VoucherService {
	
	@Autowired
	VoucherRepository voucherRepository;
	
	//apply discount to cart, get total_p from order by order ident
}

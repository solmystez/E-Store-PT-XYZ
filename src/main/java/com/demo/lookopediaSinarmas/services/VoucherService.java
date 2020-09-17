package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Voucher;
import com.demo.lookopediaSinarmas.repositories.VoucherRepository;

@Service
public class VoucherService {
	
	@Autowired
	VoucherRepository voucherRepository;
	
	//apply discount to cart, get total_p from order by order ident
	
	public Voucher addOrUpdateVoucher(Voucher voucher) {
		
		voucher.setVoucherCode(voucher.getVoucherCode());
		voucher.setVoucherName(voucher.getVoucherName());
		voucher.setVoucherDiscount(voucher.getVoucherDiscount());
		voucher.setVoucherDescription(voucher.getVoucherDescription());
		
		return voucherRepository.save(voucher);
	}

	public Iterable<Voucher> getVoucherList(){
		return voucherRepository.findAll();
	}
	
}

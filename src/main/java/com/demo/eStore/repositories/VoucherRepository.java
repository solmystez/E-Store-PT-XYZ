package com.demo.eStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.eStore.entity.Voucher;

@Repository
public interface VoucherRepository extends CrudRepository<Voucher, Long>{
	
	Voucher findByVoucherCode(String voucherCode);
}

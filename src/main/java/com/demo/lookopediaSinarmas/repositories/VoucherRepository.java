package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Voucher;

@Repository
public interface VoucherRepository extends CrudRepository<Voucher, Long>{
	
}

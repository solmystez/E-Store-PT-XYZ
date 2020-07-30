package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Invoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long>{
	
	Invoice findByInvoiceIdentifier(String invoiceId);
	
	Invoice findByUserId(Long id);
	
	List<Invoice> findAllByUserId(Long user_id);
	
}

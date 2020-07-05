package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Invoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long>{
	
//	Invoice findByInvoiceIdentifier(String invoiceId);

	@Override
	Iterable<Invoice> findAll();
	
	
}

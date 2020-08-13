package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Invoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long>{
	
//	@Query(value = "SELECT e FROM Invoice i WHERE e.invoice_identifier =: invoice_dentifier", nativeQuery = true)
	Invoice findByInvoiceIdentifier(String invoiceIdentifier);
		
	Invoice findByUserId(Long id);
	
	List<Invoice> findAllByUserId(Long user_id);
	
}

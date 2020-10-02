package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Category;
import com.demo.lookopediaSinarmas.entity.Courier;
import com.demo.lookopediaSinarmas.exceptions.product.ProductIdException;
import com.demo.lookopediaSinarmas.repositories.CourierRepository;

@Service
public class CourierService {
	
	@Autowired
	CourierRepository courierRepository;
	
	public Courier createOrUpdateCourier(Courier courier) {
		
		courier.setCourierName(courier.getCourierName());
		courier.setCourierPrice(courier.getCourierPrice());
		courier.setCourierDescription(courier.getCourierDescription());
		
		return courierRepository.save(courier);
	}

	public Iterable<Courier> getCourierList(){
		return courierRepository.findAll();
	}

	public void deleteCourierById(Long courier_id) {
		
		try {
			Courier courier = courierRepository.findById(courier_id).get();
			courierRepository.delete(courier);	
		} catch (Exception e) {
			throw new ProductIdException("Courier with ID '" + courier_id +"' cannot delete because doesn't exists");			
		}
	}

}

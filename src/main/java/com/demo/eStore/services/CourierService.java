package com.demo.eStore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eStore.entity.Courier;
import com.demo.eStore.exceptions.courier.CourierErrorException;
import com.demo.eStore.exceptions.product.ProductIdException;
import com.demo.eStore.repositories.CourierRepository;

@Service
public class CourierService {
	
	@Autowired
	CourierRepository courierRepository;
	
	public Courier createOrUpdateCourier(Courier courier) {
		
		try {
			courier.setCourierName(courier.getCourierName());
			courier.setCourierPrice(courier.getCourierPrice());
			courier.setCourierDescription(courier.getCourierDescription());
			return courierRepository.save(courier);
		} catch (Exception e) {
			throw new CourierErrorException("Courier name already exists !");
		}
		
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

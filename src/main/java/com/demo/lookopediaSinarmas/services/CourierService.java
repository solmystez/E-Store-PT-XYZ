package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Courier;
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

}

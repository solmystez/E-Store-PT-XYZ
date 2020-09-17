package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Status;
import com.demo.lookopediaSinarmas.repositories.StatusRepository;

@Service
public class StatusService {

	@Autowired
	StatusRepository statusRepository;
	
	public Status createOrUpdateCourier(Status status) {
		
		status.setStatusType(status.getStatusType());
//		status.setTransaction(status.getTransaction());
		
		return statusRepository.save(status);
	}

	public Iterable<Status> getCourierList(){
		return statusRepository.findAll();
	}
	
}

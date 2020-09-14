package com.demo.lookopediaSinarmas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.services.CourierService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/courier")
public class CourierController {
	
	@Autowired
	private CourierService courierService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
}

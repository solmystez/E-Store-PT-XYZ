package com.demo.lookopediaSinarmas.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.Courier;
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
	
	@PostMapping("/create")
	public ResponseEntity<?> createCourier(@Valid @RequestBody Courier courier, BindingResult result){
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Courier courier1 = courierService.createOrUpdateCourier(courier);
		return new ResponseEntity<Courier>(courier1, HttpStatus.CREATED);
	}
	
	@GetMapping("/loadAll")
	public Iterable<Courier> loadAllProduct(){
		return courierService.getCourierList();
	}
	
	@DeleteMapping("/deleteCourier/{courier_id}")
	public ResponseEntity<?> deleteCourier(@PathVariable Long courier_id){
		
		courierService.deleteCourierById(courier_id);

		return new ResponseEntity<String>("Courier ID '" + courier_id  + "' was successfully deleted", HttpStatus.OK);
	}
}

package com.demo.lookopediaSinarmas.web;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.Courier;
import com.demo.lookopediaSinarmas.exceptions.ProductIdException;
import com.demo.lookopediaSinarmas.repositories.CourierRepository;
import com.demo.lookopediaSinarmas.services.CourierService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/courier")
public class CourierController {
	
	@Autowired
	private CourierService courierService;
	
	@Autowired
	private CourierRepository courierRepostory;
	
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
	
	///////another variation
	
	//variation to create
	@PostMapping("/testCreateCourier")
	public Courier testCreateCourier(@RequestBody Courier courier) {
		return this.courierRepostory.save(courier);
	}
	
	//variation to update
	@PutMapping("/update/{courier_id}")
	public ResponseEntity<?> addProductToCartOrAddQty(@PathVariable(value = "courier_id") Long id,
			@Valid @RequestBody Courier courierDetails, BindingResult result){
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
	
		
		Courier courier1 = courierRepostory.findById(id)
				.orElseThrow(() -> new ProductIdException("Courier not found in this id :: " + id ));
		
		courier1.setCourierName(courierDetails.getCourierName());
		courier1.setCourierPrice(courierDetails.getCourierPrice());
		courier1.setCourierDescription(courierDetails.getCourierDescription());
		
		return ResponseEntity.ok(this.courierRepostory.save(courier1));
	}
	
	@DeleteMapping("/delete/{courier_id}")
	public Map<String, Boolean> deleteCourier(@PathVariable(value = "courier_id") Long id) {
		Courier courier1 = courierRepostory.findById(id)
				.orElseThrow(() -> new ProductIdException("Courier not found in this id :: " + id ));
		
		this.courierRepostory.delete(courier1);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
	}

}

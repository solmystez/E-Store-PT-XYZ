package com.demo.lookopediaSinarmas.controller;

import java.security.Principal;
import java.util.List;

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

import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.Voucher;
import com.demo.lookopediaSinarmas.services.VoucherService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

	@Autowired
	VoucherService voucherService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("/createOrUpdate")
	public ResponseEntity<?> createOrUpdateVoucher(@Valid @RequestBody Voucher voucher, BindingResult result){
	
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Voucher voucher1 = voucherService.createOrUpdateVoucher(voucher);
		return new ResponseEntity<Voucher>(voucher1, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/deleteVoucher/{voucher_id}")
	public ResponseEntity<?> deleteVoucher(@PathVariable Long voucher_id){
		
		voucherService.deleteVoucherById(voucher_id);

		return new ResponseEntity<String>("Voucher ID '" + voucher_id  + "' was successfully deleted", HttpStatus.OK);
	}
	
	@GetMapping("/loadAll")
	public Iterable<Voucher> loadAllProduct(){
		return voucherService.getVoucherList();
	}
	
	@PostMapping("/applyVoucher/{user_id}")
	public ResponseEntity<?> applyVoucher(@Valid @RequestBody Voucher voucher, BindingResult result,
			@PathVariable Long user_id){
	
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		List<Orders> voucher1 = voucherService.applyVoucher(voucher, user_id);
		return new ResponseEntity<List<Orders>>(voucher1, HttpStatus.OK);
	}
	
	@PostMapping("/cancelApplyVoucher/{user_id}")
	public ResponseEntity<?> cancelApplyVoucher(@PathVariable Long user_id, Principal principal){
		
		List<Orders> order = voucherService.cancelApplyVoucher(user_id, principal.getName());
		return new ResponseEntity<List<Orders>>(order, HttpStatus.OK);
	}
	
	@GetMapping("/getStatusVoucher/{user_id}")
	public String getVoucherStatusNow(@PathVariable Long user_id) {
		return voucherService.returnStatusVoucher(user_id);
	}
}

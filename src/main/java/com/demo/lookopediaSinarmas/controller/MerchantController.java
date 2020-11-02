package com.demo.lookopediaSinarmas.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.lookopediaSinarmas.entity.Merchant;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.services.MerchantService;
import com.demo.lookopediaSinarmas.services.ProductService;
import com.demo.lookopediaSinarmas.services.image.ImageStorageService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin(origins = { "http://localhost:3000"})
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
	
	private static Logger log = LoggerFactory.getLogger(MerchantController.class);
	public static String uploadDirectory = System.getProperty("user.dir") +  "/uploads";
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private ImageStorageService imageStorageService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@PostMapping("/createMerchantToUserId/{user_id}")
	public ResponseEntity<?> createMerchant(@Valid @RequestBody Merchant merchant, 
		BindingResult result,@PathVariable Long user_id, Principal principal){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Merchant merchant1 = merchantService.createMerchant(user_id, merchant, principal.getName());
		return new ResponseEntity<Merchant>(merchant1, HttpStatus.CREATED);
	 }
	
	@PostMapping("/updateMerchant/{merchant_id}")
	public ResponseEntity<?> updateMerchantInfo(@Valid @RequestBody Merchant merchant, 
			BindingResult result, Principal principal, @PathVariable Long merchant_id,
			@RequestParam("file") MultipartFile file){
			
			ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
			if(mapError != null) return mapError;
			
			merchant = merchantRepository.findMerchantByUserMerchantId(merchant_id);
			if(merchant == null) {
				throw new MerchantNotFoundException("Merchant not found");		    	
			}
			//image
			
			
			return new ResponseEntity<Merchant>(HttpStatus.ACCEPTED);
		 }
	
	@GetMapping("/findMerchant/{user_id}")
	public ResponseEntity<?> findMerchantByUserId(@PathVariable Long user_id) {
		
		Merchant merchant = merchantService.findMerchantByUserId(user_id);
		
		return new ResponseEntity<Merchant>(merchant, HttpStatus.OK);
	}
	
	@GetMapping("/loadMerchantProduct/{merchant_id}")
	public Iterable<Product> loadMerchantProduct(@PathVariable Long merchant_id){
		return productService.findAllProductsByMerchantId(merchant_id);
	}

	
	//another image code example
//	@PostMapping("/createProduct/{merchant_id}")
//	public ResponseEntity<?> createNewProduct(@Valid @RequestBody Product product, 
//		BindingResult result, @PathVariable Long merchant_id, @RequestParam("file") MultipartFile file,
//		Principal principal,  HttpServletRequest request){
//
//		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
//		if(mapError != null) return mapError;
//		
//		try {
//			String fileName = file.getOriginalFilename();
//			String path = request.getServletContext().getRealPath("") 
//					+ UPLOAD_DIR + File.separator + fileName;
//			
//			saveFile(file.getInputStream(), path);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		Product product1 = productService.createProduct(merchant_id, product, principal.getName(), file);
//		
//		
//		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
//	}
//	
//	private void saveFile(InputStream inputStream, String path) {
//		try {
//			OutputStream outputStream = new FileOutputStream(new File(path));
//			int read = 0;
//			byte[] bytes = new byte[1024];
//			while((read = inputStream.read(bytes)) != -1) {
//				outputStream.write(bytes, 0, read);
//			}
//			outputStream.flush();
//			outputStream.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}

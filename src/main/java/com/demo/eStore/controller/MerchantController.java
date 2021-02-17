package com.demo.eStore.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.eStore.entity.Cart;
import com.demo.eStore.entity.Merchant;
import com.demo.eStore.entity.Orders;
import com.demo.eStore.entity.Product;
import com.demo.eStore.entity.User;
import com.demo.eStore.exceptions.merchant.MerchantNameAlreadyExistsException;
import com.demo.eStore.exceptions.merchant.MerchantNotFoundException;
import com.demo.eStore.exceptions.user.UserIdNotFoundException;
import com.demo.eStore.repositories.MerchantRepository;
import com.demo.eStore.repositories.ProductRepository;
import com.demo.eStore.repositories.UserRepository;
import com.demo.eStore.services.MerchantService;
import com.demo.eStore.services.ProductService;
import com.demo.eStore.services.image.ImageStorageService;
import com.demo.eStore.services.otherService.MapValidationErrorService;
import com.demo.eStore.validator.MerchantValidator;

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
	private MerchantValidator merchantValidator;
	
	@Autowired
	private MerchantRepository merchantRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/createMerchantToUserId/{user_id}")
	public ResponseEntity<?> registerMerchant(@Valid Merchant merchant, @PathVariable Long user_id,
			BindingResult result, Principal principal){

			merchantValidator.validate(merchant, result);
		
			ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
			if(mapError != null) return mapError;
			
			Merchant merchant1 = merchantService.createMerchant(user_id, merchant, principal.getName());
			return new ResponseEntity<Merchant>(merchant1, HttpStatus.CREATED);
	}
	
	@PatchMapping("/updateMerchantToUserId/{user_id}")
	public ResponseEntity<?> updateMerchant(@Valid Merchant merchant, @PathVariable Long user_id,
		@RequestPart("file") MultipartFile file,
		BindingResult result, Principal principal){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Merchant merchant1 = merchantService.updateMerchant(user_id, merchant, principal.getName(), file);
		return new ResponseEntity<Merchant>(merchant1, HttpStatus.CREATED);
	 }
	
	//check order acc/reject order
	@GetMapping("/loadAllOrderInMerchant/{merchant_name}")
	public Iterable<Orders> findAllOrderMerchant(@PathVariable String merchant_name){
		return 	merchantService.findAllIncomingOrder(merchant_name);
	}
	
	@GetMapping(value = "/loadImageMerchant/{filename:.+}",
			produces = {MediaType.IMAGE_JPEG_VALUE,
					MediaType.IMAGE_GIF_VALUE,
					MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<Resource> loadImageMerchant(
			@PathVariable String filename,
			HttpServletRequest request) {
		
		Resource resource = imageStorageService.loadFileAsResource(filename);
		
		String contentType = null;
		
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}catch (IOException e) {
			System.out.println("cannot determine fileType");
		}
		
		if(contentType == null) {
			//ensure that is binary file
			contentType = "application/octet-stream";
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
//			    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
				.body(resource);
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

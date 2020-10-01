package com.demo.lookopediaSinarmas.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.util.FieldUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.services.ImageService;
import com.demo.lookopediaSinarmas.services.MerchantService;
import com.demo.lookopediaSinarmas.services.ProductService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
	
	public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@PostMapping("/createMerchantToUserId/{user_id}")
	public ResponseEntity<?> createMerchant(@Valid @RequestBody Merchant merchant, 
		BindingResult result,@PathVariable Long user_id, Principal principal){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Merchant merchant1 = merchantService.createMerchant(user_id, merchant, principal.getName());
		return new ResponseEntity<Merchant>(merchant1, HttpStatus.CREATED);
	 }
	
	
	@PostMapping("/createProduct/{merchant_id}")
	public @ResponseBody ResponseEntity<?> createNewProduct(@Valid @RequestBody Product product, 
			 @PathVariable Long merchant_id,
			 BindingResult result, Principal principal,HttpServletRequest request){
		//you cannot use request body and multipart together
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
//		Product product1 = productService.createProduct(merchant_id, product, principal.getName());
		
		try {
		    Merchant merchant = merchantRepository.findMerchantByUserMerchantId(merchant_id);
		    
		    String upImg = uploadImage(product.getProductImagePicture());
		    product.setProductImagePicture(upImg);
		    
//          File file = new File(uploadDirectory);
//          String directory = request.getRealPath("/")+"sample.jpg";
//          new FileOutputStream(directory).write(imageByte);
		    
//		    imageService.decoder(product.getProductImagePicture(), uploadDirectory);
		    
		    
		    //1
//			FileInputStream imageStream = new FileInputStream(product.getProductImagePicture());
//			
//			byte[] imageByte = java.util.Base64.getDecoder().decode(new String(imageStream.readAllBytes()));
//			FileOutputStream fileOutputStream = new FileOutputStream(uploadDirectory);
//			fileOutputStream.write(imageByte);
//			fileOutputStream.close();
//			imageStream.close();
		    //1
			
		    
		    //2
//		    try (FileOutputStream imageOutFile = new FileOutputStream(uploadDirectory)) {
//		          // Converting a Base64 String into Image byte array
//		          byte[] imageByteArray = java.util.Base64.getDecoder().decode(product.getProductImagePicture());
//		          imageOutFile.write(imageByteArray);
//		      } catch (FileNotFoundException e) {
//		          System.out.println("Image not found" + e);
//		      } catch (IOException ioe) {
//		          System.out.println("Exception while reading the Image " + ioe);
//		      }
		    //2
		    
		    product.setMerchant(merchant);
		    product.setMerchantName(merchant.getMerchantName());
//		    product.setProductCategory(product.getProductCategory().toLowerCase());
		    
		    Integer totalProduct = merchant.getTotalProduct();
		    totalProduct++;		    
		    merchant.setTotalProduct(totalProduct);
		    		
			productRepository.save(product);
		} catch (Exception e) {
			System.err.println(e);
			throw new MerchantNotFoundException("Merchant not found");
		}
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/updateProduct/{merchant_id}")
	public ResponseEntity<?> updateExistProduct(@Valid @RequestBody Product product, 
		BindingResult result, @PathVariable Long merchant_id, Principal principal){
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.updateProduct(merchant_id, product, principal.getName());
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
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

	//upload image to base64 string
	private String uploadImage(String imageVal) throws Exception {
		
//			FileInputStream imageStream = new FileInputStream(imageVal);
//			byte[] imageByte = imageStream.readAllBytes();
//			String imageString = java.util.Base64.getEncoder().encodeToString(imageByte);
			
			
			//save file local
			String filePath = Paths.get(uploadDirectory).toString();
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(imageVal);
//			fileWriter.write(imageString);
			fileWriter.close();
//			imageStream.close();
			
//			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
//			stream.write(file.getBytes());
//			stream.close();
			
			return "OK";
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

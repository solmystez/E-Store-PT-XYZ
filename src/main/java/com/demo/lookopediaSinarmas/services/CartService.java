package com.demo.lookopediaSinarmas.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Cart;
import com.demo.lookopediaSinarmas.entity.Courier;
import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.courier.CourierErrorException;
import com.demo.lookopediaSinarmas.exceptions.order.OrderNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductStockLimitException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartRepository;
import com.demo.lookopediaSinarmas.repositories.CourierRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class CartService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CourierRepository courierRepository;
	
	@Autowired
	UserService userService;
	
	//delete product on cart, not delete product
	@Transactional
	public List<Cart> removeProductFromCart(Long product_id, String username) {
		String status = "Not Paid";
		List<Cart> cart = null;
		try { 		
			cartRepository.deleteProductByProductIdAndUsernameAndStatus(product_id, username, status);
		} catch (Exception e) {
//			System.err.println(e);
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
		return cart;
	}
	
	public Iterable<Cart> selectCourier(Cart cart, Long product_id, String username) {
		
//		order = orderRepository.findByOrderIdentifier(order_identifier);
		//1. find product apa yg mau di pilih kurir
		//2. set courier ke smua product yg di 'cart' dari merchant yang sama
		//3. 
		
//		Product product;
//		try {
//			product = productRepository.findById(product_id).get();
//		} catch (Exception e) {
//			throw new ProductIdException("product id : " + product_id + " doesn't exist");
//		}
		
		String status = "Not Paid";
		List<Cart> cart1 = cartRepository
				.selectAllByProductIdAndUsernameAndStatus(product_id, username, status);
		Courier courier = courierRepository.findByCourierName(cart.getCourierName());
		if(courier==null) throw new CourierErrorException("no courier found while select courier");
		for(int i=0; i<cart1.size(); i++) {
			cart1.get(i).setCourier(courier);
			cart1.get(i).setCourierName(cart.getCourierName());
		}
		
		return cartRepository.saveAll(cart1);
	}
	
	public List<Cart> countOrderPriceAndStock(String username){
		String status = "Not Paid";
		List<Cart> cart;
		try {
			cart = cartRepository.findAllByUsernameAndStatus(username, status);
		} catch (Exception e) {
			throw new OrderNotFoundException("order not found");
		}
		
		int tempTotalPrice = 0;
		for(int i=0; i<cart.size(); i++) {
			tempTotalPrice += cart.get(i).getP_price() * cart.get(i).getQuantity(); //untuk total price di order
		}
		for(int i=0; i<cart.size(); i++) {
			cart.get(i).getOrder().setTotal_item(cart.size());
			cart.get(i).getOrder().setTotal_price(tempTotalPrice);
			cartRepository.save(cart.get(i));
		}
		return cart;
	}
	
	
	public Orders addProductToCartOrAddQty(Long product_id, Long user_id, String username) {
		
		Product product;
		try {
			product = productRepository.findById(product_id).get();
		} catch (Exception e) {
			throw new ProductNotFoundException("Product not found");
		}
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
		}
		
		String status = "Not Paid";
		//cek udah ada ordernya blm untuk merchant product x, dan user x
		List<Orders> tempListOrder = null;
		Orders order = null;
		//query ke table order ambil index 0
		tempListOrder = orderRepository.findByMerchantNameAndUsernameAndStatusIsNull(product.getMerchantName(), username, status);
		order =  tempListOrder.size() == 0 || tempListOrder  == null ? null : tempListOrder.get(0);
		
		//cek ada isi ga hasil dari atas
		// klao ga ada create
		if(order == null) {
			order = new Orders();
			order.setMerchantName(product.getMerchantName());
			order.setUsername(username);
			order.setUser(user);
			order.setStatus(status);
			orderRepository.save(order);
			countOrderPriceAndStock(username);
		}
		
		//query ke table cart ambil index 0
		List<Cart> tempListCart = cartRepository.selectCourierByOrderIdAndProductId(order.getId(), product_id);
		Cart cart = tempListCart == null || tempListCart.isEmpty() ? null : tempListCart.get(0);
		
		//cek ada isi ga hasil dari atas
		// klao ga ada create kalo ada qty ++
		if(cart == null) {
			cart = new Cart(order, product);
			cart.setOrder(order);
			cart.setUsername(username);
			
			cart.setQuantity(cart.getQuantity()+1);
			cart.setStatus("Not Paid");
			cart.setP_id(product.getProduct_id());
			cart.setP_name(product.getProductName());
			cart.setP_price(product.getProductPrice());
			cart.setP_qty(product.getProductStock());
			cart.setP_description(product.getProductDescription());
			cart.setP_filePath(product.getFilePath());
			cart.setUsername(username);
			cart.setTotal_price(cart.getQuantity() * product.getProductPrice());
			cartRepository.save(cart);
			countOrderPriceAndStock(username);
			return cart.getOrder();
		}else {
			if(cart.getQuantity() >= product.getProductStock()) {
				throw new ProductStockLimitException("Product stock only left : " + product.getProductStock());
			}
			cart.setQuantity(cart.getQuantity()+1);
			cart.setStatus("Not Paid");
			cart.setP_id(product.getProduct_id());
			cart.setP_name(product.getProductName());
			cart.setP_price(product.getProductPrice());
			cart.setP_qty(product.getProductStock());
			cart.setP_description(product.getProductDescription());
			cart.setP_filePath(product.getFilePath());
			cart.setUsername(username);
			cart.setTotal_price(cart.getQuantity() * product.getProductPrice());
			cartRepository.save(cart);
			countOrderPriceAndStock(username);
			return cart.getOrder();
		}
		
	}
	
	public Orders removeProductFromCartOrSubQty(Long product_id, Long user_id, String username) {
		
		Product product;
		try {
			product = productRepository.findById(product_id).get();
		} catch (Exception e) {
			throw new ProductNotFoundException("Product not found");
		}
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
		}
		
		String status = "Not Paid";
		//cek udah ada ordernya blm untuk merchant product x, dan user x
		List<Orders> tempListOrder = null;
		Orders order = null;
		//query ke table order ambil index 0
		tempListOrder = orderRepository.findByMerchantNameAndUsernameAndStatusIsNull(product.getMerchantName(), username, status);
		order =  tempListOrder.size() == 0 || tempListOrder  == null ? null : tempListOrder.get(0);
		
		//cek ada isi ga hasil dari atas
		// klao ga ada create
		if(order == null) {
			order = new Orders();
			order.setMerchantName(product.getMerchantName());
			order.setUsername(username);
			order.setUser(user);
			order.setStatus(status);
			orderRepository.save(order);
			countOrderPriceAndStock(username);
		}
		
		//query ke table cart ambil index 0
		List<Cart> tempListCart = cartRepository.selectCourierByOrderIdAndProductId(order.getId(), product_id);
		Cart cart = tempListCart == null || tempListCart.isEmpty() ? null : tempListCart.get(0);
		
		//cek ada isi ga hasil dari atas
		// klao ga ada create kalo ada qty ++
		if(cart == null) {
			cart = new Cart(order, product);
			cart.setOrder(order);
			cart.setUsername(username);
			
			cart.setQuantity(cart.getQuantity()-1);
			cart.setStatus("Not Paid");
			cart.setP_id(product.getProduct_id());
			cart.setP_name(product.getProductName());
			cart.setP_price(product.getProductPrice());
			cart.setP_qty(product.getProductStock());
			cart.setP_description(product.getProductDescription());
			cart.setP_filePath(product.getFilePath());
			cart.setUsername(username);
			cart.setTotal_price(cart.getQuantity() * product.getProductPrice());
			cartRepository.save(cart);
			countOrderPriceAndStock(username);
			return cart.getOrder();
		}else {
			if(cart.getQuantity() <= 1) {
				throw new ProductStockLimitException("Product must at least 1 pcs");
			}
			cart.setQuantity(cart.getQuantity()-1);
			cart.setStatus("Not Paid");
			cart.setP_id(product.getProduct_id());
			cart.setP_name(product.getProductName());
			cart.setP_price(product.getProductPrice());
			cart.setP_qty(product.getProductStock());
			cart.setP_description(product.getProductDescription());
			cart.setP_filePath(product.getFilePath());
			cart.setUsername(username);
			cart.setTotal_price(cart.getQuantity() * product.getProductPrice());
			cartRepository.save(cart);
			countOrderPriceAndStock(username);
			return cart.getOrder();
		}
		
	}

	
}

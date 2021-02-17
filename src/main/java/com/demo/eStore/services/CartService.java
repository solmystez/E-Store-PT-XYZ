package com.demo.eStore.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.eStore.entity.Cart;
import com.demo.eStore.entity.Courier;
import com.demo.eStore.entity.Orders;
import com.demo.eStore.entity.Product;
import com.demo.eStore.entity.User;
import com.demo.eStore.exceptions.courier.CourierErrorException;
import com.demo.eStore.exceptions.order.OrderNotFoundException;
import com.demo.eStore.exceptions.product.ProductIdException;
import com.demo.eStore.exceptions.product.ProductNotFoundException;
import com.demo.eStore.exceptions.product.ProductStockLimitException;
import com.demo.eStore.exceptions.user.UserIdNotFoundException;
import com.demo.eStore.repositories.CartRepository;
import com.demo.eStore.repositories.CourierRepository;
import com.demo.eStore.repositories.MerchantRepository;
import com.demo.eStore.repositories.OrderRepository;
import com.demo.eStore.repositories.ProductRepository;
import com.demo.eStore.repositories.UserRepository;

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
			User user = userRepository.findByUsername(username);
			List<Orders> order = orderRepository.findAllByUserIdAndStatus(user.getId(), status);
			for(int i=0; i<order.size(); i++) {
				Orders toRemove = order.get(i);
				if(toRemove.getCart_detail().isEmpty()) {
					order.remove(toRemove);
//					orderRepository.save(order.get(i));
					orderRepository.delete(toRemove);
				}
			}
		
		} catch (Exception e) {
//			System.err.println(e);
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
		return cart;
	}
	
	public Orders selectCourier(Orders order, String username) {
		
		//1. find product apa yg mau di pilih kurir
		//2. set courier ke smua product yg di 'cart' dari merchant yang sama
		
		//get cartBody courierName : "Tiki"
		//set courierName ke productYg di select
		try {
			String status = "Not Paid";
			Orders order1 = orderRepository.findByMerchantNameAndStatusAndUsername(order.getMerchantName(), status, username);
			
			Courier courier = courierRepository.findByCourierName(order.getCourierName());
			double tempPrice = order1.getTotal_price() + courier.getCourierPrice();
			order1.setTotal_price(tempPrice);
			order1.setCourier(courier);
			order1.setCourierName(order.getCourierName());
			order1.setUserAddress(order.getUserAddress());
//			order1.setMerchantAddress(order.getMerchantAddress());
			return orderRepository.save(order1);
		} catch (Exception e) {
			throw new CourierErrorException("please select courier and address correctly");
		}
		
	}
	
	
	public List<Cart> countOrderPriceAndStock(String username){
		String status = "Not Paid";
		User user = userRepository.findByUsername(username);
		List<Cart> cart;
//		List<Orders> order = orderRepository.findAllByUserIdAndStatus(user.getId(), status);
//		for(int i=0; i<order.size(); i++) {
//			Orders toRemove = order.get(i);
//			if(toRemove.getCart_detail().isEmpty()) {
////				orderRepository.save(order.get(i));
//				orderRepository.delete(toRemove);
//			}
//		}
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
	
	public int returnTotalItem(Long user_id) {
		String status = "Not Paid";
		//find semua order yang belum 
		List<Orders> orders = orderRepository.findAllByUserIdAndStatus(user_id, status);
		
		int temp=0;
		for(int i=0; i<orders.size(); i++) {
			temp = orders.get(i).getTotal_item();
			if(temp == orders.size()) break;
		}
		
		return temp;
	}
	

	public double returnTotalPrice(Long user_id) {
		String status = "Not Paid";
		//find semua order yang belum 
		List<Orders> orders = orderRepository.findAllByUserIdAndStatus(user_id, status);
		
		double temp=0;
		for(int i=0; i<orders.size(); i++) {
			temp = orders.get(i).getTotal_price();
			break;
		}
		
		return temp;
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
			order.setHasVoucher("No");
			order.setMerchantAddress(product.getMerchant().getMerchantAddress());
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
			try {
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
			} catch (Exception e) {
				throw new OrderNotFoundException("something wrong when add 2 time");
			}
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

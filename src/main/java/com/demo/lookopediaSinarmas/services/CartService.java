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
	
	//load all item before go to /buy
	public List<Cart> getCartDetailByUserIdAndPrincipalName(String username) {
		
		//findAll product in cart By username and statusNotPaid
		String status = "Not Paid";
		List<Cart> cartDetail = null;
		cartDetail = cartRepository.findAllByUsernameAndStatus(username, status);
		
		return cartDetail;
	}
	
	//delete product on cart, not delete product
	@Transactional
	public List<Cart> removeProductFromCart(Long product_id, String orderIdentifier) {

		try { 		
			cartRepository.deleteCartDetailByOrderIdentifierAndProductId(orderIdentifier, product_id);
		} catch (Exception e) {
//			System.err.println(e);
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
		return cartRepository.findAllByOrderIdentifier(orderIdentifier);
	
	}
	
	public Iterable<Cart> selectCourier(Cart cart, String order_identifier) {
		
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
		
		String merchant_name = cart.getMerchantName();
		
		List<Cart> cart1 = cartRepository
				.selectCourierByOrderIdentifierAndMerchantName(order_identifier, merchant_name);
		Courier courier = courierRepository.findByCourierName(cart.getCourierName());
		if(courier==null) throw new CourierErrorException("no courier found while select courier");
		for(int i=0; i<cart1.size(); i++) {
			cart1.get(i).setCourier(courier);
			cart1.get(i).setCourierName(cart.getCourierName());
		}
		
		
		return cartRepository.saveAll(cart1);
	}
	
	public List<Cart> countCartPriceAndStock(String order_identifier){
		
		Orders order = orderRepository.findByOrderIdentifier(order_identifier);
		
		List<Cart> cart;
		try {
			cart = cartRepository.findAllByOrderIdentifier(order_identifier);
		} catch (Exception e) {
			throw new OrderNotFoundException("order_identifier not found");
		}
		
		int count = 0;
		int tempPrice = 0;
		for(int i=0; i<cart.size(); i++) {
			count++;
			cart.get(i).setTotal_price(cart.get(i).getProduct().getProductPrice() * cart.get(i).getQuantity());
			tempPrice += cart.get(i).getTotal_price();
		}
		order.setTotal_price(tempPrice);
		order.setTotal_item(count);
		return cart;
	}
	
	public Orders addProductVer3(Long product_id, Long user_id, String username) {
		Orders tempOrder = null;
		String status = "Not Paid";
		int flag = 1;
		
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
		
		List<Cart> carts = cartRepository.findAllByUsernameAndStatus(username, status);
		Cart newCart = null;
		if(carts.isEmpty()) {
			System.out.println("msk ke null");
			tempOrder = new Orders();
			tempOrder.setMerchantName(product.getMerchantName());
			tempOrder.setUser(user);
			orderRepository.save(tempOrder);
			
			newCart = new Cart(tempOrder, product);
			newCart.setOrder(tempOrder);
			newCart.setProduct(product);
			newCart.setUsername(username);
			newCart.setStatus(status);
			newCart.setP_id(product.getProduct_id());
			newCart.setP_name(product.getProductName());
			flag = 0;
			cartRepository.save(newCart);
			return tempOrder;
		}

		if(flag == 1) {
			for(int i=0; i<carts.size(); i++) {
				//1. check dlu ad produk nya uda ada blm, kalo udah +qty
				if(carts.get(i).getProduct().getProduct_id().equals(product.getProduct_id())) {
					carts.get(i).setQuantity(carts.get(i).getQuantity()+1);
					carts.get(i).setTotal_price(carts.get(i).getQuantity() * carts.get(i).getProduct().getProductPrice());
					cartRepository.save(carts.get(i));
					return carts.get(i).getOrder();
					
				}
				//2. check all product in keranjang, saat ini produk sama atau tidak bdsrkan ID
				//krn tidak, do
				if(!carts.get(i).getProduct().getProduct_id().equals(product.getProduct_id())) {
					//check lg merchantName cart == product yg di select 
					if(carts.get(i).getOrder().getMerchantName()
							.equals(product.getMerchantName())) {
						newCart = new Cart(carts.get(i).getOrder(), product);
						newCart.setOrder(carts.get(i).getOrder());
						newCart.setProduct(product);
						newCart.setUsername(username);
						newCart.setStatus(status);
						newCart.setP_id(product.getProduct_id());
						newCart.setP_name(product.getProductName());
						cartRepository.save(newCart);
						return carts.get(i).getOrder();
					}else{//krn beda produknya, cek lg merchantName si produk yg lg di select skarang
						tempOrder = new Orders();
						tempOrder.setMerchantName(product.getMerchantName());
						tempOrder.setUser(user);
						orderRepository.save(tempOrder);
						
						newCart = new Cart(tempOrder, product);
						newCart.setOrder(tempOrder);
						newCart.setProduct(product);
						newCart.setUsername(username);
						newCart.setStatus(status);
						newCart.setP_id(product.getProduct_id());
						newCart.setP_name(product.getProductName());
						cartRepository.save(newCart);
						return carts.get(i).getOrder();
					}
					
				}
				
			}
			
		}
		
		return tempOrder;
	}
	
	public Orders addProductVer4(Long product_id, Long user_id, String username) {
		Orders tempOrder = null;
		String status = "Not Paid";
		int flag = 1;
		
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
		
		List<Cart> carts = cartRepository.findAllByUsernameAndStatus(username, status);
		Cart newCart = null;
		if(carts.isEmpty()) {
			System.out.println("msk ke null");
			tempOrder = new Orders();
			tempOrder.setMerchantName(product.getMerchantName());
			tempOrder.setUser(user);
			orderRepository.save(tempOrder);
			
			newCart = new Cart(tempOrder, product);
			newCart.setOrder(tempOrder);
			newCart.setProduct(product);
			newCart.setUsername(username);
			newCart.setStatus(status);
			newCart.setP_id(product.getProduct_id());
			newCart.setP_name(product.getProductName());
			flag = 0;
			cartRepository.save(newCart);
			return tempOrder;
		}

		if(flag == 1) {
			for(int i=0; i<carts.size(); i++) {
				//1. check dlu ad produk nya uda ada blm, kalo udah +qty
				if(carts.get(i).getOrder().getMerchantName()
							.equals(product.getMerchantName())) {
					if(carts.get(i).getProduct().getProduct_id().equals(product.getProduct_id())) {
						carts.get(i).setQuantity(carts.get(i).getQuantity()+1);
						carts.get(i).setTotal_price(carts.get(i).getQuantity() * carts.get(i).getProduct().getProductPrice());
						cartRepository.save(carts.get(i));
						return carts.get(i).getOrder();
						
					}else if(i == (carts.size()- 1)){
						newCart = new Cart(carts.get(i).getOrder(), product);
						newCart.setOrder(carts.get(i).getOrder());
						newCart.setProduct(product);
						newCart.setUsername(username);
						newCart.setStatus(status);
						newCart.setP_id(product.getProduct_id());
						newCart.setP_name(product.getProductName());
						cartRepository.save(newCart);
						return carts.get(i).getOrder();
					}
					
				}else if(i == (carts.size()- 1)){
					
					tempOrder = new Orders();
					tempOrder.setMerchantName(product.getMerchantName());
					tempOrder.setUser(user);
					orderRepository.save(tempOrder);
					
					newCart = new Cart(tempOrder, product);
					newCart.setOrder(tempOrder);
					newCart.setProduct(product);
					newCart.setUsername(username);
					newCart.setStatus(status);
					newCart.setP_id(product.getProduct_id());
					newCart.setP_name(product.getProductName());
					cartRepository.save(newCart);
					return tempOrder;
				}
			}
		}
		
		return tempOrder;
	}
	public Orders addProductVer2(Long product_id, Long user_id, String username) {
		

		
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
		}
		
		//query ke table cart ambil index 0
		List<Cart> tempListCart = cartRepository.selectCourierByOrderIdAndProductId(order.getId(), product_id);
		Cart cart = tempListCart == null || tempListCart.isEmpty() ? null : tempListCart.get(0);
		
		//cek ada isi ga hasil dari atas
		// klao ga ada create kalo ada qty ++
		if(cart == null) {
			cart = new Cart(order, product);
			cart.setOrder(order);
			cart.setProduct(product);
			cart.setUsername(username);
			cart.setStatus(status);
			cart.setP_id(product.getProduct_id());
			cart.setP_name(product.getProductName());
			
			cartRepository.save(cart);
			return order;
		}else {
			cart.setQuantity(cart.getQuantity()+1);
			cart.setTotal_price(cart.getQuantity() * cart.getProduct().getProductPrice());
			cartRepository.save(cart);
			return cart.getOrder();
		}
	}
	public Orders addProductToCartAndConnectToOrder(Long product_id, Long user_id, String username) {
		String status = "Not Paid";
		Orders tempOrder = null;
		Product product;
		Orders ord = null;
		
		try {
			product = productRepository.findById(product_id).get();
		} catch (Exception e) {
			throw new ProductNotFoundException("Product not found");
		}
		
		int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1	
		Iterator<Cart> it = product.getCart_detail().iterator();
		List<Cart> carts = cartRepository.findAllByUsernameAndStatus(username, status);
		//		Iterator<Cart> it = carts.iterator();
		
		if(!it.hasNext()) {
			ord = new Orders();
			ord.setUsername(username);
			ord.setMerchantName(product.getMerchantName());
			orderRepository.save(ord);
			it = ord.getCart_detail().iterator();
			
		}
		
		while(it.hasNext()){
//			it = ord.getCart_detail().iterator();
			Cart c = it.next();
			
//			if(c.getProduct().getProduct_id().equals(product.getProduct_id())) {
				if(c.getQuantity() >= product.getProductStock()) {
					throw new ProductStockLimitException("Product stock only left : " + product.getProductStock());
				}
				
				c.setStatus("Not Paid");
				c.setQuantity(c.getQuantity()+1);
				c.setMerchantName(product.getMerchantName());
				c.setP_id(product.getProduct_id());
				c.setP_name(product.getProductName());
				c.setP_price(product.getProductPrice());
				c.setP_qty(product.getProductStock());
				c.setP_description(product.getProductDescription());
				c.setTotal_price(c.getQuantity() * product.getProductPrice());
				c.setP_filePath(product.getFilePath());
				cartRepository.save(c);
				
//				countCartPriceAndStock(order_identifier);
				
				flag = 0;
				tempOrder = c.getOrder();
				break;
//			}
		}
		if(flag == 1) {
			Cart newCart = new Cart(ord, product);
			
//			newCart.setOrderIdentifier(user.getTrackOrder());
//			order.setOrderIdentifier(user.getTrackOrder());
			
			newCart.setOrder(ord);
			
			newCart.setQuantity(1);
			newCart.setStatus("Not Paid");
			newCart.setProduct(product);
			newCart.setMerchantName(product.getMerchantName());
			newCart.setP_id(product_id);
			newCart.setP_name(product.getProductName());
			newCart.setP_price(product.getProductPrice());
			newCart.setP_qty(product.getProductStock());
			newCart.setP_description(product.getProductDescription());
			newCart.setP_filePath(product.getFilePath());
			newCart.setUsername(username);
			newCart.setTotal_price(newCart.getQuantity() * product.getProductPrice());
			
			product.getCart_detail().add(newCart);
			
			orderRepository.save(ord);
			productRepository.save(product);
			cartRepository.save(newCart);
		}
		
		
		String tempMerchantName;
		for(int i=0; i<carts.size(); i++) {
			if(carts.size() == i+1) break;
			if(carts.size() > i) {
				tempMerchantName = carts.get(i).getMerchantName();
				carts.get(i).setMerchantName(tempMerchantName);
				if(carts.get(i).getMerchantName() != carts.get(i+1).getMerchantName()) {
					tempMerchantName = carts.get(i+1).getMerchantName();
					Orders newOrds = new Orders();
					carts.get(i).setOrder(newOrds);
					carts.get(i).setMerchantName(tempMerchantName);
					orderRepository.save(newOrds);
				}
			}
		}
		
//		ArrayList<Cart> checkCarts = cartRepository.findAllByUsernameAndStatus(username, status);
//		
//		ListIterator<Cart> itCarts = checkCarts.listIterator();
//		
//		while(itCarts.hasNext()) {
//			Cart cNext = itCarts.next();
//			if(itCarts.hasPrevious()) {
//				if(cNext.getMerchantName() != itCarts.previous().getMerchantName()) {
//					Orders newOrd = new Orders();
//					cNext.setOrder(newOrd);
//					
//					newOrd.setMerchantName(cNext.getMerchantName());
//					orderRepository.save(newOrd);
//					
//					break;
//				}
//			}
//			break;
//		}
//		
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
		}
		
		//-. create aj produk mskin ke cart, attach merchantName, userName, set status NotPaid
		//user view cart by userName and status 'not paid'
		
		//1. produk yg di create ke keranjang konek ke 1 order 
			//jika : product di cart get order_id null
		
		//loop smua barang di cart, check punya order_id ngga
		//check product di cart by merchantName, kalo sama konek ke 1 order
		//kalo beda new Order(), set newOrd cart(i).getProduct
		
		//2. 
//		List<Cart> carts = cartRepository.findall
		//attach ke 1 order
		
	

//		while(carts.size() < 0) {
//			if(cart)
//		}
		
//		if(product.getMerchant().getMerchantName().equals("")) {
//			
//		}
		
		return tempOrder;
	}
	
	
	////////////////////////////
	//add + sub product (ord-identifier ver)
	///////////////////////////
	
	public Orders addProductToCartOrAddQty(Long product_id, Long user_id, String order_identifier, String username) {
				
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
		
		Orders order = orderRepository.findByOrderIdentifier(order_identifier);
		if(order == null) {
			throw new OrderNotFoundException("order not found");
		}
		
		int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
		Orders tempOrder = null;
		
		Iterator<Cart> it = product.getCart_detail().iterator();
		if(!it.hasNext()) {
			it = order.getCart_detail().iterator();
			order.setOrderIdentifier(user.getTrackOrder());
			order.setMerchantName(product.getMerchantName());
		}
		
		while(it.hasNext()){
			Cart c = it.next();
			if(c.getOrder().getId().equals(order.getId()) 
					&&  c.getProduct().getProduct_id().equals(product.getProduct_id())) {
				
				if(c.getQuantity() >= product.getProductStock()) {
					throw new ProductStockLimitException("Product stock only left : " + product.getProductStock());
				}
				c.setStatus("Not Paid");
				c.setQuantity(c.getQuantity()+1);
				c.setMerchantName(product.getMerchantName());
				c.setP_id(product.getProduct_id());
				c.setP_name(product.getProductName());
				c.setP_price(product.getProductPrice());
				c.setP_qty(product.getProductStock());
				c.setP_description(product.getProductDescription());
				c.setTotal_price(c.getQuantity() * product.getProductPrice());
				c.setP_filePath(product.getFilePath());
				c.setUsername(username);
				cartRepository.save(c);
				
				countCartPriceAndStock(order_identifier);
				
				flag = 0;
				tempOrder = c.getOrder();
				break;
			}
		}
		
		
		//create cart detail kalo belom pernah di add ke cart
		if(flag == 1) {
			Cart currDetail= new Cart(order,product);
			currDetail.setOrderIdentifier(user.getTrackOrder());
			order.setOrderIdentifier(user.getTrackOrder());
			currDetail.setQuantity(1);
			currDetail.setStatus("Not Paid");
			currDetail.setOrder(order);
			currDetail.setProduct(product);
			currDetail.setMerchantName(product.getMerchantName());
			currDetail.setP_id(product_id);
			currDetail.setP_name(product.getProductName());
			currDetail.setP_price(product.getProductPrice());
			currDetail.setP_qty(product.getProductStock());
			currDetail.setP_description(product.getProductDescription());
			currDetail.setP_filePath(product.getFilePath());
			currDetail.setUsername(username);
			currDetail.setTotal_price(currDetail.getQuantity() * product.getProductPrice());
//			currDetail.setTotalProductPrice(currDetail.getTotalProductPrice() + currDetail.getTotal_price());
			
			
			cartRepository.save(currDetail);

			// add cart detail ke cart
			order.getCart_detail().add(currDetail);
			product.getCart_detail().add(currDetail);
			productRepository.save(product);
			tempOrder = orderRepository.save(order);
			countCartPriceAndStock(order_identifier);
			
		}
		return tempOrder;
		
	}
	
	public Orders subProductInCart(Long product_id, Long user_id, String order_identifier, String username) {
		
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
		
		Orders order = orderRepository.findByOrderIdentifier(order_identifier);
		if(order == null) {
			throw new OrderNotFoundException("order not found");
		}
		
		int flag = 1; // cek udah ada di cart ga, kalo udah ada quantity + 1		
		Orders tempOrder = null;
		
		Iterator<Cart> it = product.getCart_detail().iterator();
		if(!it.hasNext()) {
			it = order.getCart_detail().iterator();
			order.setOrderIdentifier(user.getTrackOrder());
		}
		
		while(it.hasNext()){
			Cart c = it.next();
			if(c.getOrder().getId().equals(order.getId()) 
					&&  c.getProduct().getProduct_id().equals(product.getProduct_id())) {
				
				if(c.getQuantity() <= 0) {
					throw new ProductStockLimitException("Product must at least 1 pcs");
				}
				c.setQuantity(c.getQuantity()-1);
				c.setMerchantName(product.getMerchantName());
				c.setP_id(product.getProduct_id());
				c.setP_name(product.getProductName());
				c.setP_price(product.getProductPrice());
				c.setP_qty(product.getProductStock());
				c.setP_description(product.getProductDescription());
				c.setTotal_price(c.getQuantity() * product.getProductPrice());
				c.setP_filePath(product.getFilePath());
				cartRepository.save(c);
				
				countCartPriceAndStock(order_identifier);
				
				flag = 0;
				tempOrder = c.getOrder();
				break;
			}
		}
		
		
		//create cart detail kalo belom pernah di add ke cart
		if(flag == 1) {
			Cart currDetail= new Cart(order,product);
			currDetail.setOrderIdentifier(user.getTrackOrder());
			order.setOrderIdentifier(user.getTrackOrder());
			currDetail.setQuantity(1);
			currDetail.setOrder(order);
			currDetail.setProduct(product);
			currDetail.setMerchantName(product.getMerchantName());
			currDetail.setP_id(product_id);
			currDetail.setP_name(product.getProductName());
			currDetail.setP_price(product.getProductPrice());
			currDetail.setP_qty(product.getProductStock());
			currDetail.setP_description(product.getProductDescription());
			currDetail.setP_filePath(product.getFilePath());
			
			currDetail.setTotal_price(currDetail.getQuantity() * product.getProductPrice());
//			currDetail.setTotalProductPrice(currDetail.getTotalProductPrice() + currDetail.getTotal_price());
			
			
			cartRepository.save(currDetail);

			// add cart detail ke cart
			order.getCart_detail().add(currDetail);
			product.getCart_detail().add(currDetail);
			productRepository.save(product);
			tempOrder = orderRepository.save(order);
			countCartPriceAndStock(order_identifier);
			
		}
		return tempOrder;
		
	}
	
}

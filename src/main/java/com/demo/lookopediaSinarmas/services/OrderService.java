package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Cart;
import com.demo.lookopediaSinarmas.entity.Courier;
import com.demo.lookopediaSinarmas.entity.Merchant;
import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.courier.CourierErrorException;
import com.demo.lookopediaSinarmas.exceptions.order.OrderNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartRepository;
import com.demo.lookopediaSinarmas.repositories.CourierRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CartRepository cartDetailRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	CourierRepository courierRepository;
	
	@Autowired 
	UserService userService;
	
	@Autowired
	CartService cartService;
	
	//nampilin order ke merchant mau jd 2 ( not paid, paid, process, rejected , finish?)
	//1. loadAllIncoming order, brati dpt smua id order yg harus di process
	// untuk order yg di klik, set statusnya (disini mau masukin alasan tidak?, klo ia, nambah attribute messageMerchant)
	//
	
	//buat tampilin di merchant, pesanan yg udh checkout untuk di acc/ reject
	public Iterable<Orders> loadIncomingOrderToProcess(String merchantName){
		String status = "Paid";
		
		List<Orders> orders;
		try {
			orders = orderRepository.findAllByMerchantNameAndStatus(merchantName, status);
		} catch (Exception e) {
			throw new OrderNotFoundException("No order found");
		}
		
		return orders;
	}
	
	//api
	//kalo udh di tampilin di merchant, bkin API yg req body, process, set process ny
	public Orders acceptOrRejectOrder(Orders order, Long order_id) {
		
		//disini front end hardcode kirim status berdasarkan tombol yg di sdiain di FE sndiri(accept button/ reject button)
		try {
			order = orderRepository.findById(order_id).get();
			
			order.setStatus(order.getStatus());
			return orderRepository.save(order);
		} catch (Exception e) {
			//prevent testing backend with empty body
			throw new OrderNotFoundException("Order status cannot empty");
		}
		
	}
	//1. getAllOrderByUsername
	//2. when click checkout button
	//buat user klo klik checkout
	public Iterable<Orders> processItem(Orders orders, Long user_id, String username) {	
		//buat semua order yg usernamenya A dan status nya Not paid
		//kondisi proses disini bgmn?, (scheduler)
		//Special Case : 
		//untuk nge checkout berarti,
		String status = "Not Paid";
		int tempPrice = 0;
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e1) {
			throw new UserIdNotFoundException("User not found");
		}
		
		List<Orders> order1;
		try {
			order1 = orderRepository.findAllByUserIdAndStatus(user_id, status);
		} catch (Exception e) {
			throw new OrderNotFoundException("No order found");
		}
		
		List<Cart> carts = cartDetailRepository.findAllByUsernameAndStatus(username, status);
		
		//set status jd paid
		for(int i=0; i<carts.size(); i++) {
			int stock = 0;
			
			//anngep scheduler sudah read dan user sudah(dianggap) bayar
			carts.get(i).setStatus("Paid");
			
//			tempPrice += carts.get(i).getP_price() * carts.get(i).getQuantity(); //untuk total price di order
//			carts.get(i).getOrder().setTotal_price(tempPrice);
			
			stock = carts.get(i).getProduct().getProductStock() - carts.get(i).getQuantity(); //ngurangin stock product merchant
			carts.get(i).getProduct().setProductStock(stock);
			cartDetailRepository.save(carts.get(i));
		}
		
		
		for(int i=0; i<order1.size(); i++) {
			order1.get(i).setStatus("Paid");
			orderRepository.save(order1.get(i));
		}
		
		return order1;
	}
	
	
//	public Iterable<Cart> processItem(Cart cart, String order_identifier, Long user_id) {	
//		//====quest====
//		//-. kalkulasi harga disini ('total_price' Orders attribute)
//		//-. totalHarga+= voucher apa
//		//-. totalHarga+= courier apa
//		//-. --Stock product di merchant
//		//-. set transaction status 'Not Paid'
//		//-. generate order_number
//		// ubah status nya jadi process
//		Orders order = orderRepository.findByOrderIdentifier(order_identifier);
//		if(order == null) throw new OrderNotFoundException("order not found");
//		
//		//1. temuin cart yg mana yg mau di proses + product apa aja 
//		//brati findByOrderIdentifier > tampung ke list
//		//=======gmn gw mainin object masing" nya ?
//		//product A di --Stockny 
//		
//		List<Cart> carts = cartDetailRepository.findAllByOrderIdentifier(order_identifier, Sort.by(Sort.Direction.ASC,"merchantName"));
//		for (int i = 0; i < carts.size(); i++) {
//			System.out.println(carts.get(i) + carts.get(i).getMerchantName());
//		}
//		int tempPrice = 0;
//		
//		
////		for(int i=0; i<carts.size(); i++) {
////			int stock = 0;
////			
////			tempPrice += carts.get(i).getP_price() * carts.get(i).getQuantity(); //untuk total price di order
////			stock = carts.get(i).getProduct().getProductStock() - carts.get(i).getQuantity(); //ngurangin stock product merchant
////			carts.get(i).getProduct().setProductStock(stock);
//////			if(carts.size()-1 == i) break;
////			if(carts.get(i).getMerchantName() != carts.get(i+1).getMerchantName()) {
////				tempPrice+=carts.get(i).getCourier().getCourierPrice();
////			}
////			
////			carts.get(i).getProduct().getMerchant().getMerchantName();
////			carts.get(i).setStatus("Paid");
////			//every product sold, then add funds to merchant balance
////			String merchantName = carts.get(i).getProduct().getMerchant().getMerchantName();
////			Merchant merchant = merchantRepository.findByMerchantName(merchantName);
////			merchant.setMerchantBalance(tempPrice);
////		}
//		Iterator<Cart> it = carts.iterator();
//		//validasi klo cmn 1 barang, kurirPrice++, belom
//		while(it.hasNext()) {
//			Cart c = it.next();
//			
//			int stock = 0;
//			tempPrice += c.getP_price() * c.getQuantity(); //untuk total price di order
//			stock = c.getProduct().getProductStock() - c.getQuantity(); //ngurangin stock product merchant
//			c.getProduct().setProductStock(stock);
////			if(carts.size()-1 == i) break;
//			if(c.getMerchantName() != c.getMerchantName()+1) {
//				tempPrice+=c.getCourier().getCourierPrice();
//			}
//			
//			c.getProduct().getMerchant().getMerchantName();
//			c.setStatus("Paid");
//			//every product sold, then add funds to merchant balance
//			String merchantName = c.getProduct().getMerchant().getMerchantName();
//			Merchant merchant = merchantRepository.findByMerchantName(merchantName);
//			merchant.setMerchantBalance(tempPrice);
//		}
//		order.setTotal_price(tempPrice);
//		order.setStatus("Paid");
//		User user = userRepository.findById(order.getUser().getId()).get();
//	
//		Integer invSeq = 0;
//		if(user.getOrder() != null) invSeq = user.getOrderSequence();
//		invSeq++;
//		user.setOrderSequence(invSeq);
//
//					
//		userService.applyInvoiceNow(user_id, user);
//
//		return cartDetailRepository.saveAll(carts);
//	}

	//loadAll order ada 2 kategori, untuk:
	//1. cart : load buat sblm checkout
	//2. order : buat history pembelanjaan
	
	//1.
	@Transactional
	public List<Orders> loadAllOrderByUserIdForCart(Long user_id, String username) {
		String status = "Not Paid";
		cartService.countOrderPriceAndStock(username);
		List<Orders> order = orderRepository.findAllByUserIdAndStatus(user_id, status);
//		for(int i=0; i<order.size(); i++) {
//			Orders toRemove = order.get(i);
//			if(toRemove.getCart_detail().isEmpty()) {
//				order.remove(toRemove);
////				orderRepository.save(order.get(i));
//				orderRepository.delete(toRemove);
//			}
//		}
		
		return order;
	}
	
	//2.
	public List<Orders> loadAllOrderByUserIdForHistory(Long user_id) {
		String status = "Not Paid";
		//find all order history if status != not paid
		List<Orders> list = orderRepository.findAllByUserIdAndStatusForHistory(user_id, status);
		return list;
	}
	
	public List<Orders> countAllTotalForCart(Long user_id) {
		
		User user = userRepository.findById(user_id).get();
		
		String status = "Not Paid";
		List<Orders> order = orderRepository.findTopByUsernameAndStatus(user.getUsername(), status);	
		return order;
	}
	
//	public Integer countTotalItemForCart(Long user_id) {
//		String status = "Not Paid";
//		List<Orders> order = orderRepository.findAllByUserIdAndStatusForHistory(user_id, status);
//		return order.get(0).getTotal_item();
//	}
	
//	public Orders findDetailOrder(String order_identifier) {
//		
//		Orders order;
//		try {
//			order = orderRepository.findByOrderIdentifier(order_identifier);
//		} catch (Exception e) {
//			throw new OrderNotFoundException("Something wrong went load order detail");
//		}
//		
//		return order;
//	}
	
}

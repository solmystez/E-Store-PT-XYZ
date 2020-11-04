package com.demo.lookopediaSinarmas.services;

import java.util.List;

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
	
	
	public Iterable<Cart> processItem(Cart cart, String order_identifier, Long user_id) {	
		//====quest====
		//-. kalkulasi harga disini ('total_price' Orders attribute)
		//-. totalHarga+= voucher apa
		//-. totalHarga+= courier apa
		//-. --Stock product di merchant
		//-. set transaction status 'Not Paid'
		//-. generate order_number
		
		Orders order = orderRepository.findByOrderIdentifier(order_identifier);
		if(order == null) throw new OrderNotFoundException("order not found");
		
		//1. temuin cart yg mana yg mau di proses + product apa aja 
		//brati findByOrderIdentifier > tampung ke list
		//=======gmn gw mainin object masing" nya ?
		//product A di --Stockny 
		
		List<Cart> carts = cartDetailRepository.findAllByOrderIdentifier(order_identifier, Sort.by(Sort.Direction.ASC,"merchantName"));
		
		int tempPrice = 0;
		
		Courier courier = courierRepository.findByCourierName(cart.getCourierName());
		
		for(int i=0; i<carts.size(); i++) {
//			System.out.println(carts);
			int stock = 0;
			carts.get(i).setCourier(courier);
			if(carts.get(i).getMerchantName() != carts.get(i).getMerchantName()+1) {
				tempPrice+=carts.get(i).getCourier().getCourierPrice();
			}
			
			tempPrice += carts.get(i).getP_price() * carts.get(i).getQuantity(); //untuk total price di order
			stock = carts.get(i).getProduct().getProductStock() - carts.get(i).getQuantity(); //ngurangin stock product merchant
			carts.get(i).getProduct().setProductStock(stock);
			
			carts.get(i).getProduct().getMerchant().getMerchantName();
			
			//every product sold, then add funds to merchant balance
			String merchantName = carts.get(i).getProduct().getMerchant().getMerchantName();
			Merchant merchant = merchantRepository.findByMerchantName(merchantName);
			merchant.setMerchantBalance(tempPrice);
		}
		
		order.setTotal_price(tempPrice);
			
		User user = userRepository.findById(order.getUser().getId()).get();
	
		Integer invSeq = 0;
		if(user.getOrder() != null) invSeq = user.getOrderSequence();
		invSeq++;
		user.setOrderSequence(invSeq);

					
		userService.applyInvoiceNow(user_id, user);

		return cartDetailRepository.saveAll(carts);
	}

	public List<Orders> loadAllOrderByUserId(Long user_id) {
		//task : validate last orderData
		List<Orders> list = orderRepository.findAllByUserId(user_id);

		if(list.size() > 1) {
			list.remove(list.size()-1);
			return list;
		}
		return list;
	}
	
	public Orders findDetailOrder(String order_identifier) {
		
		Orders order;
		try {
			order = orderRepository.findByOrderIdentifier(order_identifier);
		} catch (Exception e) {
			throw new OrderNotFoundException("Something wrong went load order detail");
		}
		
		return order;
	}
	
}

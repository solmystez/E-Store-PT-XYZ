package com.demo.lookopediaSinarmas.services;

import com.demo.lookopediaSinarmas.entity.Wallet;
import com.demo.lookopediaSinarmas.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Category;
import com.demo.lookopediaSinarmas.entity.Courier;
import com.demo.lookopediaSinarmas.exceptions.product.ProductIdException;
import com.demo.lookopediaSinarmas.repositories.CourierRepository;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    public Wallet createOrUpdateWallet(Wallet wallet) {
        wallet.setWalletBalance(wallet.getWalletBalance());
        wallet.setWalletName(wallet.getWalletName());

        return walletRepository.save(wallet);
    }

    public Iterable<Wallet> getWalletList(){
        return walletRepository.findAll();
    }

    public void deleteWalletById(Long wallet_id) {

        try {
            Wallet wallet = walletRepository.findById(wallet_id).get();
            walletRepository.delete(wallet);
        } catch (Exception e) {
            throw new ProductIdException("Wallet with ID '" + wallet_id +"' cannot delete because doesn't exists");
        }
    }

}

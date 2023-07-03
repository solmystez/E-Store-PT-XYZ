package com.demo.lookopediaSinarmas.repositories;

import com.demo.lookopediaSinarmas.entity.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Courier;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long>{

    Wallet findByWalletName(String walletName);
}

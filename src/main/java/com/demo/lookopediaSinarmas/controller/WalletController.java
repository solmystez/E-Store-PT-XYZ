package com.demo.lookopediaSinarmas.controller;

import javax.validation.Valid;

import com.demo.lookopediaSinarmas.entity.Courier;
import com.demo.lookopediaSinarmas.entity.Wallet;
import com.demo.lookopediaSinarmas.services.WalletService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/create")
    public ResponseEntity<?> createCourier(@Valid @RequestBody Wallet wallet, BindingResult result){

        ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
        if(mapError != null) return mapError;

        Wallet wallet1 = walletService.createOrUpdateWallet(wallet);
        return new ResponseEntity<Wallet>(wallet1, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public Iterable<Wallet> loadAllWallets(){
        return walletService.getWalletList();
    }

    @DeleteMapping("/delete/{wallet_id}")
    public ResponseEntity<?> deleteCourier(@PathVariable Long wallet_id){
        walletService.deleteWalletById(wallet_id);
        return new ResponseEntity<String>("Wallet ID '" + wallet_id  + "' was successfully deleted", HttpStatus.OK);
    }

}

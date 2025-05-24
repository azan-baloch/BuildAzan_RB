package com.buildazan.restcontrollers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.entities.Store;
import com.buildazan.projection.StoreProjection;
import com.buildazan.service.StoreService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/create-store")
    public ResponseEntity<?> createStore(@RequestBody Map<String, String> payload){
        try {
            String storeName = payload.get("storename") + ".buildazan.com";
            storeService.createStore(new Store(payload.get("userId"), storeName));
            return ResponseEntity.ok().build();
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Store name is already registered, try other name"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/find-current-store")
    public ResponseEntity<?> findCurrentStore(@RequestParam("storeId") String storeId) {
        try {
            Store store = storeService.findCurrentStoreById(storeId).get();
            return ResponseEntity.ok(store);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/find-stores")
    public ResponseEntity<?> findStoresByUserId(@RequestParam String userId){
        try {
            List<StoreProjection> stores = storeService.findStoresByUserId(userId);
            return ResponseEntity.ok(stores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        } 
    }

    @PutMapping("/update-store-general-details")
    public ResponseEntity<?> updateStoreGeneralDetails(@RequestBody Map<String, String> storeDetails) {
        try {
            storeService.updateStoreGeneralDetails(storeDetails);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PutMapping("/update-domain")
    public ResponseEntity<?> updateStoreDomain(@RequestBody Map<String, String> payload) {
        try {
            storeService.updateStoreDomain(payload);
            return ResponseEntity.ok("Domain updated");
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Domain name already exists! Use another name"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @DeleteMapping("/delete-store/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable String storeId){
        try {
            storeService.deleteStore(storeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/find-store-currency")
    public ResponseEntity<?> findStoreCurrency(@RequestParam String domain){
        try {
            return ResponseEntity.ok(storeService.findStoreCurrency(domain));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        } 
    }

}

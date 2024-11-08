package com.buildazan.restcontrollers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.entities.ShippingOption;
import com.buildazan.service.ShippingService;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @PostMapping("/add-shipping")
    public ResponseEntity<?> addShippingOption(@RequestBody Map<String, String> shippingDetails) {
        try {
            shippingService.createShippingOption(shippingDetails);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PutMapping("/update-shipping")
    public ResponseEntity<?> updateShipping(@RequestBody Map<String, String> shippingDetails){
        try {
            shippingService.updateShippingOption(shippingDetails);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/get-shippings")
    public ResponseEntity<?> getAllShippings(@RequestParam String storeId) {
        try {
            return ResponseEntity.ok(shippingService.findShippingByStoreId(storeId));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PutMapping("/update-shipping-status")
    public ResponseEntity<?> updateShippingStatus(@RequestBody Map<String, String> payload) {
        try {
            shippingService.updateShippingStatus(payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ShippingOption>> getShippingById(@PathVariable String id) {
        Optional<ShippingOption> shippingOption = shippingService.findShippingById(id);
        return ResponseEntity.ok(shippingOption);
    }

    @DeleteMapping("/delete-shipping/{id}")
    public ResponseEntity<?> deleteShippingOption(@PathVariable String id) {
        try {
            shippingService.deleteShippingOption(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

}

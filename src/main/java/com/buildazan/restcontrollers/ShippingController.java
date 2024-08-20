package com.buildazan.restcontrollers;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.entities.ShippingOption;
import com.buildazan.service.ShippingService;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @PostMapping("/add-shipping")
    public ResponseEntity<?> addShippingOption(@RequestBody Map<String, Object> shippingDetails ) {
        try {
            ShippingOption shippingOption = shippingService.createShippingOption(shippingDetails);
            return new ResponseEntity<>(shippingOption, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating shipping option: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/shippings")
    public ResponseEntity<List<ShippingOption>> getAllShippings(){
        return ResponseEntity.ok(shippingService.getShippings());
    }

    @PostMapping("/enable")
    public ResponseEntity<?> updateShippingOption(@RequestBody Map<String, Object> payload) {
        String id = (String) payload.get("id");
        boolean enabled = (Boolean) payload.get("enabled");
        try {
            shippingService.updateShippingOption(id, enabled);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating shipping option: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ShippingOption>> getShippingById(@PathVariable String id){
        Optional<ShippingOption> shippingOption = shippingService.findShippingById(id);
        return ResponseEntity.ok(shippingOption);
    }

    @PutMapping("/update-shipping/{id}")
    public ResponseEntity<?> updateShippingOption(@PathVariable String id, @RequestBody Map<String, Object> shippingDetails) {
        try {
            Optional<ShippingOption> existingOptionOpt = shippingService.findShippingById(id);
                ShippingOption existingOption = existingOptionOpt.get();
                shippingService.updateShippingOptionDetails(existingOption, shippingDetails);
                return new ResponseEntity<>(existingOption, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating shipping option: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-shipping/{id}")
    public ResponseEntity<?> deleteShippingOption(@PathVariable String id) {
        try {
            shippingService.deleteShippingOption(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting shipping option: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}

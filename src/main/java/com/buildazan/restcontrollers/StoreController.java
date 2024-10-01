package com.buildazan.restcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.entities.Store;
import com.buildazan.projection.ProductProjection;
import com.buildazan.service.ProductService;
import com.buildazan.service.StoreService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    // @GetMapping("/get-store")
    // public ResponseEntity<?> getStore(@RequestParam("userId") String userId) {
    // Store store = storeService.findStoreByUserId(userId);
    // return store != null ? ResponseEntity.ok(store)
    // : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
    // "error", "Store not found"));
    // }

    // @GetMapping("/find-store")
    // public ResponseEntity<?> findStoreByDomain(@RequestParam("domain") String
    // domain) {
    // Store store = storeService.findByDomain(domain);
    // if (store == null) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",
    // "Store not found"));
    // }
    // List<ProductProjection> products =
    // productService.getProductsByStoreId(store.getStoreId());
    // return ResponseEntity.ok(Map.of("storeDetails", store, "products",
    // products));
    // }

    @GetMapping("/find-store")
    public ResponseEntity<?> findByDomain(@RequestParam("domain") String domain) {
        try {
            AggregationResults<Map<String, Object>> store = storeService.findStoreWithProductsByDomain(domain);
            if (store.getMappedResults().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Store not found"));
            }
            return ResponseEntity.ok(store.getMappedResults().get(0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

}

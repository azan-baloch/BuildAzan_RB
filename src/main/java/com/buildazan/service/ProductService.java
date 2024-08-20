package com.buildazan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Product;
import com.buildazan.repo.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public boolean saveProduct(Product product){
        Product save = productRepo.save(product);
        if (save!=null) {
            return true;
        }
        return false;
    }

    public List<Product> getDemoProducts(){
        return productRepo.findAll();
    }
    public Page<Product> getAllProducts(Pageable pageable){
        return productRepo.findAll(pageable);
    }

    public Optional<Product> getProductById(String id){
        return productRepo.findById(id);
    }

    public Page<Product> getFilteredProducts(Pageable pageable, String categoryId, String status, Boolean stockStatus, String search) {
        return productRepo.findFilteredProducts(categoryId, status, stockStatus, search, pageable);
    }
    
    public void deleteProductById(String id){
        productRepo.deleteById(id);
    }
    
    // bulk actions
    public void bulkEnableProducts(List<String> productIds) {
        List<Product> products = productRepo.findAllById(productIds);
        for (Product product : products) {
            product.setStatus("enabled");
        }
        productRepo.saveAll(products);
    }

    public void bulkDisableProducts(List<String> productIds) {
        List<Product> products = productRepo.findAllById(productIds);
        for (Product product : products) {
            product.setStatus("disabled");
        }
        productRepo.saveAll(products);
    }

    public void bulkDeleteProducts(List<String> productIds) {
        productRepo.deleteAllById(productIds);
    }

}


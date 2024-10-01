package com.buildazan.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildazan.entities.Product;
import com.buildazan.projection.ProductProjection;

public interface ProductCustomRepo {
    Page<ProductProjection> getAllProducts(String storeId, Pageable pageable);
    Page<Product> getFilteredProducts(String storeId, String categoryId, String status, Boolean stockStatus, String search, Pageable pageable);
}



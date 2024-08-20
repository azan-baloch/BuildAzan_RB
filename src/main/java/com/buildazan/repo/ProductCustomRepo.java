package com.buildazan.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildazan.entities.Product;

import reactor.core.publisher.Flux;

public interface ProductCustomRepo {
    Page<Product> findFilteredProducts(String categoryId, String status, Boolean stockStatus, String search, Pageable pageable);
    // Flux<Product> findFilteredProducts(String categoryId, String status, Boolean stockStatus, String search, Pageable pageable);
}


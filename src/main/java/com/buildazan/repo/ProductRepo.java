package com.buildazan.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Product;

@Repository
public interface ProductRepo extends MongoRepository<Product, String>, ProductCustomRepo {
}   



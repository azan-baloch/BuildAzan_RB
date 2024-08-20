package com.buildazan.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Store;

@Repository
public interface StoreRepo extends MongoRepository<Store, String> {
    
}

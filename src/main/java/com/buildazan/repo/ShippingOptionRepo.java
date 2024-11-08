package com.buildazan.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.ShippingOption;

@Repository
public interface ShippingOptionRepo extends MongoRepository<ShippingOption, String>{
    List<ShippingOption> findAllByStoreId(String storeId);
    
}


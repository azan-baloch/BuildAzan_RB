package com.buildazan.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Carrier;

@Repository
public interface CarrierRepo extends MongoRepository<Carrier, String>{
    
}

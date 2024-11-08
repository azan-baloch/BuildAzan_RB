package com.buildazan.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Store;
import com.buildazan.repo.StoreRepo;

@Service
public class StoreService {

    @Autowired
    private StoreRepo storeRepo;

    // public Store findStoreByUserId(String userId){  
    //     return storeRepo.findStoreByUserId(userId);
    // }

    public Store findByDomain(String domain){
        return storeRepo.findByDomain(domain);
    }

    public AggregationResults<Map<String, Object>> findStoreWithProductsByDomain(String domain){
        return storeRepo.findStoreWithProductsByDomain(domain);
    }

    public AggregationResults<Map<String, Object>> findAllSlugsForStore(String domain){
        return storeRepo.findAllSlugsForStore(domain);
    }
    
}

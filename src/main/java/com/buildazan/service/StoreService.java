package com.buildazan.service;

import java.util.List;
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

    @Autowired
    private PageService pageService;

    // public Store findStoreByUserId(String userId){  
    //     return storeRepo.findStoreByUserId(userId);
    // }

    public void createStore(Store store){
        Store createdStore = storeRepo.save(store); 
        String createdStoreId = createdStore.getStoreId();
        pageService.createDefaultPages(createdStoreId);
    }

    public List<Store> findStoresByUserId(String userId){
        return storeRepo.findStoresByUserId(userId);
    }

    public List<Store> findStoresByIds(List<String> storeIds){
        return storeRepo.findAllById(storeIds);
    }

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

package com.buildazan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buildazan.entities.Store;
import com.buildazan.projection.StoreCurrencyProjection;
import com.buildazan.projection.StoreProjection;
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

    @Transactional
    public void createStore(Store store){
        System.out.println(store.getDomain());
        Store createdStore = storeRepo.save(store); 
        pageService.createDefaultPages(createdStore.getStoreId(), createdStore.getDomain());
    }

    public Optional<Store> findCurrentStoreById(String storeId){
        return storeRepo.findById(storeId);
    }

    public List<StoreProjection> findStoresByUserId(String userId){
        return storeRepo.findStoresByUserId(userId);
    }

    public void updateStoreGeneralDetails(Map<String, String> storeDetails){
        storeRepo.updateStoreGeneralDetails(storeDetails);
    }

    public void updateStoreDomain(Map<String, String> payload){
        storeRepo.udpateStoreDomain(payload);
    }

    public void deleteStore(String storeId){
        storeRepo.deleteById(storeId);
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

    public StoreCurrencyProjection findStoreCurrency(String domain){
        return storeRepo.findCurrencyByDomain(domain);
    }
    
}

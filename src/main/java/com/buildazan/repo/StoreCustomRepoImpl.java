package com.buildazan.repo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.buildazan.entities.Store;

public class StoreCustomRepoImpl implements StoreCustomRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateStoreGeneralDetails(Map<String, String> details) {
        Query query = new Query();
        query.addCriteria(Criteria.where("storeId").is(details.get("storeId")));
        Update update = new Update();
        update.set("storeEmail", details.get("storeEmail"));
        update.set("storePhone", details.get("storePhone"));
        update.set("address", details.get("address"));
        update.set("currency", details.get("currency"));
        mongoTemplate.updateFirst(query, update, Store.class);
    }

    @Override
    public void udpateStoreDomain(Map<String, String> payload) {
        Query query = new Query();
        query.addCriteria(Criteria.where("storeId").is(payload.get("storeId")));
        Update update = new Update();
        update.set("domain", payload.get("domain"));
        mongoTemplate.updateFirst(query, update, Store.class);
    }

}

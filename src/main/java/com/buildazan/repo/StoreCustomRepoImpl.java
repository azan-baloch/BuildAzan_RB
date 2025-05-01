package com.buildazan.repo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.buildazan.entities.Page;
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

        String storeId = payload.get("storeId");
        String newDomain = payload.get("domain");

        // 1) Updating the Store document
        Query storeQ = new Query(Criteria.where("storeId").is(storeId));
        Update storeU = new Update().set("domain", newDomain);
        mongoTemplate.updateFirst(storeQ, storeU, Store.class);

        // 2) Updating *all* Page documents for that store
        Query pageQ = new Query(Criteria.where("storeId").is(storeId));
        Update pageU = new Update().set("storeDomain", newDomain);
        mongoTemplate.updateMulti(pageQ, pageU, Page.class);
    }

}

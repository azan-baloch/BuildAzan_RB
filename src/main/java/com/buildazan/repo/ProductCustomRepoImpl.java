package com.buildazan.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.buildazan.entities.Product;

public class ProductCustomRepoImpl implements ProductCustomRepo {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Product> findFilteredProducts(String categoryId, String status, Boolean stockStatus, String search, Pageable pageable) {
        Query query = new Query();
        if (categoryId != null && !categoryId.isEmpty()) {
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        }
        if (status != null && !status.isEmpty()) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        if (stockStatus != null) {
            query.addCriteria(Criteria.where("stockStatus").is(stockStatus));
        }
        if (search != null && !search.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(search, "i"));
        }
        //  counting total pages without any pagination in query
        long count = mongoTemplate.count(query, Product.class);
        // adding pagination in with query
        query.with(pageable);
        List<Product> products = mongoTemplate.find(query, Product.class);
        System.out.println(count);
        return new PageImpl<>(products, pageable, count);
    }
}

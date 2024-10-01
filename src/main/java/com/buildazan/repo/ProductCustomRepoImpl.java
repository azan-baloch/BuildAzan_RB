package com.buildazan.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.buildazan.entities.Product;
import com.buildazan.projection.ProductProjection;

public class ProductCustomRepoImpl implements ProductCustomRepo {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<ProductProjection> getAllProducts(String storeId, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("storeId").is(storeId));
        query.with(Sort.by(Sort.Direction.DESC, "createdDate"));

        query.fields().include("id", "storeId", "name", "price", "discountPrice", "createdDate", "trackInventory", "stockQuantity", "stockStatus", "productImage");
        long count = mongoTemplate.count(query, Product.class);
        query.with(pageable);
        List<ProductProjection> products = mongoTemplate.find(query, ProductProjection.class);
        return new PageImpl<>(products, pageable, count);
    }

    @Override
    public Page<Product> getFilteredProducts(String storeId, String categoryId, String status, Boolean stockStatus, String search, Pageable pageable) {
        Query query = new Query();
        
        // Add criteria to filter by storeId
        query.addCriteria(Criteria.where("storeId").is(storeId));

        // Add additional filters if provided
        if (categoryId != null && !categoryId.isEmpty()) {
            System.out.println("category filter");
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        }
        if (status != null && !status.isEmpty()) {
            System.out.println("status filter");
            query.addCriteria(Criteria.where("status").is(status));
        }
        if (stockStatus != null) {
            System.out.println("stock status filter");
            query.addCriteria(Criteria.where("stockStatus").is(stockStatus));
        }
        if (search != null && !search.isEmpty()) {
            System.out.println("search filter");
            query.addCriteria(Criteria.where("name").regex(search, "i"));
        }
        System.out.println("...............");
        query.with(Sort.by(Sort.Direction.DESC, "createdDate"));
        // Count total products for pagination
        long count = mongoTemplate.count(query, Product.class);

        // Apply pagination to the query
        query.with(pageable);

        // Fetch products based on filters
        List<Product> products = mongoTemplate.find(query, Product.class);
        return new PageImpl<>(products, pageable, count);
    }
}


package com.buildazan.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Store;
import com.buildazan.projection.StoreProjection;

@Repository
public interface StoreRepo extends MongoRepository<Store, String>, StoreCustomRepo {

    @Query(value = "{'userId': ?0}")
    List<StoreProjection> findStoresByUserId(String userId);
    
    @Query(value = "{'domain' : ?0}")
    Store findByDomain(String domain);

    @Aggregation(pipeline = {
            "{ $match: { 'domain': ?0 } }",
            "{ $addFields: { storeIdToString: { $toString: '$_id' } } }",
            "{ $lookup: { 'from': 'products', 'localField': 'storeIdToString', 'foreignField': 'storeId', 'as': 'products' } }",
            "{ $project: { 'storeDetails': '$$ROOT', 'products': 1 } }"
    })
    AggregationResults<Map<String, Object>> findStoreWithProductsByDomain(String domain);

    @Aggregation(pipeline = {
            "{ $match: { 'domain': ?0 } }",
            "{ $addFields: { storeIdToString: { $toString: '$_id' } } }",
            "{ $lookup: { 'from': 'products', 'localField': 'storeIdToString', 'foreignField': 'storeId', 'as': 'products' } }",
            "{ $unwind: '$products' }", // Unwind products array to work with individual products
            "{ $match: { 'products.slug': ?1 } }", // Match the product by its slug
            "{ $project: { 'products': 1 } }" // Return the matching product
    })
    AggregationResults<Map<String, Object>> findStoreIdWithProduct(String domain, String productSlug);

    @Aggregation(pipeline = {
            "{ $match: {'domain': ?0}}",
            "{ $addFields: { storeIdToString: { $toString: '$_id' } } }",
            "{ $lookup: { 'from': 'products', 'localField': 'storeIdToString', 'foreignField': 'storeId', 'as': 'products' } }",
            "{ $lookup: { 'from': 'categories', 'localField': 'storeIdToString', 'foreignField': 'storeId', 'as': 'categories' } }",
            "{ $project: { 'products.slug': 1, 'categories.slug': 1 } }"
    })
    AggregationResults<Map<String, Object>> findAllSlugsForStore(String domain);
}

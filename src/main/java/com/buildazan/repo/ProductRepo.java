package com.buildazan.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Product;
import com.buildazan.projection.ProductProjection;

@Repository
public interface ProductRepo extends MongoRepository<Product, String>, ProductCustomRepo {
    @Query(value = "{ 'storeId': ?0 }", 
           fields = "{ 'id': 1, 'storeId': 1, 'name': 1, 'price': 1, 'discountPrice': 1, 'createdDate': 1, 'trackInventory': 1, 'stockQuantity': 1, 'stockStatus': 1, 'productImage': 1 }")
    Page<ProductProjection> findAllByStoreId(String storeId, Pageable pageable);

    @Query(value = "{ 'storeId': ?0 }", 
           fields = "{ 'id': 1, 'storeId': 1, 'name': 1, 'price': 1, 'discountPrice': 1, 'createdDate': 1, 'trackInventory': 1, 'stockQuantity': 1, 'stockStatus': 1, 'productImage': 1, 'status': 1 }")
    List<ProductProjection> findByStoreId(String storeId);

    @Query(value = "{ 'storeName': ?0 }", 
           fields = "{ 'id': 1, 'storeId': 1, 'name': 1, 'price': 1, 'discountPrice': 1, 'createdDate': 1, 'trackInventory': 1, 'stockQuantity': 1, 'stockStatus': 1, 'productImage': 1, 'status': 1 }")
    List<ProductProjection> findByStoreName(String storeName);

    Product findByStoreIdAndSlug(String storeId, String slug);
}   



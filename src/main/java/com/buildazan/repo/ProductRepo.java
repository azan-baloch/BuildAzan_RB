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
import com.buildazan.projection.SlugProjection;

@Repository
public interface ProductRepo extends MongoRepository<Product, String>, ProductCustomRepo {
    @Query(value = "{ 'storeId': ?0 }", 
           fields = "{ 'id': 1, 'storeId': 1, 'slug': 1, 'name': 1, 'price': 1, 'discountPrice': 1, 'createdDate': 1, 'trackInventory': 1, 'stockQuantity': 1, 'stockStatus': 1, 'productImage': 1, 'status': 1, 'categoryId': 1 }")
    Page<ProductProjection> findAllProjectionByStoreId(String storeId, Pageable pageable);

    @Query(value = "{ 'storeId': ?0 }", 
           fields = "{ 'id': 1, 'storeId': 1, 'slug': 1, 'name': 1, 'price': 1, 'discountPrice': 1, 'createdDate': 1, 'trackInventory': 1, 'stockQuantity': 1, 'stockStatus': 1, 'productImage': 1, 'status': 1, 'categoryId': 1 }")
    List<ProductProjection> findAllProjectionByStoreId(String storeId);

    List<Product> findAllProductsByStoreId(String storeId);

    Product findByStoreIdAndSlug(String storeId, String slug);

    @Query(value = "{ 'storeId': ?0}")
    List<SlugProjection> findSlugsByStoreId(String storeId);

    @Query(value = "{ 'storeId': ?0, 'categoryId': ?1 }", 
           fields = "{ 'id': 1, 'storeId': 1, 'slug': 1, 'name': 1, 'price': 1, 'discountPrice': 1, 'createdDate': 1, 'trackInventory': 1, 'stockQuantity': 1, 'stockStatus': 1, 'productImage': 1, 'status': 1, 'categoryId': 1 }")
    Page<ProductProjection> findByStoreIdAndCategoryId(String storeId, String categoryId, Pageable pageable);

    long countByStoreId(String storeId);
}   



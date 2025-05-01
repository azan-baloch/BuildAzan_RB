package com.buildazan.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Category;
import com.buildazan.projection.SlugProjection;

@Repository
public interface CategoryRepo extends MongoRepository<Category, String> {
    @Query(value = "{ 'storeId': ?0}")
    List<SlugProjection> findSlugsByStoreId(String storeId);

    long countByStoreId(String storeId);
}

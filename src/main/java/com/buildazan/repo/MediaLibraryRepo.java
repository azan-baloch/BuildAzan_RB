package com.buildazan.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.MediaLibrary;

@Repository
public interface MediaLibraryRepo extends MongoRepository<MediaLibrary, String> {
    public MediaLibrary findByStoreId(String storeId);
}

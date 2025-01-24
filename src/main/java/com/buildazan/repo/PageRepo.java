package com.buildazan.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Page;

@Repository
public interface PageRepo extends MongoRepository<Page, String>{
    public Page findPageBySlugAndStoreId(String slug, String storeId);
    public List<Page> findPageByStoreId(String storeId);
    // public void updatePageByStoreIdAndId(String storeId, String pageId);
    // public void deleteById(String pagedId);
}

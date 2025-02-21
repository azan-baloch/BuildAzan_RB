package com.buildazan.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Page;
import com.buildazan.projection.PagesProjection;

@Repository
public interface PageRepo extends MongoRepository<Page, String>{
    public Page findPageByStoreDomainAndSlug(String domain, String slug);
    public Page findPageBySlugAndStoreId(String slug, String storeId);
    public List<PagesProjection> findPagesByStoreId(String storeId);
    // public void updatePageByStoreIdAndId(String storeId, String pageId);
    // public void deleteById(String pagedId);
}

package com.buildazan.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.Page;
import com.buildazan.projection.GlobalContentProjection;
import com.buildazan.projection.PagesProjection;

@Repository
public interface PageRepo extends MongoRepository<Page, String>{
    public Page findPageByStoreDomainAndSlug(String domain, String slug);
    public Page findPageBySlugAndStoreId(String slug, String storeId);
    public List<PagesProjection> findPagesByStoreId(String storeId);

    @Query(value = "{'storeId': ?0, 'slug': ?1}", fields = "{'globalContent': 1}")
    public GlobalContentProjection findGlobalContentProjection(String storeId, String slug);
    // public void updatePageByStoreIdAndId(String storeId, String pageId);
    // public void deleteById(String pagedId);
}

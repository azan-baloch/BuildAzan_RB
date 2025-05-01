package com.buildazan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buildazan.entities.Page;
import com.buildazan.projection.GlobalContentProjection;
import com.buildazan.projection.PagesProjection;
import com.buildazan.repo.PageRepo;
import com.buildazan.utils.DefaultPageTemplates;

@Service
public class PageService {

    @Autowired
    private PageRepo pageRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createDefaultPages(String storeId, String storeDomain){
        List<Map<String, Object>> templates = DefaultPageTemplates.getTemplates();
        List<Page> defaultPages = new ArrayList<>();
        for (Map<String,Object> template : templates) {
            defaultPages.add(createPageFromTemplate(template, storeId, storeDomain));
        }
        pageRepo.saveAll(defaultPages);
    }

    public Page createPageFromTemplate(Map<String, Object> template, String storeId, String storeDomain){
        Page page = new Page();
        page.setName((String) template.get("name"));
        page.setStoreId(storeId);
        page.setSlug((String) template.get("slug"));
        page.setStoreDomain(storeDomain);
        page.setDefault((Boolean) template.getOrDefault("default", true));
        Supplier<List<Map<String, Object>>> contentGenerator = (Supplier<List<Map<String, Object>>>) template.get("contentGenerator");
        Object generatedContent = contentGenerator.get();
        if (generatedContent instanceof Map) {
            Map<String, List<Map<String, Object>>> contentMap = (Map<String, List<Map<String, Object>>>) generatedContent;
            page.setGlobalContent(contentMap.get("globalContent"));
            page.setContent(contentMap.get("content"));
        } else if (generatedContent instanceof List) {
            page.setContent((List<Map<String, Object>>) generatedContent);
        }
        return page;
    }

    public void savePage(Page page){
        // page.setContent(List.of(Map.of("type", "customPage")));
        page.setContent(new ArrayList<>());
        pageRepo.save(page); 
    }

    public Optional<Page> getPageById(String pageId){
        return pageRepo.findById(pageId);
    }

    public Optional<Page> getPageByDomainAndSlug(String domain, String slug){
        return pageRepo.findPageByStoreDomainAndSlug(domain, slug);
    }

    public Page getPageBySlugAndStoreId(String slug, String storeId){
        return pageRepo.findPageBySlugAndStoreId(slug, storeId);
    }

    public List<PagesProjection> getPagesByStoreId(String storeId){
        return pageRepo.findPagesByStoreId(storeId);
    }

    public GlobalContentProjection getGlobalContent(String storeDomain){
        return pageRepo.findGlobalContentProjection(storeDomain, "home");
    }

    public void upatePage(Page page){
        pageRepo.save(page);
    };

    public void updatePageGlobalContent(String pageId, Object content){
        Query query = new Query(Criteria.where("id").is(pageId));
        Update update = new Update().set("content", content);
        mongoTemplate.updateFirst(query, update, Page.class);
    }

    public void updatePageContent(String pageId, Object content){
        Query query = new Query(Criteria.where("id").is(pageId));
        Update update = new Update().set("content", content);
        mongoTemplate.updateFirst(query, update, Page.class);
    }

    public void updatePageAllContent(String pageId, Map<String, Object> contentData){
        Query query = new Query(Criteria.where("id").is(pageId));
        Update update = new Update()
                            .set("content", contentData.get("normalContent"))
                            .set("globalContent", contentData.get("globalContent"));
        mongoTemplate.updateFirst(query, update, Page.class);
    }
    

    public void deleteById(String pageId){
        pageRepo.deleteById(pageId);
    }

    public void updateSeo(Map<String, String> data){
        Query query = new Query(Criteria.where("storeId").is(data.get("storeId"))
                                .and("slug").is(data.get("slug")));
        Update update = new Update()
                                .set("metaTitle", data.get("metaTitle"))
                                .set("metaDescription", data.get("metaDescription"));
        mongoTemplate.updateFirst(query, update, Page.class);
    }

    public void updateHeader(Map<String, String> data){
        Query query = new Query(Criteria.where("storeId").is(data.get("storeId"))
                                .and("slug").is(data.get("slug")));
        Update update = new Update()
                                .set("metaTitle", data.get("metaTitle"))
                                .set("metaDescription", data.get("metaDescription"));
        mongoTemplate.updateFirst(query, update, Page.class);
    }
    
}

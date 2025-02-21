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
        page.setContent(contentGenerator.get());
        return page;
    }

    public void savePage(Page page){
        pageRepo.save(page);
    }

    public Optional<Page> getPageById(String pageId){
        return pageRepo.findById(pageId);
    }

    public Page getPageByDomainAndSlug(String domain, String slug){
        return pageRepo.findPageByStoreDomainAndSlug(domain, slug);
    }

    public Page getPageBySlugAndStoreId(String slug, String storeId){
        return pageRepo.findPageBySlugAndStoreId(slug, storeId);
    }

    public List<PagesProjection> getPagesByStoreId(String storeId){
        return pageRepo.findPagesByStoreId(storeId);
    }

    public void upatePage(Page page){
        pageRepo.save(page);
    }

    public void updatePageContent(String pageId, Object content){
        Query query = new Query(Criteria.where("id").is(pageId));
        Update update = new Update().set("content", content);
        mongoTemplate.updateFirst(query, update, Page.class);
    }

    public void deleteById(String pageId){
        pageRepo.deleteById(pageId);
    }
    
}

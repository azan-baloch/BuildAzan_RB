package com.buildazan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Page;
import com.buildazan.repo.PageRepo;

@Service
public class PageService {

    @Autowired
    private PageRepo pageRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void savePage(Page page){
        pageRepo.save(page);
    }

    public Page getPage(String slug){
        return pageRepo.findPageBySlug(slug);
    }

    public void updatePageContent(String pageId, Object content){
        Query query = new Query(Criteria.where("id").is(pageId));
        Update update = new Update().set("content", content);
        mongoTemplate.updateFirst(query, update, Page.class);
    }
    
}

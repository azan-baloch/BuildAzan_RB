package com.buildazan.service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Store;
import com.buildazan.entities.User;

@Service
public class AsyncService {

    @Autowired
    private PageService pageService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Async
    public CompletableFuture<User> saveUser(User user) {
        System.out.println("saveUser called on thread: " + Thread.currentThread().getName() + " at " + LocalDateTime.now());
        mongoTemplate.save(user);
        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<Void> saveStore(Store store) {
        System.out.println("saveStore called on thread: " + Thread.currentThread().getName() + " at " + LocalDateTime.now());
        mongoTemplate.save(store);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> saveDefaultPages(String storeId) {
        System.out.println("saveDefaultPages called on thread: " + Thread.currentThread().getName() + " at " + LocalDateTime.now());
        pageService.createDefaultPages(storeId);
        return CompletableFuture.completedFuture(null);
    }
    
}

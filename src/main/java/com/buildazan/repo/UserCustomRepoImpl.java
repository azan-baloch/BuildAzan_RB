package com.buildazan.repo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.buildazan.entities.User;

public class UserCustomRepoImpl implements UserCustomRepo{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateEmailByEmail(String id, String newEmail) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("email", newEmail);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    @Override
    public void updateUserGeneralDetails(String id, Map<String, Object> userDetails) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();

        for(Map.Entry<String, Object> entry : userDetails.entrySet()){
            update.set(entry.getKey(), entry.getValue());
        }
        mongoTemplate.updateFirst(query, update, User.class);
    }

    @Override
    public void updatePasswordById(String id, String password) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("password", password);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    // @Override
    // public boolean existsByEmail(String email) {
    //     Query query = new Query(Criteria.where("email").is(email));
    //     return mongoTemplate.exists(query, User.class);
    // }

    
}

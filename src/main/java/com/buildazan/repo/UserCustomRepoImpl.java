package com.buildazan.repo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.buildazan.entities.User;
import com.mongodb.client.result.UpdateResult;

public class UserCustomRepoImpl implements UserCustomRepo {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateEmailByEmail(String id, String newEmail) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(id)),
                new Update().set("email", newEmail),
                User.class);
    }

    @Override
    public void updateUserGeneralDetails(String id, Map<String, Object> userDetails) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();

        for (Map.Entry<String, Object> entry : userDetails.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        mongoTemplate.updateFirst(query, update, User.class);
    }

    @Override
    public void updatePasswordById(String id, String password) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(id)),
                new Update().set("password", password),
                User.class);
    }

    @Override
    public void updateCodeAndExpiryByEmail(String email, String verificationCode, LocalDateTime expirationTime) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("email").is(email)),
                new Update()
                        .set("verificationCode", verificationCode)
                        .set("expirationTime", expirationTime),
                User.class);
    }

    @Override
    public void updateEmailVerifiedByEmail(String email, boolean emailVerified) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("email").is(email)),
                new Update().set("emailVerified", emailVerified),
                User.class);
    }

    @Override
    public UpdateResult verifyEmailAndGenerateNewCode(String email, String verificationCode) {
        return mongoTemplate.updateFirst(
                new Query(Criteria.where("email").is(email)
                .and("verificationCode").is(verificationCode)
                .and("expirationTime").gt(new Date(System.currentTimeMillis() - 15 * 60 * 1000))),
                new Update()
                        .set("emailVerified", true)
                        .set("verificationCode", UUID.randomUUID()),
                User.class);
    }

}

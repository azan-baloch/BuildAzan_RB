package com.buildazan.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.User;
import com.buildazan.projection.UserExpirationTimeProjection;

@Repository
public interface UserRepo extends MongoRepository<User, String>, UserCustomRepo {
    User findUserByUsername(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query(value = "{'email': ?0, 'verificationCode': ?1}", fields = "{'expirationTime': 1, '_id': 0}")
    UserExpirationTimeProjection findByEmailAndVerificationCode(String email, String verificationCode);

}
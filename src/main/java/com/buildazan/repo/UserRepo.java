package com.buildazan.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.User;

@Repository
public interface UserRepo extends MongoRepository<User, String>, UserCustomRepo{
	User findUserByUsername(String username);
    User findByEmail(String email);
    User findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
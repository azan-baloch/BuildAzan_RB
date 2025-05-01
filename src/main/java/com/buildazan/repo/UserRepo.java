package com.buildazan.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.buildazan.entities.User;
import com.buildazan.projection.PasswordProjection;
import com.buildazan.projection.PaymentProjection;
import com.buildazan.projection.UserExpirationTimeProjection;
import com.buildazan.projection.UserProjection;

@Repository
public interface UserRepo extends MongoRepository<User, String>, UserCustomRepo {

    User findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query(value = "{'email': ?0, 'verificationCode': ?1}", fields = "{'expirationTime': 1, '_id': 0}")
    UserExpirationTimeProjection findByEmailAndVerificationCode(String email, String verificationCode);

    @Query(value = "{'id': ?0}", fields = "{'email': 1, 'username': 1, 'phoneNumber': 1, 'currency': 1, 'country': 1, 'gender': 1, 'dateOfBirth': 1, 'profilePicture': 1}")
    UserProjection findUserById(String id);

    @Query(value = "{'id': ?0}", fields = "{'subscriptionStatus': 1, 'memberShipLevel': 1, 'subscriptionStartDate': 1, 'subscriptionEndDate': 1}")
    PaymentProjection findPaymentById(String userId);

    @Query(value = "{'id': ?0}", fields = "{'password': 1}")
    PasswordProjection findPasswordById(String id);

    boolean existsByIdAndTrialUsedTrue(String userId);



}
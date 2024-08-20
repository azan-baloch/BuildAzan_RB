package com.buildazan.service;

import com.buildazan.entities.User;
import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    String generateSecurePassword(String password);

    User userInitializer(Map<String, String> caredentials);

    User saveUser(User user);

    Optional<User> findUserById(String id);

    User findUserByUsernameOrEmail(String usernameOrEmail);

    User findUserByEmail(String email);

    void updateUser(User updatedUser);

    void updateEmailByEmail(String id, String newEmail);

    void updatePaasswordById(String id, String password);

    void updateUserGeneralDetails(String id, Map<String, Object> userDetails);

    void deleteUserById(String userId);

    List<User> getAllUsers();

    long countTotalUsers();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void disableUser(String username);

    SubscriptionStatus checkSubscriptionStatus(String username);

    void updateSubscription(String username, SubscriptionStatus subscriptionStatus);

    void updateMembership(String username, MemberShipLevel memberShipLevel);
}

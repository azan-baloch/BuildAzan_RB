package com.buildazan.service.impl;

import com.buildazan.entities.Store;
import com.buildazan.entities.User;
import com.buildazan.entities.UserRole;
import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;
import com.buildazan.repo.StoreRepo;
import com.buildazan.repo.UserRepo;
import com.buildazan.service.UserService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String generateSecurePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public User userInitializer(Map<String, String> caredentials) {
        User user = new User();
        user.setId(new ObjectId().toString());
        user.setUsername(caredentials.get("username"));
        user.setEmail(caredentials.get("email"));
        user.setFirstName(caredentials.get("firstName"));
        user.setLastName(caredentials.get("lastName"));
        user.setPhoneNumber(caredentials.get("phoneNumber"));
        user.setCountry(caredentials.get("country"));
        user.setCurrency(caredentials.get("currency"));
        user.setPassword(generateSecurePassword(caredentials.get("password")));
        user.setTermsAndConditionsAgreed(true);

        //static details
        user.setUserRole(UserRole.ROLE_USER);
        user.setEmailVerified(false);
        user.setPasswordLastChangedDate(LocalDateTime.now());
        user.setAccountEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCaredentialsNonExpired(true);
        user.setRegistrationTimestamp(LocalDateTime.now());
        user.setPrivacyPolicyAgreed(true);
        user.setProfilePicture("defaultProfileImg.png");
        user.setSubscriptionStatus(SubscriptionStatus.NONE);
        user.setMemberShipLevel(MemberShipLevel.NONE);
        
        Store store = new Store();
        store.setStoreId(new ObjectId().toString());
        store.setUserId(user.getId());
        store.setStoreName(caredentials.get("storeName"));
        store.setSubDomain(caredentials.get("storeName").toLowerCase().replaceAll(" ", "-"));
        store.setCustomDomain(caredentials.get("storeName").toLowerCase().replaceAll(" ", "-"));
        store.setCurrency(caredentials.get("currency"));

        user.setStoreIds(Collections.singletonList(store.getStoreId()));
        mongoTemplate.save(user);
        mongoTemplate.save(store);

        return user;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Optional<User> findUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    @Transactional
    public User findUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Override
    @Transactional
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    @Transactional
    public void updateUser(User updatedUser) {
        userRepo.save(updatedUser);
    }

    @Override
    @Transactional
    public void updateEmailByEmail(String id, String newEmail){
        userRepo.updateEmailByEmail(id, newEmail);
    }

    @Override
    public void updatePaasswordById(String id, String password) {
        userRepo.updatePasswordById(id, password);
    }

    @Override
    public void updateUserGeneralDetails(String id, Map<String, Object> userDetails){
        userRepo.updateUserGeneralDetails(id, userDetails);
    }

    @Override
    public void deleteUserById(String userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public long countTotalUsers() {
        return userRepo.count();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public void disableUser(String username) {
        User user = userRepo.findUserByUsername(username);
        if (user != null) {
            user.setAccountEnabled(false);
        }
    }

    @Override
    public SubscriptionStatus checkSubscriptionStatus(String username) {
        User user = userRepo.findUserByUsername(username);
        if (user != null) {
            if (user.getSubscriptionStatus() == SubscriptionStatus.UNPAID) {
                user.setAccountEnabled(false);
                userRepo.save(user);
                return user.getSubscriptionStatus();
            }
        }
        throw new IllegalArgumentException("User not found with username: " + username);
    }

    @Override
    public void updateSubscription(String username, SubscriptionStatus subscriptionStatus) {
        User user = userRepo.findUserByUsername(username);
        if (user != null) {
            user.setSubscriptionStatus(subscriptionStatus);
            user.setSubscriptionStartDate(LocalDate.now());
            user.setSubscriptionEndDate(LocalDate.now().plusDays(user.getMemberShipLevel().getDays()));
            userRepo.save(user);
        }
    }

    @Override
    public void updateMembership(String username, MemberShipLevel memberShipLevel) {
        User user = userRepo.findUserByUsername(username);
        if (user != null) {
            user.setMemberShipLevel(memberShipLevel);
            userRepo.save(user);
        }
    }

    
}

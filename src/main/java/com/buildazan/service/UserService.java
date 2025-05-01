package com.buildazan.service;

import com.buildazan.entities.Store;
import com.buildazan.entities.User;
import com.buildazan.entities.UserRole;
import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;
import com.buildazan.projection.PasswordProjection;
import com.buildazan.projection.PaymentProjection;
import com.buildazan.projection.UserExpirationTimeProjection;
import com.buildazan.projection.UserProjection;
import com.buildazan.repo.UserRepo;
import com.mongodb.client.result.UpdateResult;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Collections;

@Service
public class UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PageService pageService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;
    
    public String generateSecurePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Transactional
    public User userInitializer(Map<String, String> caredentials) {
        User user = new User();
        user.setId(new ObjectId().toString());
        user.setUsername(caredentials.get("username"));
        user.setEmail(caredentials.get("email"));
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
        // if (caredentials.get("subscriptionStatus") != null) {
        //     user.setSubscriptionStatus((SubscriptionStatus) caredentials.get("subscriptionStatus"));
        // }else{
        //     user.setSubscriptionStatus(SubscriptionStatus.TRIAL);
        // }
        user.setSubscriptionStatus(SubscriptionStatus.NONE);
        user.setMemberShipLevel(MemberShipLevel.NONE);
        user.setSubscriptionStartDate(LocalDate.now());
        
        Store store = new Store();
        store.setStoreId(new ObjectId().toString());
        store.setDomain(caredentials.get("storeName").toLowerCase().replaceAll(" ", "-") + ".buildazan.com");
        store.setUserId(user.getId());
        mongoTemplate.save(user); 
        mongoTemplate.save(store);
        pageService.createDefaultPages(store.getStoreId(), store.getDomain());
        
        // CompletableFuture<User> userFuture = asyncService.saveUser(user);
        // CompletableFuture<Void> storeFuture = asyncService.saveStore(store);
        // CompletableFuture<Void> pageFuture = asyncService.saveDefaultPages(store.getStoreId());

        // CompletableFuture.allOf(userFuture, storeFuture, pageFuture).join();

        return user; 
    }

    public UserProjection findUserById(String id){
        return userRepo.findUserById(id);
    }

    public boolean isPasswordCorrect(String id, String password){
        PasswordProjection fetchedPasswrod = userRepo.findPasswordById(id);
        return bCryptPasswordEncoder.matches(password, fetchedPasswrod.getPassword());
    }

    public void updateUser(User updatedUser) {
        userRepo.save(updatedUser);
    }

    public void updateEmailByEmail(String id, String newEmail){
        userRepo.updateEmailById(id, newEmail);
    }

    public void updateUserGeneralDetails(String id, Map<String, Object> userDetails){
        userRepo.updateUserGeneralDetails(id, userDetails);
    }

    public void updateCodeAndExpiryByEmail(String email, String verificationCode, LocalDateTime expirationTime) {
        userRepo.updateCodeAndExpiryByEmail(email, verificationCode, expirationTime);
    }

    public void updateCodeAndExpiryById(String id, String verificationCode, LocalDateTime expirationTime) {
        userRepo.updateCodeAndExpiryById(id, verificationCode, expirationTime);
    }

    public UserExpirationTimeProjection findByEmailAndVerificationCode(String email, String verificationCode){
        return userRepo.findByEmailAndVerificationCode(email, verificationCode);
    }

    public void updateEmailVerifiedByEmail(String email, boolean emailVerified){
        userRepo.updateEmailVerifiedByEmail(email, emailVerified);
    }

    public UpdateResult verifyEmailAndGenerateNewCode(String email, String verificationCode){
        return userRepo.verifyEmailAndGenerateNewCode(email, verificationCode);
    }

    public UpdateResult verifyOtpAndChangeEmail(String id, String email, String otp){
        return userRepo.verifyOtpAndChangeEmail(id, email, otp);
    }
    public UpdateResult verifyOtpByEmail(String otp, String email){
        return userRepo.verifyOtpByEmail(otp, email);
    }

    public void changePassword(String id, String newPassword) {
        userRepo.changePassword(id, newPassword);
    }
    public void changePasswordByEmail(String email, String newPassword) {
        userRepo.changePasswordByEmail(email, newPassword);
    }
 
    public void deleteUserById(String userId) {
        userRepo.deleteById(userId);
    }

    public long countTotalUsers() {
        return userRepo.count();
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public UpdateResult updateUserPayment(Map<String, Object> payload){
        String userId = (String) payload.get("userId");
        String memberShipLevelStr = (String) payload.get("memberShipLevel");
        String subscriptionStatusStr = (String) payload.get("subscriptionStatus");
     
        MemberShipLevel memberShipLevel = MemberShipLevel.valueOf(memberShipLevelStr);
        SubscriptionStatus subscriptionStatus = SubscriptionStatus.valueOf(subscriptionStatusStr);
        
        LocalDate subscriptionStartDate = LocalDate.now();
        String billingCycle = ((String) payload.get("billingCycle")).toLowerCase();
    
        LocalDate subscriptionEndDate;
        if (billingCycle.equalsIgnoreCase("trial")) {
            if (userRepo.existsByIdAndTrialUsedTrue(userId)) {
                throw new IllegalStateException("Trial already used");
            }            
            subscriptionEndDate = subscriptionStartDate.plusDays(3);
        } else {
            int multiplier;
            switch (billingCycle) {
                case "sixmonths":
                    multiplier = 6;
                    break;
                case "yearly":
                    multiplier = 12;
                    break;
                case "monthly":
                default:
                    multiplier = 1;
                    break;
            }
            subscriptionEndDate = subscriptionStartDate.plusMonths(multiplier);
        }
        return userRepo.updateUserPayment(userId, subscriptionStatus, memberShipLevel, subscriptionStartDate, subscriptionEndDate);
    }    

    public PaymentProjection getUserPayment(String userId){
        return userRepo.findPaymentById(userId); 
    }

    
}

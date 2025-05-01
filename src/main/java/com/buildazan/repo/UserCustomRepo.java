package com.buildazan.repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.mongodb.repository.Query;

import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;
import com.buildazan.projection.PaymentProjection;
import com.mongodb.client.result.UpdateResult;

public interface UserCustomRepo {
    void updateEmailById(String id, String newEmail);
    void updateUserGeneralDetails(String id, Map<String, Object> userDetails);
    UpdateResult verifyOtpByEmail(String otp, String email);
    void updateCodeAndExpiryByEmail(String email, String verificationCode, LocalDateTime expirationTime);
    void updateCodeAndExpiryById(String id, String verificationCode, LocalDateTime expirationTime);
    void updateEmailVerifiedByEmail(String email, boolean emailVerified);
    UpdateResult verifyEmailAndGenerateNewCode(String email, String verificationCode);
    UpdateResult verifyOtpAndChangeEmail(String id, String email, String otp);
    void changePassword(String id, String newPassword);
    void changePasswordByEmail(String email, String newPassword);
    UpdateResult updateUserPayment(String userId, SubscriptionStatus subscriptionStatus, MemberShipLevel memberShipLevel, LocalDate subscriptionStartDate, LocalDate subscriptionEndDate);
}

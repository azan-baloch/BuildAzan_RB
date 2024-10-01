package com.buildazan.repo;

import java.time.LocalDateTime;
import java.util.Map;

import com.mongodb.client.result.UpdateResult;

public interface UserCustomRepo {
    void updateEmailById(String id, String newEmail);
    void updateUserGeneralDetails(String id, Map<String, Object> userDetails);
    void updateCodeAndExpiryByEmail(String email, String verificationCode, LocalDateTime expirationTime);
    void updateCodeAndExpiryById(String id, String verificationCode, LocalDateTime expirationTime);
    void updateEmailVerifiedByEmail(String email, boolean emailVerified);
    UpdateResult verifyEmailAndGenerateNewCode(String email, String verificationCode);
    UpdateResult verifyOtpAndChangeEmail(String id, String email, String otp);
    void changePassword(String id, String newPassword);
}

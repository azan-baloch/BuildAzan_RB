package com.buildazan.repo;

import java.time.LocalDateTime;
import java.util.Map;

import com.mongodb.client.result.UpdateResult;

public interface UserCustomRepo {
    void updateEmailByEmail(String id, String newEmail);
    void updateUserGeneralDetails(String id, Map<String, Object> userDetails);
    void updatePasswordById(String id, String password);
    void updateCodeAndExpiryByEmail(String email, String verificationCode, LocalDateTime expirationTime);
    void updateEmailVerifiedByEmail(String email, boolean emailVerified);
    UpdateResult verifyEmailAndGenerateNewCode(String email, String verificationCode);

}

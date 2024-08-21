package com.buildazan.repo;

import java.util.Map;

public interface UserCustomRepo {
    void updateEmailByEmail(String id, String newEmail);
    void updateUserGeneralDetails(String id, Map<String, Object> userDetails);
    void updatePasswordById(String id, String password);
    void updateVerificationCodeAndVerificationExpirationTimeByEmail(String email, String verificationCode);
}

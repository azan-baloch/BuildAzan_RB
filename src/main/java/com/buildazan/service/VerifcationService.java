package com.buildazan.service;

import java.time.LocalDateTime;

public interface VerifcationService {
	String generateVerificationCode();
	boolean sendVerificationCode(String subject, String email);
	boolean verifyCode(String storedVerificationCode, String code);
	boolean checkCodeExpiration(LocalDateTime verificationLocalDateTime);
}

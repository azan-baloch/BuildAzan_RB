package com.buildazan.service;

public interface EmailService {
	
    boolean sendVerificationEmail(String toEmail, String subject, String verificationCode);
}

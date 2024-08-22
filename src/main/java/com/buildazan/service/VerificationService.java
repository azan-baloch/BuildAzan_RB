package com.buildazan.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VerificationService{
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;
	
	private static final int EXPIRATION_THRESHOLD_MINUTES = 10;
	
	public String generateVerificationCode() {
		return UUID.randomUUID().toString();
	}
	
	public boolean sendVerificationCode(String email, String subject, LocalDateTime expirationTime) {
		String randomCode = generateVerificationCode();
		userService.updateCodeAndExpiryByEmail(email, randomCode, expirationTime);
	    String verificationCode = randomCode + ":" + email;
		return emailService.sendVerificationEmail(email, subject, verificationCode);
	}
	
	public boolean checkCodeExpiration(LocalDateTime expirationDateTime) {
		Duration duration = Duration.between(expirationDateTime, LocalDateTime.now());
		System.out.println(duration.toMinutes());
		return duration.toMinutes() < EXPIRATION_THRESHOLD_MINUTES;
	}

	


	


}

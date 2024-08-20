package com.buildazan.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildazan.service.EmailService;
import com.buildazan.service.VerifcationService;

import jakarta.servlet.http.HttpSession;


@Service
public class VerificationServiceImpl implements VerifcationService{
	
	@Autowired
	private EmailService emailService;
	

	@Override
	public String generateVerificationCode() {
		return UUID.randomUUID().toString();
	}

	@Override
	public boolean sendVerificationCode(String subject, String email) {
	    String verificationCode = generateVerificationCode() + ":" + email + ":" + LocalDateTime.now();
	    if (emailService.sendVerificationEmail(email, subject, verificationCode)) {
	        return true;
	    } else {
	        return false;
	    }
	}

	@Override
	public boolean verifyCode(String storedVerificationCode, String code) {
		return storedVerificationCode.equals(code);
	}

	@Override
	public boolean checkCodeExpiration(LocalDateTime verificationCodeLocalDateTime) {
		Duration duration = Duration.between(verificationCodeLocalDateTime, LocalDateTime.now());
		if (duration.toMinutes()>10) {
			return false;
		}
		return true;
	}

	


	


}

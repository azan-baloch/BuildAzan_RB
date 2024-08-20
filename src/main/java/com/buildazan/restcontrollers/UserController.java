package com.buildazan.restcontrollers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.service.VerifcationService;
import com.buildazan.service.impl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private VerifcationService verifcationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/generate-secure")
    public String generateSecure(){
        return verifcationService.generateVerificationCode();
    }

    // @PostMapping("/send-verification-code")
    // public ResponseEntity<Boolean> sendCode(Map<String, Object> payload, HttpSession session) {
    //     String email = (String) payload.get("currentEmail");
    //     boolean codeSent = verifcationService.sendVerificationCode("Verification code", email, session);
    //     return ResponseEntity.ok(codeSent);
    // }

    @PostMapping("/update-email")
    public ResponseEntity<String> updateUserEmail(@RequestBody Map<String, String> payload, HttpSession session) {
        if (verifcationService.verifyCode((String) session.getAttribute("verificationCode"),
                payload.get("verificationCode"))) {
            if (!verifcationService.checkCodeExpiration((LocalDateTime) session.getAttribute("expirationTime"))) {
                return ResponseEntity.ok("Verification code expire, request new one");
            }
            userServiceImpl.updateEmailByEmail(payload.get("id"), payload.get("newEmail"));
            return ResponseEntity.ok("Email updated successfully");
        } else {
            return ResponseEntity.ok("Verifictaion code is wrong");
        }
    }

    @PostMapping("/update-general-details")
    public ResponseEntity<String> updateUserGeneralDetails(@RequestBody Map<String, Object> userDetails) {
        String id = (String) userDetails.remove("id");
        userServiceImpl.updateUserGeneralDetails(id, userDetails);
        return ResponseEntity.ok("Details updated successfully");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> payload) {
        String password = payload.get("newPassword");
        userServiceImpl.updatePaasswordById(payload.get("id"), bCryptPasswordEncoder.encode(password));
        return ResponseEntity.ok("Password updated successfully");
    }

}

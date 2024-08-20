package com.buildazan.restcontrollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.service.UserService;
import com.buildazan.service.VerifcationService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/verification")
public class VerificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerifcationService verifcationService;

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        boolean userExists = userService.existsByEmail(email);
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not registered"));
        }
        try {
            String subject = "Verification link";
            verifcationService.sendVerificationCode(email, subject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Link not sent, try again"));
        }
        return ResponseEntity.ok(userExists);
    }

}

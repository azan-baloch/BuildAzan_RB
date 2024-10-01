package com.buildazan.restcontrollers;

import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.projection.UserExpirationTimeProjection;
import com.buildazan.service.UserService;
import com.buildazan.service.VerificationService;
import com.mongodb.client.result.UpdateResult;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/verification")
public class VerificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService VerificationService;

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        try {
            boolean userExists = userService.existsByEmail(email);
            if (!userExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
        try {
            String subject = "Verification link";
            if (!VerificationService.sendVerificationLink(email, subject, LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not sent"));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Link not sent, try again"));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify-link")
    public ResponseEntity<?> verifyLink(@RequestParam("email") String email, @RequestParam("code") String code) {
        try {
            UpdateResult updateResult = userService.verifyEmailAndGenerateNewCode(email, code);
            if (updateResult.getModifiedCount() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Link tampered or not correct, Wrong credentials"));
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

}

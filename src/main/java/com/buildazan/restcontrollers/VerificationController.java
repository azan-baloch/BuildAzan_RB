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
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.service.UserService;
import com.buildazan.service.VerificationService;

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
        boolean userExists = userService.existsByEmail(email);
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not found"));
        }
        try {
            String subject = "Verification link";
            if (!VerificationService.sendVerificationCode(email, subject)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not sent"));
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Link not sent, try again"));
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/verify-link")
    public ResponseEntity<?> verifyLink(@RequestParam("link") String link){
        System.out.println(link);
        String[] parts = link.split(":", 3);
        String verificationCode = parts[0];
        String email = parts[1];
        LocalDateTime time = LocalDateTime.parse(parts[2]);
        System.out.println(verificationCode);
        System.out.println(email);
        System.out.println(time);
        if (!VerificationService.checkCodeExpiration(time)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Link expired, Request new one"));
        }

        if (!userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Link tempered or not correct, Wrong caredentials: Email"));
        }

        if (!userService.existsByVerificationCode(verificationCode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Link tempered or not correct, Wrong caredentials: Code"));
        }

        System.out.println("Verificaiton successfull");

        return ResponseEntity.ok("Congratulation! verification successfull");
    }

}

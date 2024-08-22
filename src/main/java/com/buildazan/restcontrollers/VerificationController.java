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
        boolean userExists = userService.existsByEmail(email);
        if (!userExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email not found"));
        }
        try {
            String subject = "Verification link";
            if (!VerificationService.sendVerificationCode(email, subject, LocalDateTime.now())) {
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
    public ResponseEntity<?> verifyLink(@RequestParam("email") String email, @RequestParam("code") String code) {

        // UserExpirationTimeProjection userProjection = userService.findByEmailAndVerificationCode(email, verificationCode);

        // if (userProjection == null) {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        //             .body(Map.of("error", "Link tampered or not correct, Wrong credentials"));
        // }

        // if (!VerificationService.checkCodeExpiration(userProjection.getExpirationTime())) {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        //             .body(Map.of("error", "Link expired, Request new one"));
        // }

        // userService.updateEmailVerifiedByEmail(email, true);


        // UpdateResult updateResult = userService.verifyEmailAndGenerateNewCode(email, code);
        // System.out.println("Modified Count: " + updateResult.getModifiedCount());
        // System.out.println("Matched Count: " + updateResult.getMatchedCount());
        // if (updateResult.getModifiedCount()<=0) {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        //             .body(Map.of("error", "Link tampered or not correct, Wrong credentials"));
        // }

        System.out.println("Verificaiton successfull");

        return ResponseEntity.ok().build();
    }

}

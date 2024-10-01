package com.buildazan.restcontrollers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.projection.UserProjection;
import com.buildazan.service.UserService;
import com.buildazan.service.VerificationService;
import com.mongodb.client.result.UpdateResult;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verifcationService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@RequestParam String userId) {
        try {
            UserProjection currentUser = userService.findUserById(userId);
            if (currentUser != null) {
                return ResponseEntity.ok(currentUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server"));
        }
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> payload) {
        try {
            boolean isCorrect = userService.isPasswordCorrect(payload.get("userId"), payload.get("password"));
            return ResponseEntity.ok(isCorrect);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server"));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtpToNewEmail(@RequestBody Map<String, String> payload) {
        try {
            if (!verifcationService.sendOTP(payload.get("id"), payload.get("email"), "Otp", LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Otp not sent"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, while sending Otp! Try again"));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtpAndChangeEmail(@RequestBody Map<String, String> payload) {
        try {
            UpdateResult updateResult = userService.verifyOtpAndChangeEmail(payload.get("id"), payload.get("email"),
                    payload.get("otp"));
            if (updateResult.getModifiedCount() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "OTP incorrect or expired, request new one"));
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    // @PostMapping("/change-password")
    // public ResponseEntity<?> changePassword(@RequestBody Map<String, String>
    // payload) {
    // String currentPassword =
    // bCryptPasswordEncoder.encode(payload.get("currentPassword"));
    // String newPassword =
    // bCryptPasswordEncoder.encode(payload.get("newPassword"));
    // System.out.println(currentPassword);
    // try {
    // UpdateResult updateResult = userService.changePassword(payload.get("id"),
    // currentPassword,
    // newPassword);
    // System.out.println(updateResult.getModifiedCount());
    // if (updateResult.getModifiedCount() <= 0) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    // .body(Map.of("error", "Password not correct"));
    // }
    // return ResponseEntity.ok(true);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(Map.of("error", "An unexpected error occured on server, while changing
    // password! Try again"));
    // }
    // }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
        String newPassword = bCryptPasswordEncoder.encode(payload.get("newPassword"));
        try {
            userService.changePassword(payload.get("id"), newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, while changing password! Try again"));
        }
    }

    @PostMapping("/update-general-details")
    public ResponseEntity<String> updateUserGeneralDetails(@RequestBody Map<String, Object> userDetails) {
        String id = (String) userDetails.remove("id");
        userService.updateUserGeneralDetails(id, userDetails);
        return ResponseEntity.ok("Details updated successfully");
    }

}

package com.buildazan.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.buildazan.enums.MemberShipLevel;
import com.buildazan.enums.SubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    
    // Identity and Basic Information
    private String firstName;
    private String lastName;
	@Indexed(unique = true)
    private String username; 
	@Indexed(unique = true)
    private String email;
	private boolean emailVerified;
    private String password;
    private LocalDateTime passwordLastChangedDate;
    private String verificationCode;
    private LocalDateTime expirationTime;

    // Contact and Location
    private String country;
    private String phoneNumber;

    // Financial and Transactional
    private List<String> paymentMethods;

    // Roles and Security
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean accountEnabled;
    private boolean caredentialsNonExpired;
    private UserRole userRole;
    private boolean twoFactorAuthEnabled;
    private LocalDateTime registrationTimestamp;

    // Preferences and Settings
    private String currency;
    private boolean termsAndConditionsAgreed;
    private boolean privacyPolicyAgreed;
    
    // Miscellaneous
    private String dateOfBirth;
    private String gender;
    private String profilePicture;
    
    //Subcription
    private SubscriptionStatus subscriptionStatus;
    private MemberShipLevel memberShipLevel;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;  
    private boolean trialUsed = false;
    
}

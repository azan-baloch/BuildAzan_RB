// package com.buildazan.controller;

// import java.time.LocalDateTime;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.buildazan.config.UserDetailsImpl;
// import com.buildazan.entities.User;
// import com.buildazan.helper.Message;
// import com.buildazan.service.UserService;
// import com.buildazan.service.VerifcationService;
// import com.mongodb.DuplicateKeyException;

// import jakarta.servlet.http.HttpSession;

// @Controller
// public class Controllers {

//     @Autowired
//     private UserService userService;

//     @Autowired
//     private VerifcationService verificationService;

//     @PostMapping("/check-email")
//     public String checkEmail(@RequestParam("email") String email, Model model, RedirectAttributes redirectAttributes,
//             HttpSession session) {
//         User user = userService.findUserByEmail(email);
//         if (user != null) {
//             if (verificationService.sendVerificationCode("Forgot Password", email, session)) {
//                 session.setAttribute("email", email);
//                 addFlashSuccessMessage(redirectAttributes, "A verification code has been sent to your email address.");
//                 return "redirect:verify-otp";
//             } else {
//                 addFlashFailedMessage(redirectAttributes, "Failed to send the verification email. Please try again.");
//                 return "redirect:forgot";
//             }
//         }
//         addFlashFailedMessage(redirectAttributes, "User not found. Please check your email address.");
//         return "redirect:forgot";
//     }

//     @PostMapping("/verify-otp")
//     public String verifyOTP(Authentication authentication,
//             @RequestParam(name = "formType", required = false) String formType, @RequestParam("email") String email,
//             @RequestParam("code") String code, HttpSession session, RedirectAttributes redirectAttributes) {
//         User user = userService.findUserByEmail(email);
//         if (user == null) {
//             addFlashFailedMessage(redirectAttributes, "User not found. Please check your email address.");
//             return "redirect:forgot";
//         }
//         if (verificationService.verifyCode((String) session.getAttribute("verificationCode"), code)) {
//             if (!verificationService.checkCodeExpiration((LocalDateTime) session.getAttribute("expirationTime"))) {
//                 addFlashFailedMessage(redirectAttributes,
//                         "The verification code has expired. Please request a new one.");
//                 if (formType != null && formType.equals("changeEmail")) {
//                     return "redirect:verify-email";
//                 }
//                 return "redirect:verify-otp";
//             }
//             user.setEmailVerified(true);
//             userService.saveUser(user);
//             if (formType != null && formType.equals("changeEmail")) {
//                 UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
//                 userDetailsImpl.setEmailVerified(true);
//                 Authentication authentication2 = new UsernamePasswordAuthenticationToken(userDetailsImpl, authentication.getCredentials(), userDetailsImpl.getAuthorities());
//                 SecurityContextHolder.getContext().setAuthentication(authentication2);
//                 return "redirect:/dashboard/";
//             }
//             addFlashSuccessMessage(redirectAttributes, "Verification successful. You can now create a new password.");
//             return "redirect:change-password";
//         }
//         addFlashFailedMessage(redirectAttributes, "The verification code is incorrect. Please try again.");
//         if (formType != null && formType.equals("changeEmail")) {
//             return "redirect:verify-email";
//         }
//         return "redirect:verify-otp";
//     }

//     @PostMapping("/change-password")
//     public String changePasswordPOST(@RequestParam("email") String email, @RequestParam("password") String password,
//             @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes,
//             HttpSession session) {
//         if (!password.equals(confirmPassword)) {
//             addFlashFailedMessage(redirectAttributes, "Passwords do not match. Please try again.");
//             return "redirect:change-password";
//         }
//         User user = userService.findUserByEmail(email);
//         if (user != null) {
//             user.setPassword(userService.generateSecurePassword(password));
//             userService.saveUser(user);
//             addFlashSuccessMessage(redirectAttributes, "Your password has been successfully changed.");
//             session.removeAttribute("email");
//             return "redirect:login";
//         }
//         addFlashFailedMessage(redirectAttributes, "Something went wrong. Please try again.");
//         return "redirect:change-password";
//     }

//     @PostMapping("/signup")
//     public String doSignUp(@ModelAttribute("user") User user, @RequestParam("confirmPassword") String confirmPassword,
//             @RequestParam(value = "terms", defaultValue = "false") boolean agreement, Model model,
//             RedirectAttributes redirectAttributes, HttpSession session) {
//         try {
//             if (!agreement) {
//                 addFlashFailedMessage(redirectAttributes, "Please agree to the Terms & Conditions.");
//                 return "redirect:signup";
//             }
//             if (!user.getPassword().equals(confirmPassword)) {
//                 addFlashFailedMessage(redirectAttributes, "Passwords do not match.");
//                 return "redirect:signup";
//             }
//             if (userService.existsByUsername(user.getUsername()) || userService.existsByEmail(user.getEmail())) {
//                 addFlashFailedMessage(redirectAttributes, "Username or email already exists. Please choose another.");
//                 return "redirect:signup";
//             }

//             if (!verificationService.sendVerificationCode("Verification code", user.getEmail(), session)) {
//                 addFlashFailedMessage(redirectAttributes, "Registration failed");
//                 return "redirect:signup";
//             }
//             userService.saveUser(user);
//             addFlashSuccessMessage(redirectAttributes,
//                     "Registration successfull! Verification code sent to your email");
//             session.setAttribute("email", user.getEmail());
//             return "redirect:verify-email";
//         } catch (DuplicateKeyException e) {
//             addFlashFailedMessage(redirectAttributes, "Username or email already exists. Please choose another.");
//             return "redirect:signup";
//         } catch (Exception e) {
//             addFlashFailedMessage(redirectAttributes, "Registration failed. Please try again.");
//             return "redirect:signup";
//         }
//     }

//     @PostMapping("/send-verfication-code")
//     @ResponseBody
//     public boolean sendVerificationCode(HttpSession session, Authentication authentication) {
//         String email = (String) session.getAttribute("email");
//         if (email != null) {
//             if (verificationService.sendVerificationCode("Verification code", email, session)) {
//                 return true;

//             } else {
//                 return false;
//             }
//         }

//         if (authentication!=null && authentication.isAuthenticated()) {
//         UserDetailsImpl userDetailsImpl = (UserDetailsImpl)
//         authentication.getPrincipal();
//         if (userDetailsImpl!=null) {
//         if (verificationService.sendVerificationCode("Verification code",userDetailsImpl.getEmail(), session)) {
//         return true;
//         }else{
//         return false;
//         }
//         }
//         }

//         return false;
//     }

//     private void addFlashSuccessMessage(RedirectAttributes attributes, String text) {
//         attributes.addFlashAttribute("msg", new Message(text, "alert-success"));
//     }

//     private void addFlashFailedMessage(RedirectAttributes attributes, String text) {
//         attributes.addFlashAttribute("msg", new Message(text, "alert-danger"));
//     }

// }

package com.buildazan.service;

import com.buildazan.entities.Order;
import com.buildazan.entities.OrderItem;

import brevo.ApiClient;
import brevo.Configuration;
import brevoApi.TransactionalEmailsApi;
import brevoModel.CreateSmtpEmail;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class EmailService {

    private final String fromEmail;          // Verified sender email in Brevo
    private final String brevoApiKey;          
    private final String templatePath = "emails/";

    public EmailService(@Value("${brevo.api.key}") String brevoApiKey,
                        @Value("${brevo.sender.email}") String fromEmail) {
        this.brevoApiKey = brevoApiKey;
        this.fromEmail = fromEmail;
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) throws IOException {
        System.out.println("Email link triggered");
        String htmlBody = loadTemplate("verification-email.html")
                .replace("{{verificationLink}}",
                        "http://revboost.shop:5173/verify-link?link=" + verificationCode + ":" + toEmail);
        sendEmail(toEmail, "Verify Your Account", htmlBody);
    }

    public void sendOtpEmail(String toEmail, String otp) throws IOException {
        System.out.println("Email OTP triggered");
        String htmlBody = loadTemplate("otp-email.html")
                .replace("{{otp}}", otp);
        sendEmail(toEmail, "Your One-Time Password (OTP)", htmlBody);
    }

    public void sendOrderConfirmation(String customerEmail,
                                      String platformEmail,
                                      Order order) throws IOException {
        // Customer email
        String customerHtml = loadTemplate("order-confirmation-customer.html")
                .replace("{{orderId}}", order.getId())
                .replace("{{customerName}}", order.getFullName())
                .replace("{{orderItems}}", formatOrderItems(order.getOrderItems()))
                .replace("{{totalAmount}}", String.format("%.2f", order.getTotalPrice()))
                .replace("{{paymentMethod}}", order.getPaymentMethod())
                .replace("{{shippingAddress}}", formatShippingAddress(order));
        sendEmail(customerEmail, "Order Confirmation #" + order.getId(), customerHtml);

        // Platform email
        String platformHtml = loadTemplate("order-notification-platform.html")
                .replace("{{orderId}}", order.getId())
                .replace("{{storeId}}", order.getStoreId())
                .replace("{{customerName}}", order.getFullName())
                .replace("{{customerEmail}}", order.getEmail())
                .replace("{{shippingAddress}}", formatShippingAddress(order))
                .replace("{{orderItems}}", formatOrderItems(order.getOrderItems()))
                .replace("{{totalPrice}}", String.format("%.2f", order.getTotalPrice()))
                .replace("{{paymentStatus}}", order.getPaymentStatus())
                .replace("{{shippingStatus}}", order.getShippingStatus());
        sendEmail(platformEmail, "New Order Received #" + order.getId(), platformHtml);
    }

    private String formatShippingAddress(Order order) {
        return String.format("%s, %s, %s %s, %s",
                order.getAddress(),
                order.getCity(),
                order.getPostalCode(),
                order.getCountry());
    }

    private String loadTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath + templateName);
        try (Scanner scanner = new Scanner(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    private String formatOrderItems(List<OrderItem> items) {
        StringBuilder sb = new StringBuilder();
        items.forEach(item -> sb.append("<li>")
            .append(item.getName()).append(" - ")
            .append(item.getQuantity()).append(" x $")
            .append(String.format("%.2f", item.getPrice()))
            .append("</li>"));
        return sb.toString();
    }

    /**
     * This method sends the email using Brevo's Transactional Email API.
     */
    private void sendEmail(String toEmail, String subject, String htmlBody) {
        try {
            TransactionalEmailsApi apiInstance = getTransactionalEmailsApi();

            SendSmtpEmail email = new SendSmtpEmail();

            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(fromEmail);
            email.sender(sender);

            List<SendSmtpEmailTo> toList = new ArrayList<>();
            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(toEmail);
            toList.add(recipient);
            email.setTo(toList);

            email.setSubject(subject);
            email.setHtmlContent(htmlBody);

            CreateSmtpEmail response = apiInstance.sendTransacEmail(email);
            log.info("Email sent to {} with subject: {}. Message ID: {}", toEmail, subject, response.getMessageId());
        } catch (Exception e) {
            log.error("Failed to send email to {} with subject: {}. Error: {}", toEmail, subject, e.getMessage());
        }
    }

    /**
     * Initializing and returning a TransactionalEmailsApi instance configured with Brevo API key.
     */
    private TransactionalEmailsApi getTransactionalEmailsApi() {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        apiClient.setApiKey(brevoApiKey);
        return new TransactionalEmailsApi(apiClient);
    }
}

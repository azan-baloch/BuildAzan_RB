package com.buildazan.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document (collection = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;
    @Indexed
    private String storeId;

    // Customer Information
    private String fullName;
    private String email;
    private String address;
    private String city;
    private String postalCode;
    private String country;

    // Payment Method (e.g., "cod" or "card")
    private String paymentMethod;

    private List<OrderItem> orderItems;
    private double shihppingFee;
    private LocalDate date;
    private String paymentStatus;
    private String shippingStatus;
    private double totalPrice;
}

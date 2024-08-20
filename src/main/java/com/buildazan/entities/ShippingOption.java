package com.buildazan.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.buildazan.enums.ShippingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "shipping-option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingOption {
    @Id
    private String id;
    private String name;
    private String description;
    private int minEstimatedDays;
    private int maxEstimatedDays;
    private String shippingRegion;
    private ShippingType shippingType;
    private String shippingMethod;
    private boolean enabled;

    @DBRef
    private List<Carrier> carriers;

}

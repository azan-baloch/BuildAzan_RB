package com.buildazan.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.buildazan.enums.ShippingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "shipping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingOption {
    @Id
    private String id;
    @Indexed
    private String storeId;
    private String name;
    private String description;
    private int minEstimatedDays;
    private int maxEstimatedDays;
    private String shippingRegion;
    private String shippingType;
    private boolean enabled;

}

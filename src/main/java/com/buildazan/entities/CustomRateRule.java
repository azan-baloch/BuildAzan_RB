package com.buildazan.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomRateRule{
    private double minWeight;
    private double maxWeight;
    private double minPrice;
    private double maxPrice;
    private double shippingFee;
        
}

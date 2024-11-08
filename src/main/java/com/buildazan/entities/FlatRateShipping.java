package com.buildazan.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shipping")
public class FlatRateShipping extends ShippingOption{
    private double flatRate;

    public double getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(double flatRate) {
        this.flatRate = flatRate;
    }
    
}

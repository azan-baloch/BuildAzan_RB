package com.buildazan.entities;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shipping")
public class CustomRateShipping extends ShippingOption{
    private List<CustomRateRule> customRateRules;

    public List<CustomRateRule> getCustomRateRules() {
        return customRateRules;
    }

    public void setCustomRateRules(List<CustomRateRule> customRateRules) {
        this.customRateRules = customRateRules;
    }
}


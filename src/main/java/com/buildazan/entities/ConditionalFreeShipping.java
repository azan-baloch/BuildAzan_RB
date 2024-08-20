package com.buildazan.entities;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shipping-option")
public class ConditionalFreeShipping extends ShippingOption{
    private List<PriceRange> priceRanges;
    public List<PriceRange> getPriceRanges(){
        return priceRanges;
    }
    public void setPriceRanges(List<PriceRange> priceRanges){
        this.priceRanges = priceRanges;
    }

}

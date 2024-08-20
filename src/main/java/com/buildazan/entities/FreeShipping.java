package com.buildazan.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shipping-option")
public class FreeShipping extends ShippingOption{
    
}

package com.buildazan.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shipping")
public class FreeShipping extends ShippingOption{
    
}

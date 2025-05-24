package com.buildazan.entities;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariation {
	private String id;
    private double price;
    private double discountPrice;
    private int stockQuantity;
	private String sku;
	private String image;
    // variationAttributes can store any custom attributes
    // For example: {"color": "red", "size": "large"}
    private Map<String, String> attributes;
}

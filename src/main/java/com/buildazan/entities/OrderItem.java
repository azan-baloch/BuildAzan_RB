package com.buildazan.entities;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productId;
    private int variationId;
    private String name;
    private double price;
    private double discountPrice;
    private int quantity;

    /**
     * Selected attributes are stored as a map.
     * For example: {"Color": "#685555", "Size": "6"}
     */
    private Map<String, String> selectedAttributes;

    private String productImage;

}

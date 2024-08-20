package com.buildazan.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Variation {
	private String id;
	private String name;
	private String value;
	private double price;
	private double discountPrice;
	private int stock;
	private List<String> image;
	private double weight;
	private String dimensions;
	private String sku;
	
}

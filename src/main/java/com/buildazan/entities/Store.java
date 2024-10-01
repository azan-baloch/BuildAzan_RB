package com.buildazan.entities;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document (collection = "store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
	@Id
	private String storeId;
	@Indexed(unique = true)
	private String domain; 
	private String description;
	private String logo;
	private String theme;
	private String storeEmail;
	private String storePhone;
	private String address;
	private String currency;
	private List<String> productIds;
	private List<String> categoryIds;
	private List<String> orderIds;
	private List<String> paymentMethods;
	private List<String> shippingOptions;
	private String privacyPolicy;
	private String termsOfService;
	private String status;
	private Map<String, String> socialLinks;

}

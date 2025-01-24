package com.buildazan.entities;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
	@Indexed
	private String userId;
	private String storeEmail;
	private String storePhone;
	private String address;
	private String currency;
	private Map<String, String> socialLinks;

	public Store(String userId, String domain){
		this.userId = userId;
		this.domain = domain;
	}

}

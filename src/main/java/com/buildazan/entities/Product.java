package com.buildazan.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
    private String id;
	@Indexed
    private String userId;
	@Indexed
    private String storeId;
    private String name;
    private String description;
    private double price;
    private double discountPrice;
	private String productImage;
	private List<String> galleryImages;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    @Indexed
    private List<String> categoryId;
    private List<String> tags;
	private boolean trackInventory;
	private int stockQuantity;
	private boolean stockStatus;
	private String sku;
    private double weight;
    private ProductDimensions productDimensions;
    private String manufacturer;
    private String brand;
    private Map<String, String> attributes;
    private String status;
    private String visibility;
    private boolean requiresShipping;
    private List<Variation> variations;
    //SEO
    private String seoTitle;
    private String metaDescription;
    private String slug;
    private List<String> keywords;
    private String canonicalURL;
    private Map<String, Object> schemaMarkup;
    //Shipping
    private ProductShipping productShipping;
	
}

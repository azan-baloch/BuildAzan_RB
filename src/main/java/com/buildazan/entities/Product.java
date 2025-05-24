package com.buildazan.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private double length;
    private double width;
    private double height;
    private String manufacturer;
    private String brand;
    private Map<String, String> attributes;
    private String status;
    private boolean requiresShipping;
    private List<ProductVariation> variations;
    private List<AttributeGroup> attributeGroups;
    //SEO
    private String seoTitle;
    private String metaDescription;
    @Indexed
    private String slug;
    private Map<String, Object> schemaMarkup;
    //Shipping
    private ProductShipping productShipping;

    public Product(String name, String slug, String description, int price, int discountPrice, String productImage, String storeId, ProductShipping productShipping, String status) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.productImage = productImage;
        this.storeId = storeId;
        this.productShipping = productShipping;
        this.status = status;
    }
	
}

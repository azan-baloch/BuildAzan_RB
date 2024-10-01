package com.buildazan.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	@Id
	private String id;
	@Indexed
	private String storeId;
	@Indexed
	private String parentCategoryId;
	private String name;
	private String description;
	private String image;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private String seoTitle;
    private String metaDescription;
    private String slug;

}

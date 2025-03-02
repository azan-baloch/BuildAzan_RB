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

@Document (collection = "pages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    @Id
    private String id;
    private String name;
    @Indexed
    private String storeId;
    @Indexed
    private String slug;
    @Indexed
    private String storeDomain;
    private List<Map<String, Object>> globalContent;
    private List<Map<String, Object>> content;
    private boolean isDefault;
    private String metaTitle;
    private String metaDescription;
}

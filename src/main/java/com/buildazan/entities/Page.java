package com.buildazan.entities;

import java.util.List;
import java.util.Map;

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
    private String id;
    private String name;
    @Indexed
    private String storeId;
    @Indexed(unique = true)
    private String slug;
    private List<Map<String, Object>> content;
    private String metaTitle;
    private String metaDescription;
}

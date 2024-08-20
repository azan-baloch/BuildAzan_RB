package com.buildazan.service;

import java.util.List;

import com.buildazan.entities.Category;

public interface CategoryService {
    public Category saveCategory(Category category);
    public List<Category> getCategories();
    public void deleteCategoryById(String id);
}

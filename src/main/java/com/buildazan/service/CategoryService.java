package com.buildazan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Category;
import com.buildazan.repo.CategoryRepo;

@Service
public class CategoryService{
    @Autowired
    private CategoryRepo categoryRepo;

    
    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    
    public List<Category> getCategories() {
        return categoryRepo.findAll();
    }

    
    public void deleteCategoryById(String id) {
        categoryRepo.deleteById(id);
    }
    
}

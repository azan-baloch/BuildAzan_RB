package com.buildazan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Category;
import com.buildazan.projection.SlugProjection;
import com.buildazan.repo.CategoryRepo;

@Service
public class CategoryService{
    @Autowired
    private CategoryRepo categoryRepo;

    
    public void saveCategory(Category category) {
        categoryRepo.save(category);
    }

    
    public List<Category> fetchCategories() {
        return categoryRepo.findAll();
    }
    
    public void deleteCategoryById(String id) {
        categoryRepo.deleteById(id);
    }

    public List<SlugProjection> findSlugByStoreId(String storeId){
        return categoryRepo.findSlugsByStoreId(storeId);
    }
    
}

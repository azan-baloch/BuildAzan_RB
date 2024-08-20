package com.buildazan.restcontrollers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.buildazan.config.UserDetailsImpl;
import com.buildazan.entities.Category;
import com.buildazan.service.CategoryService;
import com.buildazan.service.ImageService;

@RestController
@RequestMapping("/categories")
public class CategorController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;
    
    @PostMapping("/add-category")   
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<Category> addCategory(@ModelAttribute("category") Category category, @RequestParam(name = "categoryImage", required = false) MultipartFile categoryImage, Authentication authentication){
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        category.setUserId(user.getUserId());
        category.setCreatedDate(LocalDateTime.now());
        category.setImage(imageService.saveImage("uploads/img/categories/", categoryImage));
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }
    
    @Cacheable("categories")
    @GetMapping("/get-categories")
    public ResponseEntity<List<Category>> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @DeleteMapping("/delete/{id}")
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(@PathVariable("id") String id){
        categoryService.deleteCategoryById(id);
    }

}

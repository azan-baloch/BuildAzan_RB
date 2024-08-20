package com.buildazan.restcontrollers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.buildazan.config.UserDetailsImpl;
import com.buildazan.entities.BulkProductActionRequest;
import com.buildazan.entities.Product;
import com.buildazan.entities.ProductDimensions;
import com.buildazan.entities.ProductShipping;
import com.buildazan.service.ImageService;
import com.buildazan.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/demo-products")
    public List<Product> getDemoProducts(){
        System.out.println("request recieved");
        return productService.getDemoProducts();
    }
    
    @GetMapping("/all-products")
    // @Cacheable("products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "category", required = false) String categoryId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "stock", required = false) Boolean stockStatus,
            @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getFilteredProducts(pageable, categoryId, status, stockStatus, search);
        return ResponseEntity.ok(products);
    }

    @InitBinder
    protected void InitBinder(WebDataBinder webDataBinder){
        webDataBinder.setDisallowedFields("galleryImages", "productImage");
    }

    @PostMapping("/add-product")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<Boolean> addProduct(
        @ModelAttribute(name = "product") Product formProduct,
        @ModelAttribute(name = "productDimensions") ProductDimensions productDimensions,
        Authentication authentication,
        @RequestParam("productImage") MultipartFile productImage,   
        @RequestParam("galleryImages") List<MultipartFile> galleryImages,
        @ModelAttribute("productShipping") ProductShipping productShipping) {
        
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        Product product = new Product();
        product.setUserId(user.getUserId());
        product.setName(formProduct.getName());
        product.setDescription(formProduct.getDescription());
        product.setPrice(formProduct.getPrice());
        product.setDiscountPrice(formProduct.getDiscountPrice());

        String uploadPath = "uploads/img/products/";
        if (!productImage.isEmpty()) {
            product.setProductImage(imageService.saveImage(uploadPath, productImage));
        }else{
            product.setProductImage("defaultProductImage.png");
        }

        if (!galleryImages.isEmpty()) {
            List<String> fileNames = new ArrayList<>();
            for (MultipartFile multipartFile : galleryImages) {
                String fileName = imageService.saveImage(uploadPath, multipartFile);
                if (fileName != null) {
                    fileNames.add(fileName);
                }
            }
            product.setGalleryImages(fileNames);
        } else {
            product.setGalleryImages(Collections.emptyList()); // or handle the case as needed
        }
        product.setCreatedDate(LocalDateTime.now());
        product.setUpdatedDate(LocalDateTime.now());
        product.setCategoryId(formProduct.getCategoryId());
        product.setTags(formProduct.getTags());
        product.setTrackInventory(formProduct.isTrackInventory());
        product.setStockQuantity(formProduct.getStockQuantity());
        product.setStockStatus(formProduct.isStockStatus());
        product.setSku(formProduct.getSku());
        product.setWeight(formProduct.getWeight());
        product.setProductDimensions(formProduct.getProductDimensions());
        product.setManufacturer(formProduct.getManufacturer());
        product.setBrand(formProduct.getBrand());
        product.setAttributes(formProduct.getAttributes());
        product.setStatus(formProduct.getStatus());
        product.setProductDimensions(productDimensions);
        
        product.setProductShipping(productShipping);

        productService.saveProduct(product);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/edit-product")
    public ResponseEntity<Boolean> editProduct(
        @ModelAttribute(name = "product") Product formProduct,
        @ModelAttribute(name = "productDimensions") ProductDimensions productDimensions,
        Authentication authentication,
        @RequestParam("productImage") MultipartFile productImage,   
        @RequestParam("galleryImages") List<MultipartFile> galleryImages,
        @RequestParam(value = "existingGalleryImages", required = false) List<String> existingGalleryImages,
        @ModelAttribute("productShipping") ProductShipping productShipping) {
        
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        
        Optional<Product> optionalProduct = productService.getProductById(formProduct.getId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        
        Product product = optionalProduct.get();
        
        // Update the fields with new values
        product.setName(formProduct.getName());
        product.setDescription(formProduct.getDescription());
        product.setPrice(formProduct.getPrice());
        product.setDiscountPrice(formProduct.getDiscountPrice());

        String uploadPath = "uploads/img/products/";
        if (!productImage.isEmpty()) {
            product.setProductImage(imageService.saveImage(uploadPath, productImage));
        }

        List<String> galleryImageNames = new ArrayList<>();
    if (existingGalleryImages != null && !existingGalleryImages.isEmpty()) {
        galleryImageNames.addAll(existingGalleryImages);
    }

    if (galleryImages != null && !galleryImages.isEmpty()) {
        for (MultipartFile multipartFile : galleryImages) {
            String fileName = imageService.saveImage(uploadPath, multipartFile);
            if (fileName != null) {
                galleryImageNames.add(fileName);
            }
        }
    }
    product.setGalleryImages(galleryImageNames);
        
        product.setUpdatedDate(LocalDateTime.now());
        product.setCategoryId(formProduct.getCategoryId());
        product.setTags(formProduct.getTags());
        product.setTrackInventory(formProduct.isTrackInventory());
        product.setStockQuantity(formProduct.getStockQuantity());
        product.setStockStatus(formProduct.isStockStatus());
        product.setSku(formProduct.getSku());
        product.setWeight(formProduct.getWeight());
        product.setProductDimensions(productDimensions);
        product.setManufacturer(formProduct.getManufacturer());
        product.setBrand(formProduct.getBrand());
        product.setAttributes(formProduct.getAttributes());
        product.setStatus(formProduct.getStatus());
        product.setProductShipping(productShipping);

        productService.saveProduct(product);
        return ResponseEntity.ok(true);
    }


    @DeleteMapping("/delete/{id}")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/bulk-enable")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> bulkEnableProducts(@RequestBody BulkProductActionRequest request) {
        productService.bulkEnableProducts(request.getProductIds());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bulk-disable")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> bulkDisableProducts(@RequestBody BulkProductActionRequest request) {
        productService.bulkDisableProducts(request.getProductIds());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bulk-delete")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> bulkDeleteProducts(@RequestBody BulkProductActionRequest request) {
        productService.bulkDeleteProducts(request.getProductIds());
        return ResponseEntity.ok().build();
    }

}

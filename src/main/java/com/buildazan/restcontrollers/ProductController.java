package com.buildazan.restcontrollers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.buildazan.config.UserDetailsImpl;
import com.buildazan.entities.BulkProductActionRequest;
import com.buildazan.entities.Product;
import com.buildazan.entities.ProductDimensions;
import com.buildazan.entities.ProductShipping;
import com.buildazan.projection.ProductProjection;
import com.buildazan.projection.SlugProjection;
import com.buildazan.repo.StoreRepo;
import com.buildazan.service.ImageService;
import com.buildazan.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private ImageService imageService;

    @GetMapping("/fetch-products")
    public ResponseEntity<?> fetchProducts(
            @RequestParam("storeId") String storeId) {
        try {
            List<ProductProjection> products = productService.getProductsByStoreId(storeId);
            for (ProductProjection productProjection : products) {
                productProjection.getStockStatus();
            }
            return ResponseEntity.ok(products);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/fetch-product-by-domain")
    public ResponseEntity<?> fetchProductByDomain(@RequestParam("domain") String domain,
            @RequestParam("productSlug") String productSlug) {
        try {
            AggregationResults<Map<String, Object>> product = storeRepo.findStoreIdWithProduct(domain, productSlug);
            if (product.getMappedResults().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
            }
            return ResponseEntity.ok(product.getMappedResults().get(0).get("products"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/fetch-product-by-store")
    public ResponseEntity<?> fetchProductByStoreId(@RequestParam("storeId") String storeId,
            @RequestParam("productSlug") String productSlug) {
        try {
            Product product = productService.findProductByStoreIdAndSlug(storeId, productSlug);
            if (product != null) {
                return ResponseEntity.ok(product);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/filtered-products")
    // @Cacheable("products")
    public ResponseEntity<?> getFilteredProducts(
            @RequestParam("storeId") String storeId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "category", required = false) String categoryId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "stock", required = false) Boolean stockStatus,
            @RequestParam(value = "search", required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productService.getFilteredProducts(storeId, categoryId, status, stockStatus,
                    search,
                    pageable);
            return ResponseEntity.ok(products);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PostMapping("/add-product")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> addProduct(@RequestBody Map<String, String> productData) {
        try {
            Product product = productService.productInitializer(productData);
            productService.saveProduct(product);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    
    @DeleteMapping("/delete/{id}")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/bulk-enable")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> bulkEnableProducts(@RequestBody BulkProductActionRequest request) {
        try {
            productService.bulkEnableProducts(request.getProductIds());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PutMapping("/bulk-disable")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> bulkDisableProducts(@RequestBody BulkProductActionRequest request) {
        try {
            productService.bulkDisableProducts(request.getProductIds());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @DeleteMapping("/bulk-delete")
    // @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> bulkDeleteProducts(@RequestBody BulkProductActionRequest request) {
        try {
            productService.bulkDeleteProducts(request.getProductIds());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

}

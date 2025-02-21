package com.buildazan.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.buildazan.entities.Product;
import com.buildazan.entities.ProductShipping;
import com.buildazan.entities.ProductVariation;
import com.buildazan.entities.ShippingOption;
import com.buildazan.projection.ProductProjection;
import com.buildazan.projection.SlugProjection;
import com.buildazan.repo.ProductRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Product productInitializer(Map<String, String> data) {
        System.out.println(data);
        Product product = new Product();
        if (data.get("id") != null && !data.get("id").isEmpty()) {
            product.setId(data.get("id"));
        }
        product.setStoreId(data.get("storeId"));
        product.setSlug(data.get("slug"));
        product.setName(data.get("name"));
        product.setDescription(data.get("description"));
        product.setPrice(Double.parseDouble(data.get("price")));
        if (data.get("discountPrice") != null && !data.get("discountPrice").isEmpty()) {
            product.setDiscountPrice(Double.parseDouble(data.get("discountPrice")));
        }
        product.setProductImage(data.get("productImage"));
        String galleryImages = data.get("galleryImages");
        if (galleryImages != null && !galleryImages.isEmpty()) {
            product.setGalleryImages(Arrays.asList(galleryImages.split(",")));
        } else {
            product.setGalleryImages(Collections.emptyList());
        }
        product.setCreatedDate(LocalDateTime.now());
        if (data.get("categoryId") != null && !data.get("categoryId").isEmpty()) {
            product.setCategoryId(Arrays.asList(data.get("categoryId").split(",")));
        }
        if (data.get("tags") != null && !data.get("tags").isEmpty()) {
            product.setTags(Arrays.asList(data.get("tags").split(",")));
        }
        product.setTrackInventory(Boolean.parseBoolean(data.get("trackInventory")));
        if (data.get("stockQuantity") != null && !data.get("stockQuantity").isEmpty()) {
            product.setStockQuantity(Integer.parseInt(data.get("stockQuantity")));
        }
        product.setStockStatus(Boolean.parseBoolean(data.get("stockStatus")));
        if (data.get("trackInventory") == null && data.get("stockStatus") == null) {
            product.setStockStatus(true);
        }
        if (data.get("sku") != null && !data.get("sku").isEmpty()) {
            product.setSku(data.get("sku"));
        }
        product.setStatus(data.get("status"));
        if (data.get("weight") != null && !data.get("weight").isEmpty()) {
            product.setWeight(Double.parseDouble(data.get("weight")));
        }
        if (data.get("length") != null && !data.get("length").isEmpty()) {
            product.setLength(Double.parseDouble(data.get("length")));
        }
        if (data.get("width") != null && !data.get("width").isEmpty()) {
            product.setWidth(Double.parseDouble(data.get("width")));
        }
        if (data.get("height") != null && !data.get("height").isEmpty()) {
            product.setHeight(Double.parseDouble(data.get("height")));
        }
        if (data.get("manufacturer") != null && !data.get("manufacturer").isEmpty()) {
            product.setManufacturer(data.get("manufacturer"));
        }
        if (data.get("brand") != null && !data.get("brand").isEmpty()) {
            product.setBrand(data.get("brand"));
        }
        product.setSeoTitle(data.get("seoTitle"));
        product.setMetaDescription(data.get("metaDescription"));

        ProductShipping productShipping = new ProductShipping();
        String shippingMethod = data.get("shippingMethod");
        switch (shippingMethod) {
            case "free-shipping":
                productShipping.setShippingMethod(shippingMethod);
                product.setProductShipping(productShipping);
                break;
            case "flat-rate-shipping":
                productShipping.setShippingMethod(shippingMethod);
                productShipping.setFlatRate(Double.parseDouble(data.get("flatRate")));
                product.setProductShipping(productShipping);
                break;
            case "store-shipping":
                productShipping.setShippingMethod(shippingMethod);
                product.setProductShipping(productShipping);
            default:
                break;
        }

        String variationsJson = data.get("variations");
    if (variationsJson != null && !variationsJson.isEmpty()) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ProductVariation> variationsList = mapper.readValue(
                    variationsJson, new TypeReference<List<ProductVariation>>() {});
            product.setVariations(variationsList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        return product;
    }

    public boolean saveProduct(Product product) {
        Product save = productRepo.save(product);
        if (save != null) {
            return true;
        }
        return false;
    }

    public Optional<Product> findProductById(String productId){
        return productRepo.findById(productId);
    }

    public Product findProductByStoreIdAndSlug(String storeId, String slug){
        return productRepo.findByStoreIdAndSlug(storeId, slug);
    }

    public List<ProductProjection> getProductsByStoreId(String storeId) {
        return productRepo.findAllProjectionByStoreId(storeId);
    }

    public Page<ProductProjection> getPagedProductsByStoreId(String storeId, Pageable pageable) {
        return productRepo.findAllProjectionByStoreId(storeId, pageable);
    }

    public Page<Product> getFilteredProducts(String storeId, String categoryId, String status, Boolean stockStatus,
            String search, Pageable pageable) {
        return productRepo.getFilteredProducts(storeId, categoryId, status, stockStatus, search, pageable);
    }

    public List<SlugProjection> findSlugByStoreId(String storeId){
        return productRepo.findSlugsByStoreId(storeId);
    }

    public void deleteProductById(String id) {
        productRepo.deleteById(id);
    }

    // bulk actions
    public void bulkEnableProducts(List<String> productIds) {
        List<Product> products = productRepo.findAllById(productIds);
        for (Product product : products) {
            product.setStatus("enabled");
        }
        productRepo.saveAll(products);
    }

    public void bulkDisableProducts(List<String> productIds) {
        List<Product> products = productRepo.findAllById(productIds);
        for (Product product : products) {
            product.setStatus("disabled");
        }
        productRepo.saveAll(products);
    }

    public void bulkDeleteProducts(List<String> productIds) {
        productRepo.deleteAllById(productIds);
    }

}

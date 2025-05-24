package com.buildazan.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.buildazan.entities.AttributeGroup;
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

        String attributeGroupsJson = data.get("attributeGroups");
        if (attributeGroupsJson != null && !attributeGroupsJson.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<AttributeGroup> attributeGroupList = mapper.readValue(
                        attributeGroupsJson, new TypeReference<List<AttributeGroup>>() {});
                product.setAttributeGroups(attributeGroupList);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return product;
    }

public void createDefaultProducts(String storeId) {
    ProductShipping defaultShipping = new ProductShipping("free-shipping", null);
    String defaultStatus = "enabled";

    List<Product> demoProducts = List.of(
        new Product(
            "Sport Runner X1 - Demo Product",
            "demo-sport-runner-x1",
            "High-performance running shoes for daily training and racing.",
            450,
            350,
            "https://www.soloto.com/cdn/shop/products/1_ca14a7c2-de8c-4ca5-9aac-4b09130804a3.jpg?v=1662960848",
            storeId,
            defaultShipping,
            defaultStatus
        ),
        new Product(
            "Urban Street 90 - Demo Product",
            "demo-urban-street-90",
            "Stylish streetwear sneakers for casual comfort and flair.",
            500,
            400,
            "https://www.walkaroo.in/cdn/shop/files/3_e6e303ab-9323-405f-adc3-adde5d91761b.jpg?v=1740585734&width=500",
            storeId,
            defaultShipping,
            defaultStatus
        ),
        new Product(
            "Trail Beast Pro - Demo Product",
            "demo-trail-beast-pro",
            "Durable trail shoes designed for off-road adventures.",
            600,
            480,
            "https://trex.com.pk/uploads/trex/OHfZWNpOib7U2anEJZuhI7q8knVb94GoGJILHsQq.jpg",
            storeId,
            defaultShipping,
            defaultStatus
        ),
        new Product(
            "Classic Leather Edge - Demo Product",
            "demo-classic-leather-edge",
            "Premium leather shoes for timeless elegance and comfort.",
            700,
            590,
            "https://www.soloto.com/cdn/shop/products/1_f5cb92bd-5b6d-4dd2-8670-9aa90803cb9e.jpg?v=1664948564",
            storeId,
            defaultShipping,
            defaultStatus
        ),
        new Product(
            "Flex Fit Trainer - Demo Product",
            "demo-flex-fit-trainer",
            "Versatile training shoes ideal for gym, sports, and everyday wear.",
            420,
            320,
            "https://blackcamel.pk/cdn/shop/files/Untitled-1_5000x_89167fd3-b942-4f63-a6f2-b6fcf01ae083_540x.jpg?v=1691522165",
            storeId,
            defaultShipping,
            defaultStatus
        ),
        new Product(
            "Aero Glide Max - Demo Product",
            "demo-aero-glide-max",
            "Lightweight performance sneakers engineered for speed and breathability.",
            550,
            430,
            "https://example.com/images/aero-glide-max.jpg",
            storeId,
            defaultShipping,
            defaultStatus
        )
    );

    productRepo.saveAll(demoProducts);
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

    /**
     * Fetch products by collection type.
     * 
     * @param storeId           the store id
     * @param productCollection either "latest", "old" or a categoryId
     * @param numberOfProductsStr number of products to fetch as String
     * @return a Page of ProductProjection objects
     */
    public Page<ProductProjection> fetchProductsByCollection(String storeId, String productCollection, String numberOfProductsStr) {
        int numberOfProducts = Integer.parseInt(numberOfProductsStr);
        Pageable pageable;
        Page<ProductProjection> products;

        if ("latest".equalsIgnoreCase(productCollection)) {
            // For latest products, sort descending by createdDate
            pageable = PageRequest.of(0, numberOfProducts, Sort.by("createdDate").descending());
            products = productRepo.findAllProjectionByStoreId(storeId, pageable);
        } else if ("old".equalsIgnoreCase(productCollection)) {
            // For old products, sort ascending by createdDate
            pageable = PageRequest.of(0, numberOfProducts, Sort.by("createdDate").ascending());
            products = productRepo.findAllProjectionByStoreId(storeId, pageable);
        } else {
            // Otherwise, treat the productCollection as a categoryId
            pageable = PageRequest.of(0, numberOfProducts, Sort.by("createdDate").descending());
            products = productRepo.findByStoreIdAndCategoryId(storeId, productCollection, pageable);
        }

        return products;
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

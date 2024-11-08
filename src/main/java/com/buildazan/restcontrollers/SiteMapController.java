package com.buildazan.restcontrollers;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.units.qual.g;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.projection.SlugProjection;
import com.buildazan.service.CategoryService;
import com.buildazan.service.ProductService;
import com.buildazan.service.StoreService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class SiteMapController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public ResponseEntity<?> GenerateSiteMap(HttpServletRequest request) {
        // String domain = request.getHeader("host");
        String domain = "awaisstore.buildazan.com";
        AggregationResults<Map<String, Object>> allSlugsForStore = storeService.findAllSlugsForStore(domain);
        Map<String, Object> slugsData = allSlugsForStore.getUniqueMappedResult();
        List<Map<String, String>> productSlugs = extractSlugs(slugsData, "products");
        List<Map<String, String>> categorySlugs = extractSlugs(slugsData, "categories");
        String sitemap = generateDynamicSitemap(domain, productSlugs, categorySlugs);
        return ResponseEntity.ok(sitemap);
    }

    private List<Map<String, String>> extractSlugs(Map<String, Object> slugsData, String key) {
        Object slugsObject = slugsData.get(key);

        if (slugsObject instanceof List) {
            return (List<Map<String, String>>) slugsObject;
        }

        // If it's not a list, return an empty list
        return new ArrayList<>();
    }

    public String generateDynamicSitemap(String domain, List<Map<String, String>> productSlugs,
            List<Map<String, String>> categorySlugs) {
        StringBuilder sitemap = new StringBuilder();

        sitemap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Add product slugs to the sitemap
        if (productSlugs != null) {
            for (Map<String, String> product : productSlugs) {
                String productSlug = product.get("slug");
                if (productSlug != null) {
                    sitemap.append("<url><loc>http://").append(domain).append("/product/").append(productSlug)
                            .append("</loc></url>\n");
                }
            }
        }

        // Add category slugs to the sitemap
        if (categorySlugs != null) {
            for (Map<String, String> category : categorySlugs) {
                String categorySlug = category.get("slug");
                if (categorySlug != null) {
                    sitemap.append("<url><loc>http://").append(domain).append("/category/").append(categorySlug)
                            .append("</loc></url>\n");
                }
            }
        }

        sitemap.append("</urlset>");
        return sitemap.toString();
    }

}

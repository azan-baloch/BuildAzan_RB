package com.buildazan.restcontrollers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.entities.Page;
import com.buildazan.service.PageService;

@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired
    private PageService pageService;

    @PostMapping("/add-page")
    public ResponseEntity<?> savePage(@RequestBody Page page){
        try {
            pageService.savePage(page);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/get-page-by-id")
    public ResponseEntity<?> getPageById(@RequestParam String pageId){
        try {
            Optional<Page> page = pageService.getPageById(pageId);
            if (page.isPresent()) {
                return ResponseEntity.ok(page.get());
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Page not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        } 
    }
    @GetMapping("/get-page-by-domain")
    public ResponseEntity<?> getPageByDomain(@RequestParam String domain, @RequestParam String slug){
        try {   
            Optional<Page> page = pageService.getPageByDomainAndSlug(domain, slug);
            if (page.isPresent()) {
                return ResponseEntity.ok(page.get());
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Page not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        } 
    }
    @GetMapping("/get-pages-by-store-id")
    public ResponseEntity<?> getPageByStoreId(@RequestParam String storeId){
        try {
            return ResponseEntity.ok(pageService.getPagesByStoreId(storeId));  
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }  
    }

    @GetMapping("/get-page-global-content")
    public ResponseEntity<?> getPageGlobalContent(@RequestParam String storeDomain){
        try {
            return ResponseEntity.ok(pageService.getGlobalContent(storeDomain)); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }   
    }

    @PutMapping("/update-page")
    public ResponseEntity<?> updatePage(@RequestBody Page page){
        try {
            pageService.upatePage(page);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PutMapping("/update-page-content")
    public ResponseEntity<?> updatePageContent(@RequestBody Map<String, Object> data){
        try {
            System.out.println(data);
            // System.out.println(data.get("content"));
            pageService.updatePageContent((String) data.get("id"), data.get("content"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }

    }
    @PutMapping("/update-global-content")
    public ResponseEntity<?> updatePageGlobalContent(@RequestBody Map<String, Object> data){
        try {
            System.out.println(data);
            // System.out.println(data.get("content"));
            pageService.updatePageContent((String) data.get("id"), data.get("content"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }

    }

    @PutMapping("/update-all-content")
    public ResponseEntity<?> updatePageAllContent(@RequestBody Map<String, Object> data){
        try {
            System.out.println(data);
            pageService.updatePageAllContent((String) data.get("id"), data);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred on server, try again"));
        }
    }


    @DeleteMapping("/delete/{pageId}")
    public ResponseEntity<?> deletePage(@PathVariable String pageId){
        try {
            pageService.deleteById(pageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PutMapping("/update-seo")
    public ResponseEntity<?> updateSeo(@RequestBody Map<String, String> data){
        try {
            pageService.updateSeo(data);
            return ResponseEntity.ok("Seo settings updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred on server, try again"));
        }
    }

    @PutMapping("/update-header")
    public ResponseEntity<?> updatePageHeader(@RequestBody Map<String, Object> data){
        try {
            pageService.updatePageAllContent((String) data.get("id"), data);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred on server, try again"));
        }
    }

}

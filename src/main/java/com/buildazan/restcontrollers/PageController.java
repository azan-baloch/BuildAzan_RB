package com.buildazan.restcontrollers;

import java.util.Map;

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
            System.out.println("request recieved");
            pageService.savePage(page);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/get-page")
    public ResponseEntity<?> getPage(@RequestParam String storeId){
        // String data = pageService.getPage(slug).getContent();
        // return ResponseEntity.ok(data);
        return ResponseEntity.ok(pageService.getPageByStoreId(storeId));   
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

}

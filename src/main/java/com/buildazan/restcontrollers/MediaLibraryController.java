package com.buildazan.restcontrollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buildazan.entities.MediaLibrary;
import com.buildazan.service.MediaLibraryService;

@RestController
@RequestMapping("/media-library")
public class MediaLibraryController {

    @Autowired
    private MediaLibraryService mediaLibraryService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody MediaLibrary mediaLibrary){
        try {
            mediaLibraryService.saveMediaLibrary(mediaLibrary);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody MediaLibrary mediaLibrary){
        try {
            mediaLibraryService.saveMediaLibrary(mediaLibrary);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam String storeId){
        try {
            return ResponseEntity.ok(mediaLibraryService.fetchMediaLibraryByStoreId(storeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occured on server, try again"));
        }
    }

    
}

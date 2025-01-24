package com.buildazan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildazan.entities.MediaLibrary;
import com.buildazan.repo.MediaLibraryRepo;

@Service
public class MediaLibraryService {
    @Autowired
    private MediaLibraryRepo mediaLibraryRepo;

    public void saveMediaLibrary(MediaLibrary mediaLibrary){
        mediaLibraryRepo.save(mediaLibrary);
    }

    public MediaLibrary fetchMediaLibraryByStoreId(String storeId){
        return mediaLibraryRepo.findByStoreId(storeId);
    }
}

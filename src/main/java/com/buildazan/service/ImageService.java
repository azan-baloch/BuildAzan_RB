package com.buildazan.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    public String saveImage(String directory, MultipartFile file);
}

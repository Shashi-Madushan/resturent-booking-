package org.example.resturent.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.example.resturent.exeptions.custom.StorageException;
import org.example.resturent.service.ImageStorageService;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "CLOUDINARY")
public class CloudinaryImageStorageService implements ImageStorageService {

    private final Cloudinary cloudinary;

    public CloudinaryImageStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(byte[] fileBytes, String fileName) throws StorageException {
        try {
            // write bytes to temp file
            File tempFile = File.createTempFile("upload-", fileName);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileBytes);
            }
            Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap("public_id", fileName));
            String url = uploadResult.get("secure_url").toString();
            // optionally delete tempFile
            tempFile.delete();
            return url;
        } catch (IOException e) {
            throw new StorageException("Failed to write temp file for Cloudinary upload", e);
        } catch (Exception e) {
            throw new StorageException("Failed to upload to Cloudinary", e);
        }
    }

    @Override
    public String upload(MultipartFile file, String fileName) throws StorageException {
        try {
            return upload(file.getBytes(), fileName);
        } catch (IOException e) {
            throw new StorageException("Failed to read multipart file for Cloudinary upload", e);
        }
    }
}

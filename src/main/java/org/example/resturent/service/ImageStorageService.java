package org.example.resturent.service;

import org.example.resturent.exeptions.custom.StorageException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    /**
     * Uploads file bytes (from multipart) and returns the accessible URL or identifier.
     * @param fileBytes the file content
     * @param fileName the target filename (including extension)
     * @return URL or identifier for the uploaded file
     * @throws StorageException in case of failure
     */
    String upload(byte[] fileBytes, String fileName) throws StorageException;

    /**
     * Uploads file directly from MultipartFile (stream-based, more efficient)
     * @param file the multipart file
     * @param fileName the target filename
     * @return URL for the uploaded file
     * @throws StorageException in case of failure
     */
    String upload(MultipartFile file, String fileName) throws StorageException;
}

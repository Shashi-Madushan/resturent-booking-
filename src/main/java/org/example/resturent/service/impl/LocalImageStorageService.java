package org.example.resturent.service.impl;

import org.example.resturent.exeptions.custom.StorageException;
import org.example.resturent.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "LOCAL")
public class LocalImageStorageService implements ImageStorageService {

    private final Path storagePath;
    private final String publicPath;

    public LocalImageStorageService(@Value("${storage.local.directory:${local.directory:uploads}}") String storageDirectory,
                                    @Value("${storage.local.public-path:${local.public-path:uploads}}") String publicPath) {
        this.storagePath = Paths.get(storageDirectory).toAbsolutePath().normalize();
        this.publicPath = normalizePublicPath(publicPath);
        initStorageDirectory();
    }

    @Override
    public String upload(byte[] fileBytes, String fileName) throws StorageException {
        try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            return storeFile(inputStream, fileName);
        } catch (IOException e) {
            throw new StorageException("Failed to read file bytes for local upload", e);
        }
    }

    @Override
    public String upload(MultipartFile file, String fileName) throws StorageException {
        try (InputStream inputStream = file.getInputStream()) {
            return storeFile(inputStream, fileName);
        } catch (IOException e) {
            throw new StorageException("Failed to read multipart file for local upload", e);
        }
    }

    private String storeFile(InputStream inputStream, String originalFileName) throws StorageException {
        String sanitizedFileName = sanitizeFileName(originalFileName);
        Path targetLocation = storagePath.resolve(sanitizedFileName);

        try {
            Files.createDirectories(targetLocation.getParent());
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return buildFileAccessPath(sanitizedFileName);
        } catch (IOException e) {
            throw new StorageException("Failed to store file locally", e);
        }
    }

    private void initStorageDirectory() throws StorageException {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new StorageException("Failed to initialize local storage directory", e);
        }
    }

    private String sanitizeFileName(String fileName) throws StorageException {
        if (fileName == null) {
            throw new StorageException("File name cannot be null", new IllegalArgumentException("Null file name"));
        }
        try {
            String sanitized = Paths.get(fileName).getFileName().toString();
            if (sanitized.isBlank()) {
                throw new StorageException("Resolved file name is empty", new IllegalArgumentException("Blank file name"));
            }
            return sanitized;
        } catch (InvalidPathException ex) {
            throw new StorageException("Invalid file name provided", ex);
        }
    }

    private String buildFileAccessPath(String fileName) {
        if (publicPath.isBlank()) {
            return fileName;
        }
        return publicPath + (publicPath.endsWith("/") ? "" : "/") + fileName;
    }

    private String normalizePublicPath(String path) {
        if (path == null) {
            return "";
        }
        String normalized = path.trim().replace("\\", "/");
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}

package org.example.resturent.service.impl;

import io.minio.*;
import org.example.resturent.exeptions.custom.StorageException;
import org.example.resturent.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "MINIO")
public class MinioImageStorageService implements ImageStorageService {

    private final MinioClient minioClient;
    private final String bucket;
    private final String endpoint;

    public MinioImageStorageService(MinioClient minioClient,
                                    @Value("${minio.bucket}") String bucket,
                                    @Value("${minio.endpoint}") String endpoint) {
        this.minioClient = minioClient;
        this.bucket = bucket;
        this.endpoint = endpoint;
    }

    // ✅ Option 1: Upload directly from MultipartFile (recommended)
    @Override
    public String upload(MultipartFile file, String fileName) throws StorageException {
        try (InputStream is = file.getInputStream()) {
            ensureBucketExists();

            String contentType = file.getContentType();
            if (contentType == null) contentType = "application/octet-stream";

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(is, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            return String.format("%s/%s/%s", endpoint, bucket, fileName);
        } catch (Exception e) {
            throw new StorageException("Failed to upload image to MinIO", e);
        }
    }

    // ✅ Option 2: Upload from byte[]
    @Override
    public String upload(byte[] fileBytes, String fileName) throws StorageException {
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            ensureBucketExists();

            String contentType = java.nio.file.Files.probeContentType(java.nio.file.Paths.get(fileName));
            if (contentType == null) contentType = "application/octet-stream";

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(is, fileBytes.length, -1)
                            .contentType(contentType)
                            .build()
            );

            return String.format("%s/%s/%s", endpoint, bucket, fileName);
        } catch (Exception e) {
            throw new StorageException("Failed to upload image to MinIO", e);
        }
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }
}

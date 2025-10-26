package org.example.resturent.service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;

import org.example.resturent.exeptions.custom.StorageException;
import org.example.resturent.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class MinioImageStorageService implements ImageStorageService {

    private final MinioClient minioClient;
    private final String bucket;
    private final String endpoint;  // maybe for URL building

    public MinioImageStorageService(MinioClient minioClient,
                                    @Value("${minio.bucket}") String bucket,
                                    @Value("${minio.endpoint}") String endpoint) {
        this.minioClient = minioClient;
        this.bucket = bucket;
        this.endpoint = endpoint;
    }

    @Override
    public String upload(byte[] fileBytes, String fileName) throws StorageException {
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            // ensure bucket exists
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            // upload object
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(is, fileBytes.length, -1)
                    .build());
            // build public URL (assuming bucket is public)
            String url = String.format("%s/%s/%s", endpoint, bucket, fileName);
            return url;
        } catch (Exception e) {
            throw new StorageException("Failed to upload image to MinIO", e);
        }
    }
}

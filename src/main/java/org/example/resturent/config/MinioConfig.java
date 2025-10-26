package org.example.resturent.config;

import io.minio.MinioClient;
import org.example.resturent.service.ImageStorageService;
import org.example.resturent.service.impl.MinioImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "MINIO")
public class MinioConfig {

    @Bean
    public MinioClient minioClient(@Value("${minio.endpoint}") String endpoint,
                                   @Value("${minio.accessKey}") String accessKey,
                                   @Value("${minio.secretKey}") String secretKey,
                                   @Value("${minio.useSsl:false}") boolean useSsl) {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    public ImageStorageService imageStorageService(MinioClient client,
                                                   @Value("${minio.bucket}") String bucket,
                                                   @Value("${minio.endpoint}") String endpoint) {
        return new MinioImageStorageService(client, bucket, endpoint);
    }
}

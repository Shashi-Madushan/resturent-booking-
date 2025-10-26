package org.example.resturent.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.resturent.service.ImageStorageService;
import org.example.resturent.service.impl.CloudinaryImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "CLOUDINARY")
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(@Value("${cloudinary.cloudName}") String cloudName,
                                 @Value("${cloudinary.apiKey}") String apiKey,
                                 @Value("${cloudinary.apiSecret}") String apiSecret) {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Bean
    public ImageStorageService imageStorageService(Cloudinary cloudinary) {
        return new CloudinaryImageStorageService(cloudinary);
    }
}

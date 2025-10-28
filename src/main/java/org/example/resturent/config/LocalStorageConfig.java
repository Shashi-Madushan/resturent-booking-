package org.example.resturent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "LOCAL")
public class LocalStorageConfig implements WebMvcConfigurer {

    private final Path storagePath;
    private final String publicPath;

    public LocalStorageConfig(@Value("${storage.local.directory:${local.directory:uploads}}") String storageDirectory,
                              @Value("${storage.local.public-path:${local.public-path:uploads}}") String publicPath) {
        this.storagePath = Paths.get(storageDirectory).toAbsolutePath().normalize();
        this.publicPath = normalizePublicPath(publicPath);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (publicPath.isBlank()) {
            return;
        }
        String resourceHandler = "/" + publicPath + "/**";
        String resourceLocation = storagePath.toUri().toString();
        if (!resourceLocation.endsWith("/")) {
            resourceLocation = resourceLocation + "/";
        }
        registry.addResourceHandler(resourceHandler)
                .addResourceLocations(resourceLocation);
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

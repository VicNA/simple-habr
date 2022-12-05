package ru.geekbrains.habr.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class ResourcePathConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String myUploadPath = StringUtils.replace(new File(uploadPath).getAbsolutePath(), uploadPath, "");
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///" + myUploadPath);
    }
}

package com.example.XDMHPL_Back_end.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigU implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        registry
                .addResourceHandler("/assets/**")
                .addResourceLocations("file:assets/"); // đường dẫn tương đối so với project root
        registry
                .addResourceHandler("/public/**")
                .addResourceLocations("file:public/"); // đường dẫn tương đối so với project root
    }
}

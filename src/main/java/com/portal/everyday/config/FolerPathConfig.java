package com.portal.everyday.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FolerPathConfig implements WebMvcConfigurer {//interface

//	 @Value("${file.upload-dir}")
//	    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) 
    {
//        registry.addResourceHandler("/employee/**")
//                .addResourceLocations("file:employee/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/"); // **-> traverse all folders/files in uploads
        
//        registry.addResourceHandler("/uploadFiles/**")
//        .addResourceLocations("file:"+uploadDir+"/");


       
    }
}
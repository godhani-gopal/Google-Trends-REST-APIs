package com.googleTrendsBigQuery.googleTrendsRestApis;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "SpringBoot Google Trends APIs",
        description = "This API provides REST endpoints for accessing Google Trends data. The application does not persist any user data to ensure privacy.",
        version = "v1.0",
        license = @License(
                name = "Apache 2.0"
        )),
        externalDocs = @ExternalDocumentation(
                description = "GitHub",
                url = "https://github.com/godhani-gopal/Google-Trends-REST-APIs/"
        ))
public class GoogleTrendsRestApisApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoogleTrendsRestApisApplication.class, args);
    }
}
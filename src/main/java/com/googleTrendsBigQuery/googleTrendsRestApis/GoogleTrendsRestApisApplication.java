package com.googleTrendsBigQuery.googleTrendsRestApis;

import com.googleTrendsBigQuery.googleTrendsRestApis.downloader.FileDownloadInitializer;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
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

@SpringBootApplication
@PropertySource("file:./application.properties")
public class GoogleTrendsRestApisApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GoogleTrendsRestApisApplication.class);
        app.addInitializers(new FileDownloadInitializer());
        app.run(args);
//        need to add frontend
    }

}
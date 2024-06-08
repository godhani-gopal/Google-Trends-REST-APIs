package com.google_trends.google_trends_rest_apis;

import com.google_trends.google_trends_rest_apis.service.BigQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoogleTrendsRestApisApplication {

    @Autowired
    private BigQueryService bigQueryService;

    public static void main(String[] args) {
        SpringApplication.run(GoogleTrendsRestApisApplication.class, args);
    }

}

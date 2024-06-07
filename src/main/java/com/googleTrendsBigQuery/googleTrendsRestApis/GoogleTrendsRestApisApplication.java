package com.googleTrendsBigQuery.googleTrendsRestApis;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.FileDownloaderUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoogleTrendsRestApisApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleTrendsRestApisApplication.class, args);
    }

}


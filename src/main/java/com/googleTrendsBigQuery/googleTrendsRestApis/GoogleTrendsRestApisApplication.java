package com.googleTrendsBigQuery.googleTrendsRestApis;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
<<<<<<< HEAD
import com.googleTrendsBigQuery.googleTrendsRestApis.util.FileDownloaderUtility;
=======
>>>>>>> 936544c (get top terms by country name)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GoogleTrendsRestApisApplication {

<<<<<<< HEAD
=======
    @Autowired
    private InternationalTopRisingTermsService bigQueryService;

>>>>>>> 936544c (get top terms by country name)
    public static void main(String[] args) {
        SpringApplication.run(GoogleTrendsRestApisApplication.class, args);
    }

}
<<<<<<< HEAD

=======
>>>>>>> 936544c (get top terms by country name)

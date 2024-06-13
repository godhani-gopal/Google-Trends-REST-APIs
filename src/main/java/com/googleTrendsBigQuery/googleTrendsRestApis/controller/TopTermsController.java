package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/top-terms")
public class TopTermsController {

    TopTermsService topTermsService;

    public TopTermsController(TopTermsService topTermsService) {
        this.topTermsService = topTermsService;
    }

    @GetMapping("/load-data-from-bigquery")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = topTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "saved successfully.");
    }
}

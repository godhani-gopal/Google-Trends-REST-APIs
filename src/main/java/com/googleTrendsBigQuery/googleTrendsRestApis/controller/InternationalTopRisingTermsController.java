package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/international-top-rising-terms")
public class InternationalTopRisingTermsController {

    private final InternationalTopRisingTermsService internationalTopRisingTermsService;

    public InternationalTopRisingTermsController(InternationalTopRisingTermsService internationalTopRisingTermsService) {
        this.internationalTopRisingTermsService = internationalTopRisingTermsService;
    }

    @GetMapping("/load-data-from-bigquery")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = internationalTopRisingTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "saved successfully.");
    }
}

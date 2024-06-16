package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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

    @GetMapping("/load-data-from-bigquery/latest")
    public ResponseEntity<String> loadLatestData() {
        Long totalRecordsSaved = internationalTopRisingTermsService.saveLatestDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "added.");
    }

    @GetMapping("/latest-week")
    public ResponseEntity<LocalDate> findLatestWeekValue() {
        return ResponseEntity.ok(internationalTopRisingTermsService.findLatestWeekValue());
    }

    @GetMapping("/predictive-insights")
    public TermAnalysis getPredictiveInsights(@RequestBody InternationalTopRisingTerms internationalTopRisingTerms) {
        return internationalTopRisingTermsService.getPredictiveInsights(internationalTopRisingTerms);
    }

}

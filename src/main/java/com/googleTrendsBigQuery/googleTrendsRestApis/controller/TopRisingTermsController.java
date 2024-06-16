package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopRisingTermsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/top-rising-terms")
public class TopRisingTermsController {
    TopRisingTermsService topRisingTermsService;

    public TopRisingTermsController(TopRisingTermsService topRisingTermsService) {
        this.topRisingTermsService = topRisingTermsService;
    }

    @GetMapping("/load-data-from-bigquery")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = topRisingTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "saved successfully.");
    }

    @GetMapping("/load-data-from-bigquery/latest")
    public ResponseEntity<String> loadLatestData() {
        Long totalRecordsSaved = topRisingTermsService.saveLatestDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "added.");
    }

    @GetMapping("/latest-week")
    public ResponseEntity<LocalDate> findLatestWeekValue() {
        return ResponseEntity.ok(topRisingTermsService.findLatestWeekValue());
    }
}

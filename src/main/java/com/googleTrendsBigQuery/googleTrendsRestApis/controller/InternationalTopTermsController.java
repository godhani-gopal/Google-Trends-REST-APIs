package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopTermsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/international-top-terms")
@Validated
public class InternationalTopTermsController {

    InternationalTopTermsService internationalTopTermsService;

    public InternationalTopTermsController(InternationalTopTermsService internationalTopTermsService) {
        this.internationalTopTermsService = internationalTopTermsService;
    }

    @GetMapping("/latest")
    public List<InternationalTopTerms> getTopTermDetails(
            @RequestParam(name = "week", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate week,
            @RequestParam(name = "score", required = false) @Min(1) @Max(100) Integer score,
            @RequestParam(name = "rank", required = false) @Min(1) @Max(25) Integer rank,
            @RequestParam(name = "refreshDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate refreshDate,
            @RequestParam(name = "countryName", required = false) String countryName,
            @RequestParam(name = "countryCode", required = false) @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Country code must be exactly two alphabetical characters")
            String countryCode,
            @RequestParam(name = "regionName", required = false) String regionName,
            @RequestParam(name = "regionCode", required = false) String regionCode,
            @RequestParam(name = "numOfTerms", required = false, defaultValue = "25") int numOfTerms) {
        return null;
    }

    @GetMapping("/load-data-from-bigquery")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = internationalTopTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "saved successfully.");
    }
}
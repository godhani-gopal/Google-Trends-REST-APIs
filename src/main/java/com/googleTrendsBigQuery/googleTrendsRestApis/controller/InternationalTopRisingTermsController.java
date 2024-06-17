package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/international-top-rising-terms")
public class InternationalTopRisingTermsController {
    private final InternationalTopRisingTermsService internationalTopRisingTermsService;

    public InternationalTopRisingTermsController(InternationalTopRisingTermsService internationalTopRisingTermsService) {
        this.internationalTopRisingTermsService = internationalTopRisingTermsService;
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedModel<InternationalTopRisingTerms>> getInternationalTopRisingTermDetails(
            @RequestParam(name = "term", required = false) String term,
            @RequestParam(name = "percentGain", required = false) @Min(0) Integer percentGain,
            @RequestParam(name = "week", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate week,
            @RequestParam(name = "score", required = false) @Min(0) @Max(100) Integer score,
            @RequestParam(name = "rank", required = false) @Min(1) @Max(25) Integer rank,
            @RequestParam(name = "refreshDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate refreshDate,
            @RequestParam(name = "countryName", required = false) @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Country name must be alphabetical characters") String countryName,
            @RequestParam(name = "countryCode", required = false) @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Country code must be exactly two alphabetical characters") String countryCode,
            @RequestParam(name = "regionName", required = false) @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Region name must be alphabetical characters") String regionName,
            @RequestParam(name = "regionCode", required = false) String regionCode,
            @RequestParam(name = "numOfTerms", required = false) @Min(1) Integer numOfTerms,
            @PageableDefault(size = 10, sort = "week", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(internationalTopRisingTermsService.getTerms(term, percentGain, week, score, rank, refreshDate, countryName, countryCode, regionName, regionCode, numOfTerms, pageable));
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

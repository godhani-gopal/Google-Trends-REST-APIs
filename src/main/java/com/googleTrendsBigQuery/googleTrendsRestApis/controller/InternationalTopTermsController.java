package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopTermsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/international-top-terms")
@Validated
public class InternationalTopTermsController {

    InternationalTopTermsService internationalTopTermsService;
    ChatClient chatClient;

    public InternationalTopTermsController(InternationalTopTermsService internationalTopTermsService, ChatClient chatClient) {
        this.internationalTopTermsService = internationalTopTermsService;
        this.chatClient = chatClient;
    }

    @GetMapping("/paginated")
    public ResponseEntity<PagedModel<InternationalTopTerms>> getTopTermDetails(
            @RequestParam(name = "term", required = false) String term,
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
        Page<InternationalTopTerms> page = internationalTopTermsService.getTerms(term, week, score, rank, refreshDate, countryName, countryCode, regionName, regionCode, numOfTerms, pageable);

        return ResponseEntity.ok(PagedModel.of(page.getContent(),
                new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages())));
    }

    @GetMapping("/search/terms")
    public ResponseEntity<List<String>> getMatchingTerms(@RequestParam(name = "term") String term) {
        return ResponseEntity.ok(internationalTopTermsService.getMatchingTerms(term));
    }

    @GetMapping("/load-data-from-bigquery")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = internationalTopTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "saved successfully.");
    }

    @GetMapping("/load-data-from-bigquery/latest")
    public ResponseEntity<String> loadLatestData() {
        Long totalRecordsSaved = internationalTopTermsService.saveLatestDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + " new records added.");
    }

    @GetMapping("/latest-week")
    public ResponseEntity<LocalDate> findLatestWeekValue() {
        return ResponseEntity.ok(internationalTopTermsService.findLatestWeekValue());
    }

    @GetMapping("/predictive-insights")
    public TermAnalysis getPredictiveInsights(@RequestBody InternationalTopTerms internationalTopTerms) {
        return internationalTopTermsService.getPredictiveInsights(internationalTopTerms);
    }
}
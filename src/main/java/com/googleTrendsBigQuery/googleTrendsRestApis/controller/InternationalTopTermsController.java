package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopTermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/international-top-terms")
@Validated
@Tag(name = "APIs for International Top Terms")
public class InternationalTopTermsController {

    InternationalTopTermsService internationalTopTermsService;
    ChatClient chatClient;

    public InternationalTopTermsController(InternationalTopTermsService internationalTopTermsService, ChatClient chatClient) {
        this.internationalTopTermsService = internationalTopTermsService;
        this.chatClient = chatClient;
    }

    @Operation(summary = "Retrieve International Top Terms", description = "Get a paginated list of international top terms with optional filters and sorting capabilities.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the values",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InternationalTopTerms.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping("/paginated")
    public ResponseEntity<PagedModel<InternationalTopTerms>> getTopTermDetails(
            @Parameter(description = "The human readable identifier for a term in local language. Example = “Acme Inc”.")
            @RequestParam(name = "term", required = false) String term,

            @Parameter(description = "First day of the week (ISO format) for the current row’s position in the time series for combination of term, country, region, and score . If the specified date does not exist, the API response will include data from the closest available week in the database. Example = 2024-04-07")
            @RequestParam(name = "week", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate week,

            @Parameter(description = "Index from 0-100 that denotes how popular this term was for a country’s region during the current date, relative to the other dates in the same time series for this term (260 weeks = 52 weeks * 5 years")
            @RequestParam(name = "score", required = false) @Min(0) @Max(100) Integer score,

            @Parameter(description = "Numeric representation of where the term falls in comparison to the other top terms for the day across the globe (e.g. rank of 1-25).")
            @RequestParam(name = "rank", required = false) @Min(1) @Max(25) Integer rank,

            @Parameter(description = "Date when the set of term, country, region, and score combination was added. Example = 2024-04-10")
            @RequestParam(name = "refreshDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate refreshDate,

            @Parameter(description = "Full text name of the country. Example = South Korea")
            @RequestParam(name = "countryName", required = false) @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Country name must be alphabetical characters") String countryName,

            @Parameter(description = "The ISO 3166 Alpha-2 country code of a country. example = KR")
            @RequestParam(name = "countryCode", required = false) @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Country code must be exactly two alphabetical characters") String countryCode,

            @Parameter(description = "Full text name of the region or state in the country. Example = Seoul")
            @RequestParam(name = "regionName", required = false) @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Region name must be alphabetical characters") String regionName,

            @Parameter(description = "ISO 3166-2 country subdivision code used to identify the region or state in the country. Example = KR-11")
            @RequestParam(name = "regionCode", required = false) String regionCode,

            @Parameter(description = "Total number of terms to be included in the response")
            @RequestParam(name = "numOfTerms", required = false) @Min(1) Integer numOfTerms,

            @Parameter(
                    description = "Pagination and sorting parameters for the result list.",
                    name = "sort", schema = @Schema(type = "string"), in = ParameterIn.QUERY)
            @PageableDefault(sort = "rank", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<InternationalTopTerms> page = internationalTopTermsService.getTerms(term, week, score, rank, refreshDate, countryName, countryCode, regionName, regionCode, numOfTerms, pageable);

        return ResponseEntity.ok(PagedModel.of(page.getContent(),
                new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages())));
    }

    @Operation(
            summary = "Search for terms",
            description = "Searches and returns a list of terms containing the specified substring."
    )
    @GetMapping("/search/terms")
    public ResponseEntity<List<String>> getMatchingTerms(@Parameter(description = "Substring to search within terms") @RequestParam(name = "term") String term) {
        return ResponseEntity.ok(internationalTopTermsService.getMatchingTerms(term));
    }

    @Operation(summary = "Load data from BigQuery to MySQL",
            description = "Loads 50,000 rows from the Google Cloud Platform BigQuery International Top Terms table into a MySQL database hosted on a VPS.")
    @PostMapping("/load-data-from-bigquery")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = internationalTopTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "saved successfully.");
    }

    @Operation(summary = "Load latest data from BigQuery to MySQL",
            description = "Finds and saves data from the most recent week in the Google Cloud Platform BigQuery International Top Terms table to a MySQL database hosted on a VPS, only if the week value is later than the latest existing week in MySQL.")
    @PostMapping("/load-data-from-bigquery/latest")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> loadLatestData() {
        Long totalRecordsSaved = internationalTopTermsService.saveLatestDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + " new records added.");
    }

    @Operation(summary = "Find latest week",
            description = "Find the most recent value of the week")
    @GetMapping("/latest-week")
    public ResponseEntity<LocalDate> findLatestWeekValue() {
        return ResponseEntity.ok(internationalTopTermsService.findLatestWeekValue());
    }

    @Operation(summary = "Get predictive insights from OpenAI's ChatGPT",
            description = "Get predictive and analytical insights on the provided international top terms data by processing it through OpenAI's ChatGPT model.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of predictive insights",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TermAnalysis.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request format or parameters")
    })
    @PostMapping("/predictive-insights")
    public TermAnalysis getPredictiveInsights(@RequestBody InternationalTopTerms internationalTopTerms) {
        return internationalTopTermsService.getPredictiveInsights(internationalTopTerms);
    }
}
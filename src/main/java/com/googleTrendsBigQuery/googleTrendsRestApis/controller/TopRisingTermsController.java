package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopRisingTermsService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/top-rising-terms")
@Tag(name = "Top USA Rising Terms API", description = "APIs for fetching and analyzing top rising terms in the USA by percentage gain")
@Validated
public class TopRisingTermsController {

    private final TopRisingTermsService topRisingTermsService;

    public TopRisingTermsController(TopRisingTermsService topRisingTermsService) {
        this.topRisingTermsService = topRisingTermsService;
    }

    @PostMapping("/load-data-from-bigquery")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = topRisingTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + " records saved successfully.");
    }

    @PostMapping("/load-data-from-bigquery/latest")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> loadLatestData() {
        Long totalRecordsSaved = topRisingTermsService.saveLatestDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + " records added.");
    }

    @GetMapping("/latest-week")
    public ResponseEntity<LocalDate> findLatestWeekValue() {
        return ResponseEntity.ok(topRisingTermsService.findLatestWeekValue());
    }

    @Operation(summary = "Get Rising Terms Data", description = "Fetches top rising terms in the USA based on the provided query parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rising terms data retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopRisingTerms.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/paginated")
    public ResponseEntity<?> getTopRisingTerms(
            @Parameter(description = "Full text name of the Designated Market Area (DMA). Examples: 'New York', 'Los Angeles', 'Chicago'")
            @RequestParam(required = false) @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "DMA name must be alphabetical characters") String dmaName,

            @Parameter(description = "3-digit numerical ID used to identify a DMA. Examples: '123', '456', '789'")
            @RequestParam(required = false) String dmaId,

            @Parameter(description = "First day of the week for the current row's position in the time series. Examples: '2024-05-27', '2024-06-03'")
            @RequestParam(required = false) String week,

            @Parameter(description = "Human-readable identifier for a term or topic being tracked. Examples: 'Technology', 'Solar', 'Election'")
            @RequestParam(required = false) String term,

            @Parameter(description = "Percentage gain at which the term rose compared to the previous date period. Examples: '450' (4.5% increase)")
            @RequestParam(required = false) Integer percentGain,

            @Parameter(description = "Numeric representation of where the term falls in comparison to other top terms for the day across the US market. Values range from 1 to 25.")
            @RequestParam(required = false) @Min(value = 1, message = "Rank must be between 1 and 25") @Max(value = 25, message = "Rank must be between 1 and 25") Integer rank,

            @Parameter(description = "Index from 0-100 denoting how popular a term was for a DMA during the current date. Examples: 85 (high interest), 50 (moderate interest)")
            @RequestParam(required = false) @Min(value = 1, message = "Score must be between 1 and 100") @Max(value = 100, message = "Score must be between 1 and 100") Integer score,

            @Parameter(description = "Page number for pagination. Default is 0.")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be greater than or equal to 0") int page,

            @Parameter(description = "Page size for pagination. Default is 10.")
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be greater than 0") int pageSize,

            @Parameter(description = "Sorting parameters for the result list, specified as 'field,order'. E.g., 'rank,asc'. Defaults to sorting by 'rank' in ascending order.",
                    name = "sort", schema = @Schema(type = "string"), in = ParameterIn.QUERY)
            @PageableDefault(sort = "rank", direction = Sort.Direction.ASC) Pageable pageable) {

        Pageable customPageable = PageRequest.of(page, pageSize, pageable.getSort());
        return ResponseEntity.ok(topRisingTermsService.getTopRisingTerms(term, dmaName, dmaId, week, rank, score, percentGain, customPageable));
    }

    @GetMapping("/predictive-insights")
    public TermAnalysis getPredictiveInsights(@RequestBody TopRisingTerms topRisingTerms) {
        return topRisingTermsService.getPredictiveInsights(topRisingTerms);
    }
}

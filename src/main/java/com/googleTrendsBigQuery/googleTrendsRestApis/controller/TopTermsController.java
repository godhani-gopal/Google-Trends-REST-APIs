package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/top-terms")
@Validated
@Tag(name = "Top USA Terms API", description = "APIs for fetching and analyzing trending top terms in the USA, including top terms, rising terms, and international trends")
public class TopTermsController {

    private final TopTermsService topTermsService;
    private final TopTermsRepository topTermsRepository;

    public TopTermsController(TopTermsService topTermsService, TopTermsRepository topTermsRepository) {
        this.topTermsService = topTermsService;
        this.topTermsRepository = topTermsRepository;
    }

    @GetMapping("/load-data-from-bigquery")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = topTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + " records saved successfully.");
    }

    @Operation(summary = "Get Top USA Terms Data", description = "Fetches top terms for the USA based on the provided query parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Terms data retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopTerms.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/paginated")
    public ResponseEntity<?> getTopTerms(
            @Parameter(description = "Full text name of the Designated Market Area (DMA). Examples: 'New York', 'Los Angeles', 'Chicago'")
            @RequestParam(required = false) @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "DMA name must be alphabetical characters") String dmaName,

            @Parameter(description = "3-digit numerical ID used to identify a DMA. Examples: 501 (New York), 803 (Los Angeles), 602 (Chicago)")
            @RequestParam(required = false) String dmaId,

            @Parameter(description = "First day of the week for the current row's position in the time series. Examples: '2024-05-27', '2024-06-03'")
            @RequestParam(required = false) String week,

            @Parameter(description = "Human-readable identifier for a term or topic being tracked. Examples: 'Acme Inc', 'Olympics 2024', 'Super Bowl'")
            @RequestParam(required = false) String term,

            @Parameter(description = "Numeric representation of where the term falls in comparison to other top terms for the day. Values range from 1 to 25.")
            @RequestParam(required = false) @Min(value = 1, message = "Rank must be between 1 and 25") @Max(value = 25, message = "Rank must be between 1 and 25") Integer rank,

            @Parameter(description = "Index from 0-100 denoting how popular a term was for a DMA during the current date. Examples: 95 (very high interest), 50 (moderate interest)")
            @RequestParam(required = false) @Min(value = 1, message = "Score must be between 1 and 100") @Max(value = 100, message = "Score must be between 1 and 100") Integer score,

            @Parameter(description = "Page number for pagination. Default is 0.")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be greater than or equal to 0") int page,

            @Parameter(description = "Page size for pagination. Default is 10.")
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be greater than 0") int pageSize,

            @Parameter(description = "Sorting parameters for the result list, specified as 'field,order'. Defaults to sorting by 'rank' in ascending order.",
                    name = "sort", schema = @Schema(type = "string"), in = ParameterIn.QUERY)
            @PageableDefault(sort = "rank", direction = Sort.Direction.ASC) Pageable pageable) {

        Pageable customPageable = PageRequest.of(page, pageSize, pageable.getSort());
        return ResponseEntity.ok(topTermsService.getTopTerms(term, dmaName, dmaId, week, rank, score, customPageable));
    }

    @Operation(summary = "Get Predictive Insights", description = "Fetches predictive insights based on the provided terms.")
    @GetMapping("/predictive-insights")
    public TermAnalysis getPredictiveInsights(@RequestBody Object object) {
        return topTermsService.getPredictiveInsights(object);
    }
}

package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/top-terms")
@Validated
@Tag(name = "APIs for US Top Terms")
public class TopTermsController {

    TopTermsService topTermsService;
    TopTermsRepository topTermsRepository;

    public TopTermsController(TopTermsService topTermsService, TopTermsRepository topTermsRepository) {
        this.topTermsService = topTermsService;
        this.topTermsRepository = topTermsRepository;
    }

    @GetMapping("/load-data-from-bigquery")
    public ResponseEntity<String> loadData() {
        Long totalRecordsSaved = topTermsService.saveDataFromBQtoMySQL();
        return ResponseEntity.ok("Total " + totalRecordsSaved + "saved successfully.");
    }

    // You can give any parameter any value and also without value this method work its flexible
    @GetMapping
    public ResponseEntity<?> TopTerms(
            @RequestParam(required = false) String dmaName,
            @RequestParam(required = false) String dmaId,
            @RequestParam(required = false) String week,
            @RequestParam(required = false) @Min(value = 1, message = "Rank must be between 1 and 25") @Max(value = 25, message = "Rank must be between 1 and 25") Integer rank,
            @RequestParam(required = false) @Min(value = 1, message = "Score must be between 1 and 100") @Max(value = 100, message = "Score must be between 1 and 100") Integer score,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be greater than 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "PageSize must be greater than 0") int pageSize,
            // Annotating with @Valid ensures validation of request parameters
            @PageableDefault(sort = "rank", direction = Sort.Direction.ASC) Pageable pageable) {

        return null;

        /*
        This method also makesure date is valid and not in future
         LocalDate parsedWeek = DateUtils.parseWeek(week);
         Create custom pageable with default values for page and pageSize
         Pageable customPageable = PageRequest.of(page, pageSize, pageable.getSort());
         return ResponseEntity.ok(topTermsService.getTopTerms(dmaName, dmaId, parsedWeek, rank, score, customPageable));
         */
    }


}

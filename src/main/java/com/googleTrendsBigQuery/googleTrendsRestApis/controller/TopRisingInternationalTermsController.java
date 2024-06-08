package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopRisingInternationalTermsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/international/top/rising")
public class TopRisingInternationalTermsController {

    private final TopRisingInternationalTermsService topRisingInternationalTermsService;

    public TopRisingInternationalTermsController(TopRisingInternationalTermsService topRisingInternationalTermsService) {
        this.topRisingInternationalTermsService = topRisingInternationalTermsService;
    }

    @GetMapping("/terms/{countryName}")
    public List<String> getTopTerms(@PathVariable(name = "countryName") String countryName,
                                    @RequestParam(name = "limit") int limit)
            throws InterruptedException {
        return topRisingInternationalTermsService.getTopTermsByCountry(countryName, limit);
    }


}

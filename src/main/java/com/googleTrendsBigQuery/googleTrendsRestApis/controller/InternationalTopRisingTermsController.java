package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/international/top/rising")
public class InternationalTopRisingTermsController {

    private final InternationalTopRisingTermsService internationalTopRisingTermsService;

    public InternationalTopRisingTermsController(InternationalTopRisingTermsService internationalTopRisingTermsService) {
        this.internationalTopRisingTermsService = internationalTopRisingTermsService;
    }

    @GetMapping("/terms/{countryName}")
    public List<String> getTopTerms(@PathVariable(name = "countryName") String countryName,
                                    @RequestParam(name = "limit",required = false) int limit)
            throws InterruptedException {
        return internationalTopRisingTermsService.getTopTermsByCountry(countryName, limit);
    }
}

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
<<<<<<< HEAD
                                    @RequestParam(name = "limit",required = false) int limit)
=======
                                    @RequestParam(name = "limit") int limit)
>>>>>>> 936544c (get top terms by country name)
            throws InterruptedException {
        return internationalTopRisingTermsService.getTopTermsByCountry(countryName, limit);
    }
}

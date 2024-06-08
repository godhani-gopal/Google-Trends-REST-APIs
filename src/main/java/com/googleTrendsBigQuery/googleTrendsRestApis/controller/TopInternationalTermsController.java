package com.googleTrendsBigQuery.googleTrendsRestApis.controller;

import com.googleTrendsBigQuery.googleTrendsRestApis.payload.InternationalTermDetails;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopInternationalTermsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/international/top")
public class TopInternationalTermsController {

    TopInternationalTermsService topInternationalTermsService;

    public TopInternationalTermsController(TopInternationalTermsService topInternationalTermsService) {
        this.topInternationalTermsService = topInternationalTermsService;
    }

    @GetMapping("/terms")
    public List<InternationalTermDetails> getTopTermDetails() {
        return topInternationalTermsService.getTopInternationalTermDetails();
    }

}

package com.google_trends.google_trends_rest_apis.controller;

import com.google_trends.google_trends_rest_apis.service.BigQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/international")
public class BigQueryController {

    BigQueryService bigQueryService;

    public BigQueryController(BigQueryService bigQueryService) {
        this.bigQueryService = bigQueryService;
    }

    @GetMapping("/getTopTerms/{countryName}")
    public List<String> getTopTerms (@PathVariable String countryName) throws InterruptedException {
        return bigQueryService.getTopTermsFromCountry(countryName);
    }
}

package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InternationalTopRisingTermsService {

    List<String> getTopTermsByCountry(String countryName, int limit) throws InterruptedException;
}

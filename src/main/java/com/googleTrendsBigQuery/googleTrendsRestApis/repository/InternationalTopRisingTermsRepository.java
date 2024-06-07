package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternationalTopRisingTermsRepository {
    List<String> getTopRisingTermsByCountry(String countryName, int limit) throws InterruptedException;
}

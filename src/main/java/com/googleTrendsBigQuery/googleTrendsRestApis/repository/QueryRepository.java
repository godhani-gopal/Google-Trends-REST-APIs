package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface QueryRepository {
    String getTopRisingTermsByCountryQuery(String countryName, int limit);

}

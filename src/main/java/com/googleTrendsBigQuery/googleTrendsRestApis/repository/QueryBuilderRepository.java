package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface QueryBuilderRepository {
    String getTopRisingTermsInternationalQuery(String countryName, int limit);
}

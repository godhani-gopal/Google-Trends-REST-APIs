package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

public interface QueryBuilderRepository {
    String getTopRisingTermsInternationalQuery(String countryName, int limit);
}

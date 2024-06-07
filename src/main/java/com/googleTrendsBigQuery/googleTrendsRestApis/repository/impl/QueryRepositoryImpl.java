package com.googleTrendsBigQuery.googleTrendsRestApis.repository.impl;

import com.googleTrendsBigQuery.googleTrendsRestApis.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class QueryRepositoryImpl implements QueryRepository {

    @Value("${query.selectTopRisingTermsByCountry}")
    private String query_topTermsFromCountry;

    @Override
    public String getTopRisingTermsByCountryQuery(String countryName, int limit) {
        return String.format(query_topTermsFromCountry, countryName, limit);
    }
}

package com.googleTrendsBigQuery.googleTrendsRestApis.repository.impl;

import com.googleTrendsBigQuery.googleTrendsRestApis.repository.QueryBuilderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QueryBuilderRepositoryImpl implements QueryBuilderRepository {
    @Override
    public String getTopRisingTermsInternationalQuery(String countryName, int limit) {
        String query = "";
        try {
            query = "SELECT distinct(term) FROM `bigquery-public-data.google_trends.international_top_rising_terms`";
            if (!(countryName == null || countryName.isEmpty())) {
                query = query + " where country_name like '%" + countryName + "%'";
            }
            if (limit > 25 || limit <= 0) {
                throw new Exception(("Invalid Input! Maximum 25 records available!"));
            } else {
                query = query + " limit " + limit;
            }
        } catch (Exception e) {
            query = "";
            System.out.println("Invalid Input entered. Cannot Generate Query.");
        }
        System.out.println(query);
        return query;
    }
}
